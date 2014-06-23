package alex_gontarenko.testinstagramcollage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import alex_gontarenko.testinstagramcollage.Fragments.FragmentGetCollage;
import alex_gontarenko.testinstagramcollage.Fragments.FragmentInstagramLogin;
import alex_gontarenko.testinstagramcollage.Fragments.FragmentPopularImages;
import alex_gontarenko.testinstagramcollage.Instagram.AuthorizationWebViewClient;
import alex_gontarenko.testinstagramcollage.Listners.LogoutListner;

public class MainActivity extends ActionBarActivity implements AuthorizationWebViewClient.AuthorizationListner,LogoutListner {

    private boolean _visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commitFragmentInstagramLogin();
    }

    @Override
    protected void onPause(){
        super.onPause();
        _visible=false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        _visible=true;
    }

    private void commitFragmentInstagramLogin(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentInstagramLogin fragment = (FragmentInstagramLogin) manager.findFragmentByTag("FragmentInstagramLogin");
        FragmentTransaction transaction = manager.beginTransaction();
        manager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (fragment != null) {
            transaction.replace(R.id.container_fragment, fragment,"FragmentInstagramLogin");
        } else {
            fragment = new FragmentInstagramLogin();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            transaction.replace(R.id.container_fragment, fragment,"FragmentInstagramLogin");
        }
        transaction.commit();
    }

    private void commitFragmentGetCollage(String token){
        FragmentManager manager = getSupportFragmentManager();
        FragmentGetCollage fragment = (FragmentGetCollage) manager.findFragmentByTag("FragmentGetCollage");
        FragmentTransaction transaction = manager.beginTransaction();

        if (fragment != null) {
            transaction.replace(R.id.container_fragment, fragment,"FragmentGetCollage");
        } else {
            fragment = new FragmentGetCollage();
            Bundle args = new Bundle();
            args.putString(FragmentGetCollage.TOKEN_TAG,token);
            fragment.setArguments(args);
            fragment.setLogoutListner(this);
            fragment.setOnCompleteListner(new FragmentGetCollage.OnCompleteLisnter() {
                @Override
                public void onComplete(String token, String userId) {
                    commitFragmentPopularImage(token,userId);
                }
            });
            transaction.replace(R.id.container_fragment, fragment,"FragmentGetCollage");
        }
        transaction.commit();
    }

    private void commitFragmentPopularImage(String token, String userId){
        FragmentManager manager = getSupportFragmentManager();
        FragmentPopularImages fragment = (FragmentPopularImages) manager.findFragmentByTag("FragmentPopularImage");
        FragmentTransaction transaction = manager.beginTransaction();

        if (fragment != null) {
            transaction.replace(R.id.container_fragment, fragment,"FragmentPopularImage");
        } else {
            fragment = new FragmentPopularImages();
            Bundle args = new Bundle();
            args.putString(FragmentPopularImages.TOKEN_TAG,token);
            args.putString(FragmentPopularImages.USER_ID_TAG,userId);
            fragment.setArguments(args);
            fragment.setLogoutListner(this);
            transaction.replace(R.id.container_fragment, fragment,"FragmentPopularImage");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* AuthorizationListner methods  */
    @Override
    public void onComplete(String token) {
        if(_visible&&token!=null&&!token.isEmpty())
            commitFragmentGetCollage(token);
        Toast.makeText(this,"Instagram Authorization Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogout() {
        commitFragmentInstagramLogin();
    }
}