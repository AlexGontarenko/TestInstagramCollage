package alex_gontarenko.testinstagramcollage.CollageCreator;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import alex_gontarenko.testinstagramcollage.BaseClass.ImageListConverter;
import alex_gontarenko.testinstagramcollage.BaseClass.InstagramImageLikes;

/**
 * Created by Alex on 24.06.2014.
 */
public class CollageCreator {

    private static final String LOG_TAG = "CollageCreator";

    private String _arrayImage;
    private Bitmap _bitmap;
    private boolean _isCreated;


    public CollageCreator(String arrayImage){
        _arrayImage= arrayImage;
        _bitmap=null;
    }

    public boolean isCreatedCollage(){
        return _isCreated;
    }

    public Bitmap getCollage(){
        return _bitmap;
    }

    protected int calcParam(double scale, int param,int likes, int maxLikes){
        return (int)Math.ceil(scale * param * (likes + maxLikes) / (maxLikes + maxLikes));
    }

    public void createCollage(){
        int maxLikes, likes,i,size, maxWidth, maxHeight;
        double scaleKoef,width, height,square;
        InstagramImageLikes img;

        //for algoritm creator
        ArrayList<VerticalLine> verticalLines;
        ArrayList<Pair> pairList, resultPair;
        final ArrayList<InstagramImageLikes> rects;
        ArrayList<Segment> segmentList;
        boolean success,notadd;
        VerticalLine vl,newVl;
        Segment sl,newSl;
        Pair pair;
        int j,k,l;
        int x,y,w,h,imgH,imgW;

        //init data
        if(_arrayImage==null||_arrayImage.isEmpty()) return;

        rects = ImageListConverter.convertStringToArray(_arrayImage);
        if(rects==null||rects.isEmpty()) return;
        size = rects.size();

        scaleKoef = 1./(1.+Math.log(size));
        //find max likes
        maxLikes=rects.get(0).getLikes();
        for(i=1;i<size;i++){
            likes=rects.get(i).getLikes();
            if(maxLikes<likes) maxLikes = likes;
        }
        //calculate square, find maxWidth and maxHeight
        square = 0.;maxWidth=0;maxHeight=0;
        for(i=0;i<size;i++){
            img = rects.get(i);
            width = calcParam(scaleKoef,img.getWidth(),img.getLikes(),maxLikes);
            height = calcParam(scaleKoef,img.getHeight(),img.getLikes(),maxLikes);
            square+=width*height;
            if(maxHeight<height) maxHeight=(int)height;
            if(maxWidth<width) maxWidth=(int)width;
        }
        square = Math.sqrt(1.3*square);

        maxHeight = (int) Math.max(square,maxHeight);
        maxWidth = (int) Math.max(square,maxWidth);

        if(maxHeight>720) {
            scaleKoef*=720./maxHeight;
            maxWidth=(int)Math.ceil(maxWidth*720./maxHeight);
            maxHeight= 720;
        }
        if(maxWidth>720) {
            scaleKoef *= 720. / maxWidth;
            maxHeight = (int) Math.ceil(maxHeight*720. / maxWidth);
            maxWidth = 720;
        }

        //init to packing
        sl=new Segment(0,0,maxWidth,maxHeight);
        vl = new VerticalLine(sl);
        verticalLines = new ArrayList<VerticalLine>(1);
        verticalLines.add(vl);
        resultPair = new ArrayList<Pair>(size);
        final double scale = scaleKoef;
        final int max = maxLikes;
        Collections.sort(rects,new Comparator<InstagramImageLikes>() {
            @Override
            public int compare(InstagramImageLikes l, InstagramImageLikes r) {
                int result = calcParam(scale,r.getHeight(),r.getLikes(),max)-calcParam(scale,l.getHeight(),l.getLikes(),max);
                if(result==0)
                    result = calcParam(scale, r.getWidth(), r.getLikes(), max)-calcParam(scale, l.getWidth(), l.getLikes(), max);
                return result;
            }
        });
        //packing image
        while (!verticalLines.isEmpty()&&!rects.isEmpty()){
            success=false;
            notadd=true;

            for (j=0;j<verticalLines.size();j++){
                vl=verticalLines.get(j);
                pairList = new ArrayList<Pair>();
                segmentList = vl.getListSegment();
                //find rects
                for(k=0;k<segmentList.size();k++){
                    sl = segmentList.get(k);
                    for(l=0;l<rects.size();l++){
                        img = rects.get(l);
                        h=calcParam(scaleKoef,img.getHeight(),img.getLikes(),maxLikes);
                        //w=calcParam(scaleKoef,img.getWidth(),img.getLikes(),maxLikes);
                        if(sl.getHeight()>=h){//&&sl.getWidth()>=w
                            pairList.add(new Pair(sl,img));
                            break;
                        }
                    }
                }


                if(pairList.isEmpty()){
                    pairList=null;continue;
                }
                //sort list

                Collections.sort(pairList,new Comparator<Pair>() {
                    @Override
                    public int compare(Pair l, Pair r) {
                        int rP,lP,result;
                        InstagramImageLikes img;
                        img = r.getImg();
                        rP = calcParam(scale, img.getWidth(), img.getLikes(), max);
                        img = l.getImg();
                        lP = calcParam(scale, img.getWidth(), img.getLikes(), max);
                        result = (rP/r.getSegment().getWidth()-lP/l.getSegment().getWidth());
                        return result;
                    }
                });
                //create new VerticalLines ans Segments
                for(k=0;k<pairList.size();k++) {
                    pair = pairList.get(k);
                    img = pair.getImg();
                    sl = pair.getSegment();

                    imgH = calcParam(scaleKoef, img.getHeight(), img.getLikes(), maxLikes);
                    imgW = calcParam(scaleKoef, img.getWidth(), img.getLikes(), maxLikes);

                    if (imgW <= sl.getHeight()) {
                        resultPair.add(pair);
                        rects.remove(img);
                        vl.getListSegment().remove(sl);
                        if (imgH < sl.getHeight()) {
                            x = sl.getXCoord();
                            y = sl.getYCoord() + imgH;
                            w = imgW;
                            h = sl.getHeight() - y;
                            newSl = new Segment(x, y, w, h);
                            vl.addSegment(newSl);
                        }

                        if (imgW < sl.getWidth()) {
                            x = sl.getXCoord() + imgW;
                            y = sl.getYCoord();
                            w = sl.getWidth() - x;
                            h = sl.getHeight();
                            newSl = new Segment(x, y, w, h);
                            newVl = new VerticalLine(newSl);
                            verticalLines.add(newVl);
                        }
                        if (vl.getListSegment().isEmpty())
                            verticalLines.remove(vl);

                        success = true;
                        notadd = false;
                        break;
                    }
                }
                pairList=null;

                if(success) break;
            }

            if(notadd) break;
        }

        //create bitmap

    }
}
