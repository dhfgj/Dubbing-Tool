import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URL;
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
	JFrame previewWindow; 
	Clip clip;
	
	public TrackDialog(Track track){

		theTrack = track;
		initialTrack = track;

		frame = new JFrame(track.getTrackName());
		frame.setSize(400, 180);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());

		drawTracks();
		drawIntensity();
		drawStartOrEnd();
		drawButtons();

		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}
	public void drawTracks(){
		c.insets = new Insets(0, 20, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		theTrackName = new JLabel(theTrack.getTrackName());
		contentPane.add(theTrackName, c);

		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		relativeTrackName = new JLabel(theTrack.getRelativeTo().getTrackName());
		contentPane.add(relativeTrackName, c);
	}
	public void drawIntensity(){
		c.insets = new Insets (0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		intensityLabel = new JLabel("Intensity:");
		contentPane.add(intensityLabel, c);

		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		changeIntensity = new JSlider(0, 100, theTrack.getIntensity());
		changeIntensity.setMajorTickSpacing(10);
		changeIntensity.setMinorTickSpacing(1);
		changeIntensity.setPaintTicks(true);
		changeIntensity.setPaintLabels(true);
		contentPane.add(changeIntensity, c);

	}
	public void drawStartOrEnd(){
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
		start = new JRadioButton("Start");

		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		end = new JRadioButton("End");

		if (theTrack.getStart())
			start.setSelected(true);
		else 
			end.setSelected(true);

		start.setActionCommand("Start");
		start.addActionListener(this);
		end.setActionCommand("End");
		end.addActionListener(this);

		contentPane.add(start, c);
		contentPane.add(end, c);

	}
	public void drawButtons(){
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
					preview();
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
						} else {
							if (actionCommand.equals("Cancel Preview")){
								previewWindow.setVisible(false);
								clip.stop();
								previewWindow.dispose();
							}
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
		
		previewWindow = new JFrame(theTrack.getTrackName());
		previewWindow.setSize(400, 180);
		previewWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		previewWindow.setVisible(true);
		
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel Preview");
		cancel.addActionListener(this);
		previewWindow.add(cancel);
		
		  try {
		         // Use URL (instead of File) to read from disk and JAR.
		         File file = new File(theTrack.getPath());
		         // Set up an audio input stream piped from the sound file.
		         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		         // Get a clip resource.
		         clip = AudioSystem.getClip();
		         // Open audio clip and load samples from the audio input stream.
		         clip.open(audioInputStream);

		         clip.setFramePosition(0);
				 clip.start();
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		  
	}
	public static void main(String[] args){
		TrackDialog test = new TrackDialog(new Track("Test", new Script("Test Script", "C:\\Users\\Andrew\\workspace\\DubbingTool"), "C:\\Users\\Andrew\\workspace\\DubbingTool\\src\\Test.wav"));
	}
}
