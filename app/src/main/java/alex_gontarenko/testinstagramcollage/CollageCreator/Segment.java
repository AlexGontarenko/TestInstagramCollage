package alex_gontarenko.testinstagramcollage.CollageCreator;

/**
 * Created by Alex on 25.06.2014.
 */
public class Segment {
    private int _xCoord,_yCoord,_width,_height;

    public Segment(){
        _xCoord=0;  _yCoord=0;
        _width=0;   _height=0;
    }

    public Segment(int x,int y, int w, int h){
        _xCoord=x;  _yCoord=y;
        _width=w;   _height=h;
    }

    public int getXCoord(){return _xCoord;}

    public void setXCoord(int x){_xCoord = x;}

    public int getYCoord(){return _yCoord;}

    public void setYCoord(int y){_yCoord = y;}

    public int getWidth(){return _width;}

    public void setWidth(int w){_width=w;}

    public int getHeight(){return _height;}

    public void setHeight(int h){_height=h;}
}
