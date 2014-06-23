package alex_gontarenko.testinstagramcollage.BaseClass;

/**
 * Created by Alex on 22.06.2014.
 */
public class InstagramMediaImage {

    private InstagramImage _imgThumbnail,_img;
    private int _likes;

    public InstagramMediaImage(InstagramImage img, InstagramImage imgThumbnail, int likes){
        _img = img;
        _imgThumbnail =imgThumbnail;
        _likes=likes;
    }

    public void setInstagramImage(InstagramImage img){
        _img = img;
    }

    public InstagramImage getInstagramImage(){
        return _img;
    }

    public void setInstagramImageThumbnail(InstagramImage imgThumbnail){
        _imgThumbnail = imgThumbnail;
    }

    public InstagramImage getInstagramImageThumbnail(){
        return _imgThumbnail;
    }

    public int getLikes(){
        return _likes;
    }

    public void setLikes(int likes){
        _likes=likes;
    }

    public String getImgURLThumbnail(){
        String url=(_imgThumbnail!=null?_imgThumbnail.getURL():null);
        return url;
    }

    public String getImgURL(){
        String url=(_img!=null?_img.getURL():null);
        return url;
    }

    public int getWidthImgThumbnail(){
        int width=(_imgThumbnail!=null?_imgThumbnail.getWidth():-1);
        return width;
    }

    public int getHeightImgThumbnail(){
        int height=(_imgThumbnail!=null?_imgThumbnail.getHeight():-1);
        return height;
    }

    public int getWidthImg(){
        int width=(_img!=null?_img.getWidth():-1);
        return width;
    }

    public int getHeightImg(){
        int height=(_img!=null?_img.getHeight():-1);
        return height;
    }



}
