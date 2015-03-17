import java.util.ArrayList;
public class TrackNode{
    private Track myTrack;
    public Track getTrackNode(){return myTrack;}

    private TrackNode myParent;
    private ArrayList<TrackNode> children;

    public TrackNode(Track theNewTrack){
	myTrack=theNewTrack;
	if((theNewTrack!=theNewTrack.getRelativeTo())||(theNewTrack.getRelativeTo()!=null)){
		myParent=new TrackNode(theNewTrack.getRelativeTo());
	}
	
	//gots to set up children boi
    }

    public void addChildren(TrackNode newTrack){
	children.add(newTrack);
    }

}
