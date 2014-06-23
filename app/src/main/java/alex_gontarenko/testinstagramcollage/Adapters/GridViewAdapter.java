package alex_gontarenko.testinstagramcollage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import alex_gontarenko.testinstagramcollage.BaseClass.ImagePick;
import alex_gontarenko.testinstagramcollage.BaseClass.ImagePickerInstagram;
import alex_gontarenko.testinstagramcollage.BaseClass.InstagramMediaImage;
import alex_gontarenko.testinstagramcollage.Loaders.ImageLoader;
import alex_gontarenko.testinstagramcollage.R;

/**
 * Created by Alex on 22.06.2014.
 */
public class GridViewAdapter extends BaseAdapter{
    private static final String TAG_ID = "IMAGE_PICKER_ID";

    private LayoutInflater _inflater;

    private ArrayList<ImagePick> _array;

    public GridViewAdapter(Context context) {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        if(_array !=null&&!_array.isEmpty())
            return _array.size();
        return 0;
    }

    public ArrayList<ImagePick> getArray(){
        return _array;
    }

    public void setArray(ArrayList<InstagramMediaImage> array){
        if(_array!=null)
            _array.clear();
        int i,size;
        if(array!=null&&!array.isEmpty()) {
            size = array.size();
            _array = new ArrayList<ImagePick>(array.size());
            for (i=0;i<size;i++)
                _array.add(new ImagePick(array.get(i)));
        }
        notifyDataSetChanged();
    }

    public void clear(){
        if(_array!=null)
            _array.clear();
        notifyDataSetChanged();
    }


    @Override
    public Object getItem(int position) {
        return _array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImagePickerInstagram image = (ImagePickerInstagram) _inflater.inflate(R.layout.item_image_gredview, parent,false);

        ImageLoader.instance().load(_array.get(position).getImgURLThumbnail(),image.getImageView());
        image.setPick(_array.get(position).isPick());
        image.setOnPickImage(new ImagePickerInstagram.OnPickImage() {
            @Override
            public void onPickImage(boolean pick) {
                final int pos = position;
                _array.get(pos).setPick(pick);
            }
        });
        return image;
    }
}
