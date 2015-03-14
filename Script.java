import java.util.ArrayList;
import java.io.File;

public class Script {
    private ArrayList<Track> tracks;
    private String name;
    private String path;
    private OffsetTree myTree;
    public Script(String scriptName,String thePath){
        name=scriptName;
        path=thePath;
        tracks=new ArrayList<Track>();
    }

    public void setName(String theName){name=theName;}
    public String getName(){return name;}
    public ArrayList<Track> getScriptTracks(){return tracks;}

    public void deleteTrack(Track track){
        if(track.getRelativeTo()==track){
            boolean done=false;
            int i;
            for(i=1;!done&&i<tracks.size();i++){
                if(tracks.get(i).getRelativeTo()!=track){
                    done=true;

                }
            }
            for(int j=1;j<i&&j<tracks.size();j++){
                tracks.get(j).setRelativeTo(tracks.get(1));
            }
            tracks.remove(track);
        }else{
            tracks.remove(track);
        }
    }

    public void addTrack(Track theNewTrack) throws Exception{
        if(theNewTrack.getRelativeTo()==theNewTrack){
            if(tracks.size()==0){
                tracks.add(theNewTrack);
		myTree=new OffsetTree(theNewTrack);
            }else{
                throw new Exception();
            }
        }else if(theNewTrack.getRelativeTo()!=null){
            int index1=tracks.indexOf(theNewTrack.getRelativeTo());
            int index2=0;
            for(int i=index1;i<tracks.size();i++){
                if(tracks.get(i).getRelativeTo()!=theNewTrack.getRelativeTo()){
                    index2=i;
                }
            }
            tracks.add(index2-1,theNewTrack);

	    if(theNewTrack.getRelativeTo()==tracks.get(0)){
		OffsetTree.addStartChildren(new TrackNode(theNewTrack));
	    }else{
		//go down tree and find your relative :)
		//tell your relative that you are its child
		
	    }
        }else{
            throw new Exception();
        }
    }

    public int getDuration(){
       
    }

    private void saveBytesAsWav(byte[] bytes, String path) throws Exception{
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream);
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(path));
    }
    public void saveScript(){ WriteXML xml=new WriteXML(tracks, path); } //<--XML one

    public void saveScriptAsWav(String path){} //<--write out as WAV one

    //needs to read in XML?
}

//duration pls
