package alex_gontarenko.testinstagramcollage.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import alex_gontarenko.testinstagramcollage.CollageCreator.CollageCreator;
import alex_gontarenko.testinstagramcollage.Listners.LogoutListner;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 24.06.2014.
 */
public class FragmentCreateCollage extends Fragment implements View.OnClickListener {

    public static final String IMAGES_TAG="FragmentCreateCollage_IMAGES_TAG";
    private static final String LOG_TAG="FragmentCreateCollage";


    private FrameLayout _processDialog,_messageView;
    private LinearLayout _collageContainer;
    private ImageView _imageView;
    private EditText _emailView;

    private String _email;
    private String _arrayImage;
    private Bitmap _bitmap;

    private AsyncCollageCreator _collageCreator;
    private LogoutListner _logoutListner;


    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        String str=null;
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(IMAGES_TAG))
                str=savedInstanceState.getString(IMAGES_TAG);
        } else {
            Bundle args = getArguments();
            if(args.containsKey(IMAGES_TAG))
                str=args.getString(IMAGES_TAG);
        }
        _arrayImage = str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_collage, container, false);
        _processDialog = (FrameLayout) rootView.findViewById(R.id.create_collage_processing);
        _messageView = (FrameLayout) rootView.findViewById(R.id.notcreate_collage_label);
        _collageContainer = (LinearLayout) rootView.findViewById(R.id.collage_containder);
        _imageView = (ImageView) rootView.findViewById(R.id.collage_image);
        _emailView = (EditText) rootView.findViewById(R.id.edit_email);

        _emailView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    hideKeyboard();
                    sendCollage();
                    handled = true;
                }
                return handled;
            }
        });
        Button button = (Button) rootView.findViewById(R.id.button_send_collage);
        button.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        _messageView.setVisibility(View.GONE);
        _collageContainer.setVisibility(View.GONE);
        _processDialog.setVisibility(View.VISIBLE);
        if(_collageCreator !=null)
            _collageCreator.cancel(true);
        _collageCreator = new AsyncCollageCreator();
        _collageCreator.execute();

    }

    @Override
    public void onPause(){
        super.onPause();
        if(_collageCreator !=null)
            _collageCreator.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(IMAGES_TAG,_arrayImage);
    }

    public void setLogoutListner(LogoutListner listner){
        _logoutListner=listner;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.getcollage_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.instagram_logout:
                if(_logoutListner!=null) _logoutListner.onLogout();
                else {
                    getActivity().moveTaskToBack(true);
                    getActivity().finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send_collage:
                hideKeyboard();
                sendCollage();
                break;
        }
    }

    protected void sendCollage(){
        String email = _emailView.getText().toString();
        File file=null;

        if(email.isEmpty()){
            Toast.makeText(getActivity(), "E-mail is empty!!!", Toast.LENGTH_SHORT).show();
        } else {
            file = saveCollage();
            if(file==null)
                Toast.makeText(getActivity(), "Error!!!", Toast.LENGTH_SHORT).show();
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Collage");
            intent.putExtra(Intent.EXTRA_TEXT,"InstagramCollage");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            getActivity().startActivity(Intent.createChooser(intent, "Send e-mail..."));
        }
    }

    private File saveCollage() {
        // is SD mounted
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return null;
        }
        // get SD path
        String sdPath = Environment.getExternalStorageDirectory().toString();
        File file = new File(sdPath,getString(R.string.collage_name));
        FileOutputStream outputStream=null;
        try {
            outputStream = new FileOutputStream(file);
            _bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }

        return file;
    }

    protected void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_emailView.getWindowToken(),0);
    }

    public class AsyncCollageCreator extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            CollageCreator creator = new CollageCreator(_arrayImage);
            creator.createCollage();
            if(creator.isCreatedCollage())
                _bitmap = creator.getCollage();
            else
            _bitmap = null;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(!isCancelled()){
                _processDialog.setVisibility(View.GONE);
                if(_bitmap!=null){
                    _collageContainer.setVisibility(View.VISIBLE);
                    _imageView.setImageBitmap(_bitmap);
                } else {
                    _messageView.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _processDialog.setVisibility(View.VISIBLE);
        }
    }
}
