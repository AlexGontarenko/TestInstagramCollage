package alex_gontarenko.testinstagramcollage.BaseClass;

/**
 * Created by Alex on 24.06.2014.
 */
public class InstagramImageLikes extends InstagramImage {

    private int _likes;

    public InstagramImageLikes(){
        super();
        _likes=0;
    }

    public InstagramImageLikes(String url,int width,int height){
        super(url,width,height);
        _likes=0;

    }

    public InstagramImageLikes(String url,int width,int height,int likes){
        super(url,width,height);
        _likes=likes;

    }

    public void setLikes(int likes){
        _likes = likes;
    }

    public int getLikes(){
        return _likes;
    }
}
