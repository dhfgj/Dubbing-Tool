import java.util.ArrayList;
public class TrackNode{
    private Track myTrack;
    public Track getTrackNode(){return myTrack;}
    private TrackNode myParent;
    private ArrayList<TrackNode> children;
    public TrackNode(Track theNewTrack){
        myTrack=theNewTrack;
        children=new ArrayList<>();
        if(theNewTrack.getRelativeTo()!=theNewTrack){
            myParent=new TrackNode(theNewTrack.getRelativeTo());
        }else{
            myParent=this;
        }
        //gots to set up children boi
    }
    public void addChildren(TrackNode newTrack){
        children.add(newTrack);
    }
   
    public int getNumChildren(){return children.size();}
    public TrackNode getParent(){return myParent;}
    public ArrayList<TrackNode> getChildren(){return children;}
}
