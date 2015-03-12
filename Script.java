import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

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

	public File generateSoundFile(){	
		
	}
	
	private byte[] overlapTwoTracks(byte[] a, byte[] b) {
		
		boolean aLonger=true;
		if (a.length<b.length) {
			aLonger=false;
		}
		
		byte[] out = new byte[a.length];
		
		if (aLonger) {
			try {

				for (int i=0; i<a.length; i++) {
					out[i] = (byte) ((a[i] + b[i]) >> 1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {

				for (int i=0; i<b.length; i++) {
					out[i] = (byte) ((a[i] + b[i]) >> 1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return out;
	}
	
	private void saveBytesAsWav(byte[] bytes, String path) throws Exception{
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream);
		
		AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(path));
	}


	public void saveScript(){ WriteXML xml=new WriteXML(tracks, path); } //<--XML one
	public void updateScript(){} //<--write out as WAV one

}
