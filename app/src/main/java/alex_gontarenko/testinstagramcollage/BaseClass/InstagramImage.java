package alex_gontarenko.testinstagramcollage.BaseClass;

/**
 * Created by Alex on 22.06.2014.
 */
public class InstagramImage {

    private String _imgURLThumbnail;
    private String _imgURL;
    private int _likes;

    public InstagramImage(String imgURL, String imgURLThumbnail,int likes){
        _imgURL = imgURL;
        _imgURLThumbnail =imgURLThumbnail;
        _likes=likes;
    }

    public int getLikes(){
        return _likes;
    }

    public void setLikes(int likes){
        _likes=likes;
    }

    public String getImgURLThumbnail(){
        return _imgURLThumbnail;
    }

    public void setImgURLThumbnail(String imgURL){
        _imgURLThumbnail =imgURL;
    }

    public String getImgURL(){
        return _imgURL;
    }

    public void setImgURL(String imgURL){
        _imgURL=imgURL;
    }
}
