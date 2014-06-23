package alex_gontarenko.testinstagramcollage.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.util.ArrayList;

import alex_gontarenko.testinstagramcollage.Adapters.GridViewAdapter;
import alex_gontarenko.testinstagramcollage.BaseClass.ImageListConverter;
import alex_gontarenko.testinstagramcollage.BaseClass.InstagramMediaImage;
import alex_gontarenko.testinstagramcollage.Instagram.InstagramAPI;
import alex_gontarenko.testinstagramcollage.Listners.LogoutListner;
import alex_gontarenko.testinstagramcollage.Loaders.PopularImageLoader;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 22.06.2014.
 */
public class FragmentPopularImages extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<InstagramMediaImage>>,View.OnClickListener{

    public final static String TOKEN_TAG = "FragmentPopularImages_TOKEN_TAG";
    public final static String USER_ID_TAG = "FragmentPopularImages_USER_ID_TAG";

    private static final int POPULAR_IMAGE_LOADER_ID = 123;

    private LogoutListner _logoutListner;

    private FrameLayout _progressDialog, _messageFound;
    private LinearLayout _gridContainer;
    private GridView _gridImageView;
    private GridViewAdapter _adapter;

    private String _token,_userId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(TOKEN_TAG))
                _token=savedInstanceState.getString(TOKEN_TAG);
            if(savedInstanceState.containsKey(USER_ID_TAG))
                _userId=savedInstanceState.getString(USER_ID_TAG);
        } else {
            Bundle args = getArguments();
            if(args.containsKey(TOKEN_TAG))
                _token=args.getString(TOKEN_TAG);
            if(args.containsKey(USER_ID_TAG))
                _userId=args.getString(USER_ID_TAG);
        }
        Bundle bndl = new Bundle();
        bndl.putString(PopularImageLoader.TAG_URL, InstagramAPI.getURLMediaDataUser(_token,_userId));
        getLoaderManager().initLoader(POPULAR_IMAGE_LOADER_ID, bndl, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_image, container, false);
        _progressDialog = (FrameLayout) rootView.findViewById(R.id.loading_label_popularimage_fragment);
        _messageFound = (FrameLayout) rootView.findViewById(R.id.notdata_label_popularimage_fragment);
        _gridContainer = (LinearLayout) rootView.findViewById(R.id.grid_image_view_container);
        _gridImageView = (GridView) rootView.findViewById(R.id.grid_image_view_popularimage_fragment);
        _adapter = new GridViewAdapter(getActivity().getApplicationContext());
        _gridImageView.setAdapter(_adapter);
        Button button = (Button) rootView.findViewById(R.id.button_create_collage_popularimage_fragment);
        button.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        _adapter.clear();
        Bundle bndl = new Bundle();
        bndl.putString(PopularImageLoader.TAG_URL, InstagramAPI.getURLMediaDataUser(_token,_userId));
        Loader<ArrayList<InstagramMediaImage>> loader = getLoaderManager().restartLoader(POPULAR_IMAGE_LOADER_ID, bndl, this);
        loader.forceLoad();
        _gridContainer.setVisibility(View.GONE);
        _progressDialog.setVisibility(View.VISIBLE);
        _messageFound.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(TOKEN_TAG,_token);
        outState.putString(USER_ID_TAG,_userId);
    }

    @Override
    public Loader<ArrayList<InstagramMediaImage>> onCreateLoader(int id, Bundle args) {
        Loader<ArrayList<InstagramMediaImage>> loader = null;
        if (id == POPULAR_IMAGE_LOADER_ID) {
            args.putString(PopularImageLoader.TAG_URL,InstagramAPI.getURLMediaDataUser(_token,_userId,100));
            loader = new PopularImageLoader(getActivity().getApplicationContext(), args);
        }
        return loader;
    }



    @Override
    public void onLoaderReset(Loader<ArrayList<InstagramMediaImage>> loader) {
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<InstagramMediaImage>> loader, ArrayList<InstagramMediaImage> data) {
        if(data!=null&&data.size()>0){
            _adapter.setArray(data);
            _gridContainer.setVisibility(View.VISIBLE);
            _progressDialog.setVisibility(View.GONE);
            _messageFound.setVisibility(View.GONE);
        } else {
            _progressDialog.setVisibility(View.GONE);
            _messageFound.setVisibility(View.VISIBLE);
        }
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
        int count, countPick;
        switch (v.getId()){
            case R.id.button_create_collage_popularimage_fragment:
                count=_adapter.getCount();
                countPick=_adapter.getCountPickImage();
                if(count<11) {
                    if(count!=countPick) {
                        Toast.makeText(getActivity(), "Select all image", Toast.LENGTH_SHORT).show();
                    } else {
                        ImageListConverter.convertArrayToString(_adapter.getArray());
                    }
                } else {
                    if(countPick<10) {
                        Toast.makeText(getActivity(), "Select more image", Toast.LENGTH_SHORT).show();
                    } else {
                        ImageListConverter.convertStringToArray(ImageListConverter.convertArrayToString(_adapter.getArray()));
                    }
                }
                break;
        }
    }
}
