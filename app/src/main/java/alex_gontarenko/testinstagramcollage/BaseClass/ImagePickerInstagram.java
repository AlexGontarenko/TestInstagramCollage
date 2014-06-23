package alex_gontarenko.testinstagramcollage.BaseClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import alex_gontarenko.testinstagramcollage.Loaders.ImageLoader;
import alex_gontarenko.testinstagramcollage.R;


public class ImagePickerInstagram extends FrameLayout implements View.OnClickListener{


    private FrameLayout _layout;
    private ImageView _image;
    private OnPickImage _listner;

    private String _imgUrl;

    private boolean _isPick=false;

    public interface OnPickImage{
        public abstract void onPickImage(boolean pick);
    }

    public ImagePickerInstagram(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_picker_instagram, this, true);
        _layout = (FrameLayout)  view.findViewById(R.id.root_picker);
        _layout.setOnClickListener(this);
        _image = (ImageView) view.findViewById(R.id.image_peacker);
    }

    public ImagePickerInstagram(Context context) {
        super(context);
    }

    public ImagePickerInstagram(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnPickImage(OnPickImage listner){
        _listner=listner;
    }

    public void setPick(boolean pick){
        _isPick=pick;
        pickerBackground();
    }

    public void setImageBitmap(Bitmap image){
        _image.setImageBitmap(image);
    }

    public ImageView getImageView(){
        return _image;
    }

    protected void pickerBackground(){
        if(_isPick)
            _layout.setBackgroundColor(Color.CYAN);
        else
            _layout.setBackgroundColor(Color.TRANSPARENT);
    }

    protected void clickPick(){
        _isPick=!_isPick;
        pickerBackground();
        if(_listner!=null) _listner.onPickImage(_isPick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root_picker:
                clickPick();
                break;
        }
    }
}
