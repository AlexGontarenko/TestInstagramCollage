package alex_gontarenko.testinstagramcollage.BaseClass;

/**
 * Created by Alex on 23.06.2014.
 */
public class InstagramImage {

    private String _URL;
    private int _width,_height;

    public InstagramImage(){
        _URL=null;
        _width=-1;
        _height=-1;
    }

    public InstagramImage(String url,int width,int height){
        _URL=url;
        _width=width;
        _height=height;
    }

    public String getURL(){
        return _URL;
    }

    public void setURL(String url){
        _URL=url;
    }

    public int getWidth(){
        return _width;
    }

    public void setWidth(int width){
        _width=width;
    }

    public int getHeight(){
        return _height;
    }

    public void setHeight(int height){
        _height=height;
    }
}
