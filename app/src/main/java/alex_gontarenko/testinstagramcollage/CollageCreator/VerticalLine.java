package alex_gontarenko.testinstagramcollage.CollageCreator;

import java.util.ArrayList;

/**
 * Created by Alex on 25.06.2014.
 */
public class VerticalLine {
    private ArrayList<Segment> _listSegment;

    public VerticalLine(){
        _listSegment = new ArrayList<Segment>();
    }

    public VerticalLine(Segment segment){
        _listSegment = new ArrayList<Segment>(1);
        _listSegment.add(segment);
    }

    public ArrayList<Segment> getListSegment() {
        return _listSegment;
    }

    public void addSegment(Segment segment){
        _listSegment.add(segment);
    }
}
