package alex_gontarenko.testinstagramcollage.Loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;

import alex_gontarenko.testinstagramcollage.BaseClass.InstagramMediaImage;
import alex_gontarenko.testinstagramcollage.Parcers.InstagramImageParcer;

/**
 * Created by Alex on 22.06.2014.
 */
public class PopularImageLoader extends Loader<ArrayList<InstagramMediaImage>> {

    private static final String LOG_TAG = "PopularImageLoader";
    public static final String TAG_URL = "PopularImageLoader_URL";

    private String _url;

    PopularImageAsyncTask _imageListLoader;

    public PopularImageLoader(Context context, Bundle args) {
        super(context);
        if(args.containsKey(TAG_URL))
            _url = args.getString(TAG_URL);
        else
            _url=null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (_imageListLoader != null)
            _imageListLoader.cancel(true);
        _imageListLoader = new PopularImageAsyncTask();
        _imageListLoader.execute(_url);

    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (_imageListLoader != null)
            _imageListLoader.cancel(true);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        Log.d(LOG_TAG, hashCode() + " onAbandon");
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d(LOG_TAG, hashCode() + " onReset");
    }

    class PopularImageAsyncTask extends AsyncTask<String, Void, ArrayList<InstagramMediaImage>> {
        @Override
        protected ArrayList<InstagramMediaImage> doInBackground(String... params) {
            HttpsURLConnection httpsURLConnection = null;
            ArrayList<InstagramMediaImage> array=null;
            String linkUrl = params[0];
            if(linkUrl!=null&&!linkUrl.isEmpty()) {

                try {
                    URL url = new URL(linkUrl);
                    httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setDoInput(true);

                    InstagramImageParcer parcer = new InstagramImageParcer();
                    array = parcer.parceData(httpsURLConnection.getInputStream());

                } catch (Exception e) {
                    array=null;
                    Log.e(LOG_TAG, e.toString());
                } finally {
                    if (httpsURLConnection != null)
                        httpsURLConnection.disconnect();
                }
            }
            if(array!=null&&!array.isEmpty())
                Collections.sort(array, new Comparator<InstagramMediaImage>() {
                    @Override
                    public int compare(InstagramMediaImage l, InstagramMediaImage r) {
                        return r.getLikes()-l.getLikes();
                    }
                });
            return array;
        }

        @Override
        protected void onPostExecute(ArrayList<InstagramMediaImage> result) {
            super.onPostExecute(result);
            deliverResult(result);

        }

    }
}
