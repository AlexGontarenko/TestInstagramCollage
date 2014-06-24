package alex_gontarenko.testinstagramcollage.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.FrameLayout;

import alex_gontarenko.testinstagramcollage.Instagram.AuthorizationWebViewClient;
import alex_gontarenko.testinstagramcollage.Instagram.AuthorizationWebViewClient.ShowLoadDialogListner;
import alex_gontarenko.testinstagramcollage.Instagram.InstagramAPI;
import alex_gontarenko.testinstagramcollage.MainActivity;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 21.06.2014.
 */
public class FragmentInstagramLogin extends Fragment implements ShowLoadDialogListner {

    private static final FrameLayout.LayoutParams FILL_LAYOUT_PARAMS = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);


    private FrameLayout _progressDialog;
    private FrameLayout _containerView;
    private WebView _webView;

    private boolean _visible;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(getActivity().getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        _progressDialog = (FrameLayout) rootView.findViewById(R.id.loading_label_login_fragment);
        _containerView = (FrameLayout) rootView.findViewById(R.id.webview_conteiner_login_fragment);
        MainActivity activity = (MainActivity) getActivity();
        _webView = new WebView(getActivity().getApplicationContext());
        _webView.setVerticalScrollBarEnabled(true);
        _webView.setHorizontalScrollBarEnabled(true);

        AuthorizationWebViewClient client = new AuthorizationWebViewClient();
        client.setAuthorizationListner(activity);
        client.setShowLoadDialogListner(this);
        _webView.setWebViewClient(client);
        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setSavePassword(false);
        _webView.setLayoutParams(FILL_LAYOUT_PARAMS);
        _containerView.addView(_webView);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        _webView.loadUrl(InstagramAPI.getURLInstagramAuth());
    }

    @Override
    public void onResume() {
        super.onResume();
        _visible=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        _visible=false;
    }


    @Override
    public void showDialog() {
        if(_visible)
        _progressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        if(_visible)
        _progressDialog.setVisibility(View.GONE);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.instagram_login_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.instagram_exit:
                getActivity().moveTaskToBack(true);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}