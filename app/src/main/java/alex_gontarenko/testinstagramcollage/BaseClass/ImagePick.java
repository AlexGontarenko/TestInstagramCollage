package alex_gontarenko.testinstagramcollage.BaseClass;

/**
 * Created by Alex on 22.06.2014.
 */
public class ImagePick extends InstagramMediaImage {

    private boolean _isPick;

    public ImagePick(InstagramMediaImage image) {
        super(image.getInstagramImage(), image.getInstagramImageThumbnail(), image.getLikes());
        _isPick=false;
    }

    public boolean isPick(){
        return _isPick;
    }

    public void setPick(boolean pick){
        _isPick=pick;
    }
}
