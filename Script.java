import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Script {
	private ArrayList<Track> tracks;
	private String name;
	private String path;

	public Script(String scriptName,String thePath){
		name=scriptName;
		path=thePath;
		tracks=new ArrayList<Track>();
	}
	public String getPath() {
		return path;
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
	//private OffsetTree myTree;
	public void addTrack(Track theNewTrack) throws Exception{
		if(theNewTrack.getRelativeTo()==theNewTrack){
			if(tracks.size()==0){
				tracks.add(theNewTrack);
				//myTree=new OffsetTree(theNewTrack);
			}else{
				throw new Exception();
			}
		}else if(theNewTrack.getRelativeTo()!=null){
			int index1=tracks.indexOf(theNewTrack.getRelativeTo());
			int index2=tracks.size();
			for(int i=index1;i<tracks.size();i++){
				if((tracks.get(i).getRelativeTo()!=theNewTrack.getRelativeTo())||(i==tracks.size()-1)){
					index2=i;
				}
			}
			tracks.add(index2,theNewTrack);
			/*if(theNewTrack.getRelativeTo()==tracks.get(0)){
                myTree.addStartChildren(new TrackNode(theNewTrack));
            }else{
                myTree.findMyDaddy(theNewTrack.getRelativeTo(), theNewTrack);
            }*/
		}else{
			throw new Exception();
		}
	}
	public int getDuration(){
		int maxLength=0;
		for(Track t:tracks){
			if((t.startTime()+t.getLength())>maxLength){
				maxLength=(t.startTime()+t.getLength());
			}
		}
		return maxLength;
	}
	private void saveBytesAsWav(byte[] bytes, String path) throws Exception{
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream);
		AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(path));
	}
	private byte[] combineBytes(byte[] a, byte[] b, int aSecs, int bSecs, int offset) {
		int bytesPerSecond=a.length/aSecs;
		int bytesAfter=bytesPerSecond*offset;
		ArrayList<Byte> bytes=new ArrayList<Byte>();
		for (int i=0; i<bytesAfter; i++) {
			bytes.add(a[i]);
		}
		int counter=0;
		for (int i=bytesAfter; i<a.length; i++) {
			bytes.add((byte) ((a[i] + b[counter]) >> 1));
			counter++;
		}
		for (int i=counter; i<b.length; i++) {
			bytes.add(b[i]);
		}
		byte[] out = new byte[bytes.size()];
		for(int i=0; i<bytes.size(); i++) {
			out[i]=bytes.get(i);
		}
		return out;
	}
	public void saveScript(){ WriteXML xml=new WriteXML(tracks, path); } //<--XML one
	
	private byte[] listToBytes(ArrayList<Byte> b) {
		byte[] getThis=new byte[b.size()];
		for (int i=0; i<getThis.length; i++) {
			getThis[i]=b.get(i);
		}
		return getThis;
	}
	
	private ArrayList<Byte> bytesToList(byte[] b) {
		ArrayList<Byte> newList=new ArrayList<Byte>();
		for (int i=0; i<b.length; i++) {
			newList.add(b[i]);
		}
		return newList;
	}

	public byte[] finalBytes() {
		ArrayList<Byte> finalBytes=new ArrayList<Byte>();

		byte[] byte1=tracks.get(0).getBytes();
		for (int i=0; i<byte1.length; i++) {
			finalBytes.add(byte1[i]);
		}

		for (int i=1; i<tracks.size(); i++) {

			byte[] newBytes=(combineBytes(listToBytes(finalBytes), tracks.get(i).getBytes(), 
				tracks.get(i-1).getDurationSeconds(), tracks.get(i).getDurationSeconds(), 
				tracks.get(i).getSecondsOffset()));

			finalBytes=bytesToList(newBytes);

		}
		
		return listToBytes(finalBytes);
	}

	public void saveScriptAsWav(String path){

	
	} //<--write out as WAV one
	//some tests IGNORE THESE
	/*public OffsetTree getMyTree(){return myTree;}
public static void main(String[] args){
Script myScript=new Script("Begumbar", "Z:\\AOOD\\Begumbar.xml");
Track newTrack=new Track("Begu", myScript, "src/(100) Daft Punk - Lose Yourself to Dance");
Track dagomba=new Track("Dagomba",newTrack,myScript, "src/(128) Daft Punk - One More Time");
Track jarmungar=new Track("Jarmungar",newTrack,myScript, "src/(128) Daft Punk - One More Time");
Track stopIt=new Track("stopIt",dagomba,myScript, "src/(128) Daft Punk - One More Time");
try {
myScript.addTrack(newTrack);
myScript.addTrack(dagomba);
myScript.addTrack(jarmungar);
myScript.addTrack(stopIt);
} catch (Exception e) {e.printStackTrace();}
myScript.getMyTree().printChildren(myScript.getMyTree().getRootNode());
}*/
	public static void main(String[] args) {
		Track newTrack=new Track("Name", null, null,"src/(100) Daft Punk - Lose Yourself to Dance.wav", true, 100);
		//Path path1 = Paths.get("src/(100) Daft Punk - Lose Yourself to Dance.wav");
		Path path2 = Paths.get("Z:\\AOOD\\(128) Cee Lo Green - Forget You.wav");
		try {
			byte[] blue = Files.readAllBytes(path2);
			int bytesPerSecond=blue.length/newTrack.getDurationSeconds();
			System.out.println(bytesPerSecond);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// uncomment for testing
	/*public static void main(String[] args) {

    	Track newTrack=new Track("Name", null, null,"src/(100) Daft Punk - Lose Yourself to Dance.wav", true, 100);

    	//Path path1 = Paths.get("src/(100) Daft Punk - Lose Yourself to Dance.wav");
		Path path2 = Paths.get("Z:\\AOOD\\(128) Cee Lo Green - Forget You.wav");

		try {
			byte[] blue = Files.readAllBytes(path2);

	    	int bytesPerSecond=blue.length/52;
	    	System.out.println(bytesPerSecond);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


}

//duration pls
