import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

public class TrackDialog extends JFrame implements ActionListener {
    Track theTrack;
    Track initialTrack;
	JFrame frame;
	JPanel contentPane;
	GridBagConstraints c = new GridBagConstraints();
	JLabel theTrackName, relativeTrackName, startEnd, intensityLabel;
	JRadioButton start, end;
	JSlider changeIntensity;
	JButton preview, save, cancel;
	ButtonGroup startEndGroup;
	
	public TrackDialog(Track track){
    	theTrack = track;
    	initialTrack = track;
    	
    	frame = new JFrame(track.getTrackName());
    	frame.setSize(500, 500);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	contentPane = new JPanel();
    	contentPane.setLayout(new GridBagLayout());
    	
    	c.insets = new Insets(0, 20, 0, 0);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 0;
    	theTrackName = new JLabel(theTrack.getTrackName());
    	contentPane.add(theTrackName, c);
    	
    	c.insets = new Insets(0, 30, 0, 20); 
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 1;
    	c.gridy = 0;
    	relativeTrackName = new JLabel(theTrack.getRelativeTo().getTrackName());
    	contentPane.add(relativeTrackName, c);
    	
    	c.insets = new Insets(0, 5, 0, 0); 
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 1;
    	startEnd = new JLabel("Start/End:");
    	contentPane.add(startEnd, c);
    	
    	c.insets = new Insets(0, 5, 0, 0);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 1;
    	c.gridy = 1;
    	if (theTrack.getStart()){
    		start = new JRadioButton("Start", true);
    		end = new JRadioButton("End");
    	} else {
    		start = new JRadioButton("Start");
    		end = new JRadioButton("End", true);
    	}
    	start.setActionCommand("Start");
		start.addActionListener(this);
		end.setActionCommand("End");
		end.addActionListener(this);
		contentPane.add(start, c);
		contentPane.add(end, c);
		
		startEndGroup = new ButtonGroup();
		startEndGroup.add(start);
		startEndGroup.add(end);
		
		c.insets = new Insets (0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		intensityLabel = new JLabel("Intensity:");
		contentPane.add(intensityLabel, c);
		
		c.insets = new Insets(1, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		changeIntensity = new JSlider(0, 100, theTrack.getIntensity());
		changeIntensity.setMajorTickSpacing(10);
        changeIntensity.setMinorTickSpacing(1);
        changeIntensity.setPaintTicks(true);
        changeIntensity.setPaintLabels(true);
        contentPane.add(changeIntensity, c);
		
		c.insets = new Insets(0, 10, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		preview = new JButton("Preview");
		preview.setActionCommand("Preview");
		preview.addActionListener(this);
		contentPane.add(preview, c);
		
		c.insets = new Insets(0, 10, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		save = new JButton("Save");
		save.setActionCommand("Save");
		save.addActionListener(this);
		contentPane.add(save, c);
        
		c.insets = new Insets(0, 10, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.addActionListener(this);
		contentPane.add(cancel, c);
		
    	frame.setContentPane(contentPane);
    	frame.setVisible(true);

    }

	public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("Start")){
        	theTrack.setStart(true);
        } else {
        	if (actionCommand.equals("End")){
        		theTrack.setStart(false);
        	}
        	else {
        		if (actionCommand.equals("Preview")){
        			
        		}
        		else {
        			if (actionCommand.equals("Save")){
        				theTrack.getScript().saveScript();
    					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        			}
        			else {
        				if (actionCommand.equals("Cancel")){
        					theTrack = initialTrack;
        					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        				}
        			}
        		}
        	}
        }
        	
	}
	
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();

        if (!source.getValueIsAdjusting()) {
            theTrack.setIntensity((int)source.getValue());
        }
    }
	
	public void preview(){
		
	}
	
	public static void main(String[] args){

	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*Track theTrack;
    Track relativeTo;
    int intensity;
    Script script;
    JFrame frame;
    ArrayList <JLabel> labels = new ArrayList <JLabel>();
    JRadioButton start, end;
    ButtonGroup startAndEnd;
    JSlider changeIntensity;
    ArrayList <JButton> buttons = new ArrayList <JButton>();
    boolean startOrEnd;
   
    int initialIntensity;
    boolean initialStartOrEnd;
   
    public TrackDialog(Track track){
        frame = new JFrame(track.getTrackName());
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setContentPane(new ContentPane());

        theTrack = track;
        
        initialValues();
    }
    private void initialValues(){
        initialStartOrEnd = startOrEnd;
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
        JLabel currentTrack = new JLabel(theTrack.getTrackName());
        labels.add(currentTrack);

        JLabel relativeTrack = new JLabel(relativeTo.getTrackName());
        labels.add(relativeTrack);

        add(currentTrack);
        add(relativeTrack);
    }
    public void drawStartOrEnd(){
        JLabel startOrEnd = new JLabel("Start/End:");
        labels.add(startOrEnd);

        if(this.startOrEnd == true){
            start = new JRadioButton("Start", true);
            start.setActionCommand("Start");
            start.addActionListener(this);
            end = new JRadioButton("End");
            end.setActionCommand("End");
            end.addActionListener(this);
        }
        else {
            end = new JRadioButton("End", true);
            end.setActionCommand("End");
            end.addActionListener(this);
            start = new JRadioButton("Start");
            start.setActionCommand("Start");
            start.addActionListener(this);
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
            button.addActionListener(this);
            buttons.add(button);
        }
        for (int i = 0; i < buttons.size(); i++){
            add(buttons.get(i));
        }
    }
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("Start"))
            theTrack.setStart(true);
        else
            if (actionCommand.equals("End"))
                theTrack.setStart(false);

                   
    }
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();

        if (!source.getValueIsAdjusting()) {
            intensity = (int)source.getValue();
        }
        theTrack.setIntensity(intensity);
    }
    public void preview(){
        // use only the file name from track to generate sound clip
        JFrame preview = new JFrame(theTrack.getTrackName());
        preview.setSize(250, 250);
        preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        preview.setVisible(true);

        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("Cancel Preview");
        cancel.addActionListener(this);
        preview.add(cancel);

        try {
            File file = new File(theTrack.getPath());
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            stream = AudioSystem.getAudioInputStream(file);
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
        theTrack.setStart(initialStartOrEnd);
        theTrack.setIntensity(initialIntensity);
        dispose();
    }
    public static void main(String args[]){
        TrackDialog test = new TrackDialog(new Track("Test", new Script("Test", "C:\\"), "C:\\"));
    }
}*/
