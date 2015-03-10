import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Track {

	private String trackName;
	private String soundFile;
	private File file;
	private Clip clip;
	private AudioInputStream stream;
	private Track relativeTo;
	private int intensity;
	private Script myScript;
	private boolean startOrEnd;
	private int trackLength;

	private int secondsOffset;


	public int getSecondsOffset() {
		return secondsOffset;
	}

	public void setSecondsOffset(int newSeconds) {
		secondsOffset=newSeconds;
	}

	private int getDurationSeconds() {
		try {
			return (int) (getDurationMilliseconds()/1000);
		} catch(Exception e) {
			
		}
		
		return 0;
	}

	private int getDurationMilliseconds() {
		try{	AudioInputStream input=AudioSystem.getAudioInputStream(file);
		AudioFormat format=input.getFormat();
		long fileLength=file.length();
		int frames=format.getFrameSize();
		float rate=format.getFrameRate();
		float seconds=(fileLength/(frames * rate));
		double milliseconds=seconds * 1000;
		input.close();
		return (int) milliseconds;}catch(Exception e){}
		return 0;
	}
	public byte[] getBytes(int milliseconds) {
		try{
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			BufferedInputStream input=new BufferedInputStream(new FileInputStream(soundFile));
			int reader;
			byte[] buffer= new byte[(int)file.length()];

			while ((reader=input.read(buffer))>0) {
				out.write(buffer, 0, reader);
			}

			out.flush();

			byte[] allBytes=out.toByteArray();
			int totalMillis=getDurationMilliseconds();
			out=null;
			input.close();


			if (totalMillis>milliseconds) {
				int sampleSize=(int) totalMillis/milliseconds; 
				byte[] sampleBytes=new byte[sampleSize];
				for (int i=0; i<sampleSize; i++) {
					sampleBytes[i]=allBytes[i * sampleSize];
				}
				return sampleBytes;
			} else {
				return allBytes;
			}
		}catch(Exception e){}
		return new byte[1];
	}

	public Track(String myName, Track relative, Script host,String newPath, boolean beginning,int newIntensity) {
		trackName=myName;
		relativeTo=relative;
		startOrEnd=beginning;
		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}
	public Track(String myName, Track relative, Script host,String newPath) {
		trackName=myName;
		relativeTo=relative;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}
	public Track(String myName, Track relative, Script host,String newPath,boolean beginning) {
		trackName=myName;

		relativeTo=relative;
		startOrEnd=beginning;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}
	public Track(String myName, Track relative, Script host,String newPath,int newIntensity) {
		trackName=myName;

		relativeTo=relative;

		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}


	public Track(String myName, Script host, int newIntensity, String newPath, boolean beginning) {
		trackName=myName;
		relativeTo=this;
		startOrEnd=beginning;
		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}
	public Track(String myName, Script host,String newPath) {
		trackName=myName;
		relativeTo=this;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}
	public Track(String myName, Script host,String newPath,boolean beginning) {
		trackName=myName;

		relativeTo=this;
		startOrEnd=beginning;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}
	public Track(String myName, Script host,String newPath, int newIntensity) {
		trackName=myName;

		relativeTo=this;

		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=0;
	}

	public Track(String myName, Track relative, Script host,String newPath, boolean beginning,int newIntensity, int newSeconds) {
		trackName=myName;
		relativeTo=relative;
		startOrEnd=beginning;
		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	public Track(String myName, Track relative, Script host, int newSeconds, String newPath) {
		trackName=myName;
		relativeTo=relative;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	public Track(String myName, Track relative, Script host,String newPath, int newSeconds, boolean beginning) {
		trackName=myName;

		relativeTo=relative;
		startOrEnd=beginning;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	public Track(String myName, Track relative, Script host,String newPath,int newIntensity, int newSeconds) {
		trackName=myName;

		relativeTo=relative;

		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}


	public Track(String myName, Script host,String newPath, boolean beginning,int newIntensity, int newSeconds) {
		trackName=myName;
		relativeTo=this;
		startOrEnd=beginning;
		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	public Track(String myName, Script host, int newSeconds, String newPath) {
		trackName=myName;
		relativeTo=this;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	public Track(String myName, Script host,String newPath,boolean beginning, int newSeconds) {
		trackName=myName;

		relativeTo=this;
		startOrEnd=beginning;

		intensity=100;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	public Track(String myName, Script host,String newPath,int newIntensity, int newSeconds) {
		trackName=myName;

		relativeTo=this;

		intensity=newIntensity;
		myScript=host;
		soundFile=newPath;
		file=new File(soundFile);
		startOrEnd=true;
		trackLength=getDurationMilliseconds()/1000;
		secondsOffset=newSeconds;
	}
	
	
	
	
	
	
	
	
	
	

	public boolean getStart() {
		return startOrEnd;
	}
	public void setStart(boolean newStart){
		startOrEnd=newStart;
	}
	public void changeIntensity(int newIntensity){intensity=newIntensity;}
	public Track getRelativeTo(){return relativeTo;}
	public void setRelativeTo(Track newRelative){relativeTo=newRelative;}
	public int getLength(){return trackLength;}
	public int getIntensity(){return intensity;}
	public void setIntensity(int newIntensity){intensity=newIntensity;}
	public String getTrackName(){return trackName;}
	public Script getScript(){return myScript;}
	public String getPath(){return soundFile;}
	public BufferedImage generateGraphics(){
		byte[] myBytes=getBytes(200);
		Coordinate[] theseCoords=new Coordinate[myBytes.length];
		for(int i=0;i<myBytes.length;i++){
			theseCoords[i]=new Coordinate(i, myBytes[i]);
		}

		double rangeNum=(double)range(myBytes);
		double smallNum=(double)minNum(myBytes);
		XYGrapher theGraph=new XYGrapher(smallNum,rangeNum,myBytes.length,theseCoords);

		return theGraph.drawGraph(0,0,myBytes.length,100);
	}
	
	public int startTime() {
		Track relative=this;
		
		int millis=-relative.getDurationMilliseconds();
		
		while (relative!=null) {
			millis=millis + relative.getDurationMilliseconds();
			relative=this.getRelativeTo();
		}
		
		return (int) millis/1000;
	}
	
	public int endTime() {
		Track relative=this;
		
		int millis=0;
		
		while (relative!=null) {
			millis=millis + relative.getDurationMilliseconds();
			relative=this.getRelativeTo();
		}
		
		return (int) millis/1000;
	}

	private static int range(byte[] theBytes){
		int max=theBytes[0];
		int min=theBytes[0];
		for(int i=0;i<theBytes.length;i++){
			if(theBytes[i]>max){
				max=theBytes[i];
			}else if(theBytes[i]<min){
				min=theBytes[i];
			}
		}
		return (max-min)+1;
	}
	private static int minNum(byte[] theBytes){
		int min=theBytes[0];
		for(int i=0;i<theBytes.length;i++){
			if(theBytes[i]<min){
				min=theBytes[i];
			}
		}
		return min;
	}
	//	public void playTrack(){}
	//	public Clip getPlayableClip(){}
	
/*	public static void main(String[] args) {
    	Track baladev=new Track("...", null, "src/(100) Daft Punk - Lose Yourself to Dance.wav");
    	JFrame bello=new JFrame();
    	
    	bello.setSize(500,500);
    	//JLabel jLabel = new JLabel(new ImageIcon(baladev.generateGraphics()));
    	JLabel jLabel = new JLabel(new ImageIcon(baladev.generateGraphics()));
        JPanel jPanel = new JPanel();
        jPanel.add(jLabel);
        bello.add(jPanel);
        bello.setVisible(true);
    	
    }*/

}



    
