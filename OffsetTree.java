
public class OffsetTree{
    private Track root;
    private int maxTrackLength;
    private ArrayList<TrackNode> rootChildren;
    public OffsetTree(Track startTrack){
	root=startTrack;
    }



    public void addStartChildren(TrackNode newTrack){
	rootChildren.add(newTrack);
    }

    public static class TrackNode{
	private Track myTrack;
	public Track getTrackNode(){return myTrack;}

	private TrackNode myParent;
	private ArrayList<TrackNode> children;

	public TrackNode(Track theNewTrack){
	    myTrack=theNewTrack;
	    myParent=theNewTrack.getRelativeTo();

	    //gots to set up children boi
	}

	public void addChildren(TrackNode newTrack){
	    children.add(newTrack);
	}

    }
}
