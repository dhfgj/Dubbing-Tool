import java.util.ArrayList;


public class Script {
	private ArrayList<Track> tracks;
	private String name;
	private String path;

	public Script(String scriptName,String thePath){
		name=scriptName;
		path=thePath;
		tracks=new ArrayList<Track>();
	}
	
	public void addTrack(Track track){tracks.add(track);}
	public void deleteTrack(Track track){tracks.remove(track);}
	public void setName(String theName){name=theName;}
	public String getName(){return name;}
	public ArrayList<Track> getScriptTracks(){return tracks;}
	
	public int getDuration(){}
	public File generateSoundFile(){} 
	public void playScript(){}
	public void saveScript(){} //<--XML one
	public void updateScript(){} //<--write out as WAV one
}
