import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


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
    private int getDurationMilliseconds() {
	AudioInputStream input=AudioSystem.getAudioInputStream(file);
	AudioFormat format=input.getFormat();
	long fileLength=file.length();
	int frames=format.getFrameSize();
	float rate=format.getFrameRate();
	float seconds=(fileLength/(frames * rate));
	double milliseconds=seconds * 1000;
	input.close();
	return (int) milliseconds;
    }
    public byte[] getBytes(int milliseconds) {
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
    }

    public Track(String myName, Track relative, Script host,String newPath, boolean beginning,int newIntensity) {
	trackName=myName;
	if(relative=null){
	    relativeTo=this;
	}else{
	    relativeTo=relative;
	}
	startTime= start;
	endTime= end;
	intensity=newIntensity;
	myScript=host;
	soundFile=newPath;
	file=new File(soundFile);
	startOrEnd=beginning;
	trackLength=getDurationMilliseconds()/1000;
    }
    public Track(String myName, Track relative, Script host,String newPath) {
	trackName=myName;
	if(relative=null){
	    relativeTo=this;
	}else{
	    relativeTo=relative;
	}
	startTime= start;
	endTime= end;
	intensity=100;
	myScript=host;
	soundFile=newPath;
	file=new File(soundFile);
	startOrEnd=true;
	trackLength=getDurationMilliseconds()/1000;
    }
    public boolean getStart() {
	return startOrEnd;
    }

    public void changeIntensity(int newIntensity){intensity=newIntensity;}
    public Track getRelativeTo(){return relativeTo;}
    public int getLength(){return trackLength;}
    public int getIntensity(){return intensity;}
    public void setIntensity(int newIntensity){intensity=newIntensity;}
    public String getTrackName(){return trackName;}
    public Script getScript(){return myScript;}
    public void setStartTime(Time newTime){startTime=newTime;}
    public void setEndTime(Time newTime){endTime=newTime;}
    public String getPath(){return soundFile;}
    public BufferedImage generateGraphics(){
	byte[] myBytes=getBytes(200);
	Coordinate[] theseCoords=new Coordinate[myBytes.length];
	for(int i=0;i<myBytes.length;i++){
	    theseCoords[i]=new Coordinate(i, myBytes[i]);
	}
	double rangeNum=(double)range(myBytes);
	



	XYGrapher theGraph=new XYGrapher(rangeNum,theseCoords);

	return theGraph.drawGraph(0,0,(int)rangeNum,100);
    }

    public static int range(byte[] theBytes){
	int max=theBytes[0];
	int min=theBytes[0];
	for(int i=0;i<theBytes.length;i++){
	    if(theBytes[i]>max){
		max=theBytes[i];
	    }else if(i<min){
		min=theBytes[i];
	    }
	}
	return (max-min)+1;
    }
    //	public void playTrack(){}
    //	public Clip getPlayableClip(){}

}
