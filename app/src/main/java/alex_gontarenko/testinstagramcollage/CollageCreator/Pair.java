package alex_gontarenko.testinstagramcollage.CollageCreator;

import alex_gontarenko.testinstagramcollage.BaseClass.InstagramImageLikes;

/**
 * Created by Alex on 25.06.2014.
 */
public class Pair {
    private Segment _segment;
    private InstagramImageLikes _img;

    public Pair(){_segment=null;_img=null;}

    public Pair(Segment segment,InstagramImageLikes img){
        _segment=segment;
        _img=img;
    }

    public Segment getSegment() {
        return _segment;
    }

    public void setSegment(Segment segment) {
        segment = segment;
    }

    public InstagramImageLikes getImg(){
        return _img;
    }

    public void setImg(InstagramImageLikes img) {
        _img = img;
    }
}
