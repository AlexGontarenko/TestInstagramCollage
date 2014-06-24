package alex_gontarenko.testinstagramcollage.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


import java.util.ArrayList;


import alex_gontarenko.testinstagramcollage.BaseClass.ImageListConverter;
import alex_gontarenko.testinstagramcollage.BaseClass.InstagramImageLikes;
import alex_gontarenko.testinstagramcollage.Listners.LogoutListner;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 24.06.2014.
 */
public class FragmentCreateCollage extends Fragment implements View.OnClickListener {

    public static final String IMAGES_TAG="FragmentCreateCollage_IMAGES_TAG";

    private FrameLayout _processDialog,_messageView;
    private LinearLayout _collageContainer;
    private ImageView _imageView;
    private EditText _emailView;

    private String _email;
    private ArrayList<InstagramImageLikes> _arrayImage;

    private InstagramFindUserLoader _collageCreator =null;
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
        _arrayImage = ImageListConverter.convertStringToArray(str);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_collage, container, false);
        _processDialog = (FrameLayout) rootView.findViewById(R.id.notcreate_collage_label);
        _messageView = (FrameLayout) rootView.findViewById(R.id.create_collage_processing);
        _collageContainer = (LinearLayout) rootView.findViewById(R.id.collage_containder);
        _emailView = (EditText) rootView.findViewById(R.id.edit_email);

        _emailView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    hideKeyboard();
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
    public void onPause(){
        super.onPause();
        if(_collageCreator !=null)
            _collageCreator.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        _messageView.setVisibility(View.GONE);
        _collageContainer.setVisibility(View.GONE);
        _processDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String str = ImageListConverter.convertArrayToString(_arrayImage);
        outState.putString(IMAGES_TAG,str);
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
                break;
        }
    }

    protected void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_emailView.getWindowToken(),0);
    }

    public class InstagramFindUserLoader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!isCancelled()){

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _processDialog.setVisibility(View.VISIBLE);
        }
    }
}
