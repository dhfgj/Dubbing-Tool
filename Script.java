import java.util.ArrayList;
import java.io.File;

public class Script {
    private ArrayList<Track> tracks;
    private String name;
    private String path;

    public Script(String scriptName,String thePath){
	name=scriptName;
	path=thePath;
	tracks=new ArrayList<Track>();
    }
	
    public void setName(String theName){name=theName;}
    public String getName(){return name;}
    public ArrayList<Track> getScriptTracks(){return tracks;}

    public void addTrack(Track track){
	tracks.add(track);
	//gots to sort bois
    }
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
    public int getDuration(){}
    
    public File generateSoundFile(){} 
    public void saveScript(){} //<--XML one
    public void updateScript(){} //<--write out as WAV one

    //needs to read in XML?
}
