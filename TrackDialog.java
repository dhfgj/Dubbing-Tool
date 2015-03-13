import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

import javax.sound.sampled.*;
public class TrackDialog extends JFrame implements ActionListener, MouseListener {
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
	Timer clipTimer;
	ArrayList<JLabel> trackNames = new ArrayList<JLabel>();
	boolean openBoolean;
	int returnOpenVal;
	File openFile;

	public TrackDialog(Track track){

		theTrack = track;
		initialTrack = track;

		frame = new JFrame(track.getTrackName());
		frame.setSize(500, 180);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		theTrackName.addMouseListener(this);
		contentPane.add(theTrackName, c);

		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		relativeTrackName = new JLabel(theTrack.getRelativeTo().getTrackName());
		relativeTrackName.addMouseListener(this);
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
		if (!source.getValueIsAdjusting()){
			theTrack.setIntensity((int)source.getValue());
		}
	}
	public void preview(){
		clipTimer = new Timer(theTrack.getDurationMilliseconds(), this);
		clipTimer.setActionCommand("Cancel Preview");
		clipTimer.addActionListener(this);

		previewWindow = new JFrame(theTrack.getTrackName());
		previewWindow.setSize(400, 180);
		previewWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		previewWindow.setVisible(true);

		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel Preview");
		cancel.addActionListener(this);
		previewWindow.add(cancel);

		try {
			File file = new File(theTrack.getPath());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.setFramePosition(0);
			clip.start();
			clipTimer.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args){
		TrackDialog test = new TrackDialog(new Track("Test", new Script("Test Script", "Z:\\My Documents\\DubbingTool\\src"), "Z:\\My Documents\\DubbingTool\\src\\Test.wav"));
	}
	public void mouseClicked(MouseEvent arg0) {

	}
	public void mouseEntered(MouseEvent arg0) {

	}
	public void mouseExited(MouseEvent arg0) {

	}
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == theTrackName){
			openBoolean=true;
			FileChooser chooser=new FileChooser();
			if(returnOpenVal==JFileChooser.APPROVE_OPTION){
				openFile=chooser.getChooser().getSelectedFile();
			}
		}
		else {
			if (e.getSource() == relativeTrackName){
				JFrame openTrack = new JFrame();
				openTrack.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				openTrack.setVisible(true);

				JPanel contentPane = new JPanel();
				contentPane.setLayout(new GridLayout(theTrack.getScript().getScriptTracks().size() + 2, 1));

				JLabel tracks = new JLabel("Tracks:");
				tracks.setPreferredSize(new Dimension(500, 10));
				contentPane.add(tracks);
				JLabel beginningOfScript = new JLabel("Beginning of Script");
				tracks.setPreferredSize(new Dimension(500, 10));
				contentPane.add(beginningOfScript);

				for(int i = 0; i < theTrack.getScript().getScriptTracks().size(); i++){
					JLabel trackName = new JLabel(theTrack.getScript().getScriptTracks().get(i).getTrackName());
					trackName.addMouseListener(this);
					trackNames.add(trackName);
					contentPane.add(trackName);
				}

				openTrack.setSize(200, 50 * (theTrack.getScript().getScriptTracks().size() + 2));
				openTrack.setContentPane(contentPane);
				openTrack.setVisible(true);
			} else {
				for (int i = 0; i < trackNames.size(); i++){
					if (e.getSource() == trackNames.get(i)){
						theTrack.setRelativeTo(theTrack.getScript().getScriptTracks().get(i));
					}
				}
			}
		}
	}
	public void mouseReleased(MouseEvent arg0) {

	}
	public class FileChooser extends JPanel implements ActionListener{
		JFileChooser chooser;
		JTextArea log;
		public FileChooser(){
			super(new BorderLayout());
			log = new JTextArea(5,20);
			log.setMargin(new Insets(5,5,5,5));
			log.setEditable(false);
			JScrollPane logScrollPane = new JScrollPane(log);
			chooser=new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			FileNameExtensionFilter filter=new FileNameExtensionFilter("XML File", "xml");
			chooser.setFileFilter(filter);
			add(logScrollPane, BorderLayout.CENTER);
			if(openBoolean){
				returnOpenVal = chooser.showOpenDialog(FileChooser.this);
				openBoolean=false;
			}
		}
		public void actionPerformed(ActionEvent arg0) {

		}
		public JFileChooser getChooser(){
			return chooser;
		}

	}
}
