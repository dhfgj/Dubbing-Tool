import java.util.ArrayList;
public class OffsetTree{
    private TrackNode root;
    private int maxTrackLength;
    private ArrayList<TrackNode> rootChildren;
    public OffsetTree(Track startTrack){
	root=new TrackNode(startTrack);
    }



    public void addStartChildren(TrackNode newTrack){
	rootChildren.add(newTrack);
    }

}
