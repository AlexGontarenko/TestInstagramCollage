package alex_gontarenko.testinstagramcollage.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import alex_gontarenko.testinstagramcollage.Instagram.InstagramAPI;
import alex_gontarenko.testinstagramcollage.Listners.LogoutListner;
import alex_gontarenko.testinstagramcollage.Parcers.InstagramFindUserParcer;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 21.06.2014.
 */
public class FragmentGetCollage extends Fragment implements View.OnClickListener{

    public static final String TOKEN_TAG="FragmentGetCollage_TOKEN_TAG";

    private FrameLayout _progressDialog;
    private TextView _messageView;
    private EditText _nicknameView;

    private String _nickname,_token;

    private InstagramFindUserLoader _loader=null;
    private LogoutListner _logoutListner;
    private OnCompleteLisnter _onCompleteListner;

    public interface OnCompleteLisnter    {
        public abstract void onComplete(String token,String userId);
    }

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(TOKEN_TAG))
                _token=savedInstanceState.getString(TOKEN_TAG);
        } else {
            Bundle args = getArguments();
            if(args.containsKey(TOKEN_TAG))
                _token=args.getString(TOKEN_TAG);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_getcollage, container, false);
        _progressDialog = (FrameLayout) rootView.findViewById(R.id.loading_label_getcollage_fragment);
        _nicknameView = (EditText) rootView.findViewById(R.id.edit_nickname);

        _nicknameView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    hideKeyboard();
                    findUser();
                    handled = true;
                }
                return handled;
            }
        });
        _nicknameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard();
            }
        });
        _messageView = (TextView) rootView.findViewById(R.id.message_label);
        Button button = (Button) rootView.findViewById(R.id.button_get_collage);
        button.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(_loader!=null)
            _loader.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        _messageView.setText("");
        _progressDialog.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(TOKEN_TAG,_token);
    }

    public void setLogoutListner(LogoutListner listner){
        _logoutListner=listner;
    }
    public void setOnCompleteListner(OnCompleteLisnter listner){
        _onCompleteListner=listner;
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

    protected void findUser(){
        _nickname = _nicknameView.getText().toString();
        if(!_nickname.isEmpty()){
            if(_loader!=null)
                _loader.cancel(true);
            _loader = new InstagramFindUserLoader();
            _loader.execute(InstagramAPI.getURLFindUser(_token,_nickname));
            _messageView.setText("");
        } else {
            _messageView.setText("Input nickname!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_get_collage:
                hideKeyboard();
                findUser();
                break;
        }
    }

    protected void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_nicknameView.getWindowToken(),0);
    }

    public class InstagramFindUserLoader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection httpsURLConnection = null;
            String userId=null;
            try
            {
                URL url = new URL(params[0]);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);

                InstagramFindUserParcer parcer = new InstagramFindUserParcer(_nickname);
                userId = parcer.parceData(httpsURLConnection.getInputStream());

            } catch (Exception e) {
                Log.e(String.valueOf(R.string.tag_app), e.toString());
                userId=null;
            } finally {
                if(httpsURLConnection!=null)
                    httpsURLConnection.disconnect();
            }
            return userId;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!isCancelled()){
                _progressDialog.setVisibility(View.GONE);
                if(result!=null&&!result.isEmpty()) {
                    if(_onCompleteListner!=null)
                        _onCompleteListner.onComplete(_token,result);
                } else
                    _messageView.setText("Not found!");

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _progressDialog.setVisibility(View.VISIBLE);
        }
    }
}