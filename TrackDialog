import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;
import javax.sound.sampled.*;

public class TrackDialog extends JFrame implements ActionListener, ChangeListener{
	Track track;
	Track relativeTo;
	Time startTime;
	int intensity;
	Script script;
	JFrame frame; 
	ArrayList <JLabel> labels = new ArrayList <JLabel>();
	JRadioButton start, end; 
	ButtonGroup startAndEnd;
	JSlider changeIntensity; 
	ArrayList <JButton> buttons = new ArrayList <JButton>();
	Time initialStartTime;
	int initialIntensity;
	
	public TrackDialog(Track track){
		frame = new JFrame(track.getTrackName());
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setContentPane(new ContentPane());
		
		this.track = track;
		this.relativeTo = track.getRelativeTo();
		this.startTime = track.getStartTime(); 
		this.startTime = track.getStartTime(); 
		this.intensity = track.getIntensity();
		this.script = track.getScript();
		
		initialValues();
	}
	private void initialValues(){
		initialStartTime = startTime;
		initialIntensity = intensity;
	}
	class ContentPane extends JPanel{
		public ContentPane(){
			setLayout(new GridLayout(4, 2));
			drawTracks();
			drawStartOrEnd();
			drawIntensity();
			drawButtons();
		}
	}
	public void drawTracks(){
		JLabel currentTrack = new JLabel(track.getTrackName());
		labels.add(currentTrack);
		
		JLabel relativeTrack = new JLabel(relativeTo.getTrackName());
		labels.add(relativeTrack);
		
		add(currentTrack);
		add(relativeTrack);
	}
	public void drawStartOrEnd(){
		JLabel startOrEnd = new JLabel("Start/End:");
		labels.add(startOrEnd);
		
		if(track.getStartTime().toString().equals(relativeTo.getStartTime().toString())){
			start = new JRadioButton("Start", true);
			start.setActionCommand("Start");
			start.addActionListener(this);
		}
		else { 
			end = new JRadioButton("End", true);
			end.setActionCommand("End");
			end.addActionListener(this);
		}
		
		startAndEnd = new ButtonGroup();
		startAndEnd.add(start);
		startAndEnd.add(end);
		
		add(startOrEnd);
		add(start);
		add(end);
	}
	public void drawIntensity(){
		JLabel intensityLabel = new JLabel("Intensity");
		labels.add(intensityLabel);
		
		changeIntensity = new JSlider(0, 100, intensity);
		changeIntensity.setMajorTickSpacing(10);
		changeIntensity.setMinorTickSpacing(1);
		changeIntensity.setPaintTicks(true);
		changeIntensity.setPaintLabels(true);
		
		add(intensityLabel);
		add(changeIntensity);
	}
	public void drawButtons(){
		for (int i = 0; i < 3; i++){
			JButton button = null;
			if (i == 0){
				button = new JButton("Preview");
				button.setActionCommand("Preview");
			}
			if (i == 1){
				button = new JButton("Save");
				button.setActionCommand("Save");
			}
			if (i == 2){
				button = new JButton("Cancel");
				button.setActionCommand("Cancel");
			}
			buttons.add(button);
		}
		for (int i = 0; i < buttons.size(); i++){
			add(buttons.get(i));
		}
	}
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
	
		if (actionCommand.equals("Start"))
			track.setStartTime(relativeTo.getStartTime());
		else 
			if (actionCommand.equals("End"))
				track.setStartTime(relativeTo.getEndTime());
	}
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();

		if (!source.getValueIsAdjusting()) {
			intensity = (int)source.getValue();
		}
		track.setIntensity(intensity);
	}
	public void preview(){
		// use only the file name from track to generate sound clip
		try {
		    File yourFile = new File(track.getPath());
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    Clip clip;

		    stream = AudioSystem.getAudioInputStream(yourFile);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		}
		catch (Exception e){
		}
	}
	public void save(){
		// from menu?
	}
	public void cancel(){
		track.setStartTime(initialStartTime);
		track.setIntensity(initialIntensity);
		dispose();
	}
	public static void main(String args[]){
	
	}
}
