package alex_gontarenko.testinstagramcollage.Loaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by Alex on 22.06.2014.
 */
public class ImageLoader {

    private static final String LOG_TAG = "ImageLoader";
    private static final int MAX_COUNT = 3;

    private LinkedList<TaskLoad> _taskList;
    private static  ImageLoader _instance;
    private int _count;

    protected class TaskLoad{
        public String _URL;
        public ImageView _view;
        public Bitmap _bitmap;
        public TaskLoad(String stringURL, ImageView view){
            _URL = stringURL;
            _view = view;
        }
    }

    protected ImageLoader() {
        _count = 0;
        _taskList = new LinkedList<TaskLoad>();
    }

    public synchronized static ImageLoader instance(){
        if(_instance==null)
            _instance = new ImageLoader();
        return _instance;
    }

    public void load(String stringURL, ImageView view ){
        if(stringURL==null || view==null) return;
        ImageLoader loader = instance();
        synchronized (_taskList){
            _taskList.addLast(new TaskLoad(stringURL, view));
        }
        startLoad();
    }

    protected synchronized void startLoad(){
        Thread loaderThread;
        if(_count<MAX_COUNT){
            _count++;
            loaderThread = new Thread(new AsyncLoader(this,new Handler() {
                @Override
                public void handleMessage(Message message) {
                    TaskLoad task = (TaskLoad) message.obj;
                    task._view.setImageBitmap(task._bitmap);
                }
            }));
            loaderThread.setPriority(3);
            loaderThread.start();
        }
    }

    public void stop(){
        ImageLoader loader = instance();
        synchronized (_taskList){
            _taskList.clear();
        }
    }

    protected synchronized TaskLoad getTask(){
        TaskLoad task = null;
        if(!_taskList.isEmpty()){
            task=_taskList.getFirst();
            _taskList.removeFirst();
        }
        return task;
    }

    protected synchronized void threadTerminated(){
        _count--;
    }

    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream buf_stream = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            buf_stream = new BufferedInputStream(httpURLConnection.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, "Url parsing was failed: " + url);
        } catch (IOException ex) {
            Log.e(LOG_TAG, url + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.e(LOG_TAG, "Out of memory!!!");
            return null;
        } finally {
            try {
                if ( buf_stream != null )
                 buf_stream.close();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Error close buffer stream");
            }
            if ( httpURLConnection != null )
                httpURLConnection.disconnect();
        }
        return bitmap;
    }

    protected class AsyncLoader implements Runnable{

        private ImageLoader _loader;
        private Handler _handler;

        public AsyncLoader(ImageLoader loader,Handler handler){
            _loader=loader;_handler = handler;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            TaskLoad task=null;
            boolean running = true;
            while(running) {
                task = getTask();
                if (task != null) {
                    if(task._URL!=null || task._view!=null) {
                        bitmap = downloadImage(task._URL);
                        if (bitmap != null && task._view != null){
                            task._bitmap=bitmap;
                            Message message = _handler.obtainMessage(1, task);
                            _handler.sendMessage(message);
                        }
                    }
                } else {
                    _loader.threadTerminated();
                    running=false;
                }
            }
            return;
        }
    }
}
