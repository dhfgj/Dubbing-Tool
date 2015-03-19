import java.util.ArrayList;
public class OffsetTree{
    private TrackNode root;
    private int maxTrackLength;
    private ArrayList<TrackNode> rootChildren;
    public OffsetTree(Track startTrack){
        root=new TrackNode(startTrack);
        maxTrackLength=0;
        rootChildren=new ArrayList<TrackNode>();
    }
    public void printChildren(TrackNode t){
        System.out.println(t.getTrackNode().getTrackName());
        for(TrackNode myBabis:t.getChildren()){printChildren(myBabis);}
    }
    public void addStartChildren(TrackNode newTrack){
        rootChildren.add(newTrack);
    }

    public TrackNode getRootNode(){return root;}

    public int getMaxLength(){
        return maxTrackLength;
    }

   
    private TrackNode findMe;
    private Track myBaby;
    public void findMyDaddy(Track myDad,Track child){
        findMe=new TrackNode(myDad);
        myBaby=child;
        treeSearch(root);
    }
   
    public void treeSearch(TrackNode nextNode){
        if(nextNode.getTrackNode()==findMe.getTrackNode()){
            nextNode.addChildren(new TrackNode(myBaby));
        }else{
            for(TrackNode t:nextNode.getChildren()){
                treeSearch(t);
            }
        }
    }
   
   
    public void calculateLength(TrackNode myNode,int currentLength){
        //go down through tree
        if(myNode.getTrackNode().getRelativeTo()!=myNode.getTrackNode()){
            if(!myNode.getTrackNode().getStart()){
                currentLength+=myNode.getParent().getTrackNode().getLength();
            }
            currentLength+=myNode.getTrackNode().getSecondsOffset();
        }
        //            t.getTrackNode().getLength()
        //            t.getTrackNode().getSecondsOffset()
        //           
        //            currentTrack+=;
        //   
        if(myNode.getNumChildren()==0){
            //done with recursion
            //check if max lol
            currentLength+=myNode.getTrackNode().getLength();
            if(currentLength>maxTrackLength){
                maxTrackLength=currentLength;

            }

        }else{
            for( TrackNode t:rootChildren){

                this.calculateLength(t,currentLength);
            }
        }

    }
}
