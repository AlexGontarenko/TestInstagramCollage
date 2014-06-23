package alex_gontarenko.testinstagramcollage.Instagram;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import alex_gontarenko.testinstagramcollage.Parcers.InstagramTokenParcer;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 21.06.2014.
 */
public class AuthorizationWebViewClient extends WebViewClient {

    private ShowLoadDialogListner _showLoadDialogListner;
    private AuthorizationListner _authorizationListner;
    private String _code;

    public interface ShowLoadDialogListner {
        public abstract void showDialog();
        public abstract void hideDialog();
    }

    public interface AuthorizationListner{
        public abstract void onComplete(String token);
        public abstract void onError(String error);
    }

    public void setShowLoadDialogListner(ShowLoadDialogListner showLoadDialogListner){
        _showLoadDialogListner=showLoadDialogListner;
    }

    public void setAuthorizationListner(AuthorizationListner authorizationListner){
        _authorizationListner=authorizationListner;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(InstagramAPI.getRedirectURI())) {
            _code = url.split("=")[1];
            new InstagramUserDataLoader().execute(InstagramAPI.getURLInstagramToken());
            if(_showLoadDialogListner!=null) _showLoadDialogListner.hideDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(_showLoadDialogListner!=null) _showLoadDialogListner.hideDialog();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(_showLoadDialogListner!=null) _showLoadDialogListner.showDialog();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if(_showLoadDialogListner!=null) _showLoadDialogListner.hideDialog();
        if(_authorizationListner!=null) _authorizationListner.onError(description);
    }

    public class InstagramUserDataLoader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String token=null;
            HttpsURLConnection httpsURLConnection = null;
            try
            {
                URL url = new URL(params[0]);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write(InstagramAPI.getPOSTCodeData(_code));

                outputStreamWriter.flush();

                InstagramTokenParcer parcer = new InstagramTokenParcer();

                token = parcer.parceData(httpsURLConnection.getInputStream());
            } catch (Exception e) {
                if(_authorizationListner!=null) _authorizationListner.onError("Failed get token");
                token=null;
                Log.e(String.valueOf(R.string.tag_app), e.toString());
            } finally {
                if(httpsURLConnection!=null)
                    httpsURLConnection.disconnect();
            }
            return token;
        }

        @Override
        protected void onPostExecute(String result) {
            if(_showLoadDialogListner!=null) _showLoadDialogListner.hideDialog();
            if(result!=null&&!result.isEmpty())
                if(_authorizationListner!=null) _authorizationListner.onComplete(result);
            else
                if(_authorizationListner!=null) _authorizationListner.onError("Haven't token");
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            if(_showLoadDialogListner!=null) _showLoadDialogListner.showDialog();
            super.onPreExecute();
        }
    }
}
