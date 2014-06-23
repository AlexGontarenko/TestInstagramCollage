package alex_gontarenko.testinstagramcollage.BaseClass;

/**
 * Created by Alex on 22.06.2014.
 */
public class InstagramImagePick extends InstagramImage {

    private boolean _isPick;

    public InstagramImagePick(String imgURL, String imgURLThumbnail, int likes) {
        super(imgURL, imgURLThumbnail, likes);
    }

    public InstagramImagePick(InstagramImage image) {
        super(image.getImgURL(), image.getImgURLThumbnail(), image.getLikes());
    }

    public boolean isPick(){
        return _isPick;
    }

    public void setPick(boolean pick){
        _isPick=pick;
    }
}
