import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import javax.sound.sampled.*;
// To do: make sure preview quits at end of track, set volume of preview based on intensity (0-100 -> -80-6)
public class TrackDialog extends JFrame implements MouseListener, ActionListener, ChangeListener, WindowListener {
	JFrame frame, previewWindow;
	Track origTrack, relativeTrack;
	boolean startOrEnd, openBoolean;
	int trackIntensity, returnOpenVal;
	JPanel contentPane, drawTracks, drawStartOrEnd, drawIntensity, drawButtons;
	JLabel trackName, relativeTrackName, startEnd, intensity, beginningOfScript;
	JRadioButton start, end;
	ButtonGroup group;
	JSlider changeIntensity;
	JButton preview, save, cancel;
	Clip clip;
	Timer clipTimer;
	ArrayList<JLabel> trackNames = new ArrayList<JLabel>();
	String trackPath;
	public TrackDialog(Track track){
		origTrack = track;
		startOrEnd = origTrack.getStart();
		trackIntensity = origTrack.getIntensity();
		relativeTrack = origTrack.getRelativeTo();
		trackPath = origTrack.getPath();
		frame = new JFrame(origTrack.getTrackName());
		frame.setSize(600, 250);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		drawTracks();
		drawStartOrEnd();
		drawIntensity();
		drawButtons();
		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}
	public void drawTracks(){
		drawTracks = new JPanel();
		drawTracks.setBorder(new EmptyBorder(10, 0, 10, 0));
		trackName = new JLabel(origTrack.getTrackName());
		trackName.addMouseListener(this);
		trackName.setBorder(new EmptyBorder(0, 100, 0, 100));
		drawTracks.add(trackName, BorderLayout.LINE_START);
		if (origTrack.getRelativeTo() == relativeTrack)
			relativeTrackName = new JLabel("Start of Script");
		else
			relativeTrackName = new JLabel(origTrack.getRelativeTo().getTrackName());
		relativeTrackName.addMouseListener(this);
		relativeTrackName.setBorder(new EmptyBorder(0, 100, 0, 100));
		drawTracks.add(relativeTrackName, BorderLayout.LINE_END);
		drawTracks.setVisible(true);
		contentPane.add(drawTracks);
	}
	public void drawStartOrEnd(){
		drawStartOrEnd = new JPanel();
		drawStartOrEnd.setBorder(new EmptyBorder(10, 0, 10, 0));
		startEnd = new JLabel("Start/End:");
		startEnd.setBorder(new EmptyBorder(0, 0, 0, 0));
		drawStartOrEnd.add(startEnd, BorderLayout.LINE_START);
		start = new JRadioButton("Start");
		start.setActionCommand("Start");
		start.addActionListener(this);
		drawStartOrEnd.add(start, BorderLayout.LINE_START);
		end = new JRadioButton("End");
		end.setActionCommand("End");
		end.addActionListener(this);
		drawStartOrEnd.add(end, BorderLayout.LINE_START);
		if (origTrack.getStart())
			start.setSelected(true);
		else
			end.setSelected(true);
		group = new ButtonGroup();
		group.add(start);
		group.add(end);
		drawStartOrEnd.setVisible(true);
		contentPane.add(drawStartOrEnd);
	}
	public void drawIntensity(){
		drawIntensity = new JPanel();
		intensity = new JLabel("Intensity:");
		drawIntensity.add(intensity, BorderLayout.PAGE_START);
		changeIntensity = new JSlider(0, 100, origTrack.getIntensity());
		changeIntensity.setMajorTickSpacing(10);
		changeIntensity.setMinorTickSpacing(1);
		changeIntensity.setPaintTicks(true);
		changeIntensity.setPaintLabels(true);
		changeIntensity.addChangeListener(this);
		drawIntensity.add(changeIntensity, BorderLayout.LINE_START);
		drawIntensity.setVisible(true);
		contentPane.add(drawIntensity);
	}
	public void drawButtons(){
		drawButtons = new JPanel();
		preview = new JButton("Preview");
		preview.setActionCommand("Preview");
		preview.addActionListener(this);
		drawButtons.add(preview, BorderLayout.LINE_START);
		save = new JButton("Save");
		save.setActionCommand("Save");
		save.addActionListener(this);
		drawButtons.add(save, BorderLayout.CENTER);
		cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.addActionListener(this);
		drawButtons.add(cancel, BorderLayout.LINE_END);
		drawButtons.setVisible(true);
		contentPane.add(drawButtons);
	}
	public void mouseClicked(MouseEvent arg0) {
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == trackName){
			openBoolean=true;
			FileChooser chooser=new FileChooser();
			if(returnOpenVal==JFileChooser.APPROVE_OPTION){
				trackPath=chooser.getChooser().getSelectedFile().getAbsolutePath();
			}
		} else {
			if (e.getSource() == relativeTrackName){
				JFrame openTrack = new JFrame();
				openTrack.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				openTrack.setVisible(true);
				JPanel contentPane = new JPanel();
				contentPane.setLayout(new GridLayout(origTrack.getScript().getScriptTracks().size() + 2, 1));
				JLabel tracks = new JLabel("Tracks:");
				tracks.setPreferredSize(new Dimension(500, 10));
				contentPane.add(tracks);
				beginningOfScript = new JLabel("Start of Script");
				tracks.setPreferredSize(new Dimension(500, 10));
                beginningOfScript.addMouseListener(this);
				contentPane.add(beginningOfScript);
				for(int i = 0; i < origTrack.getScript().getScriptTracks().size(); i++){
					JLabel trackName = new JLabel(origTrack.getScript().getScriptTracks().get(i).getTrackName());
					trackName.addMouseListener(this);
					trackNames.add(trackName);
					contentPane.add(trackName);
				}
				openTrack.setSize(200, 50 * (origTrack.getScript().getScriptTracks().size() + 2));
				openTrack.setContentPane(contentPane);
				openTrack.setVisible(true);
			} else {
				for (int i = 0; i < trackNames.size(); i++){
					if (e.getSource() == trackNames.get(i)){
						relativeTrack = origTrack.getScript().getScriptTracks().get(i);
						relativeTrackName.setText(origTrack.getScript().getScriptTracks().get(i).getTrackName());
					} 
				}
				if (e.getSource() == beginningOfScript){
					relativeTrack = origTrack.getRelativeTo();
					relativeTrackName.setText("Start of Script");
				}
				
			}
		}
	}
	public void mouseReleased(MouseEvent arg0) {
	}
	public static void main(String [] args){
		TrackDialog test = new TrackDialog(new Track("Test", new Script("Test Script", "C:\\Users\\Andrew\\workspace\\DubbingTool\\src"),
				"C:\\Users\\Andrew\\workspace\\DubbingTool\\src\\Test.wav"));
	}
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Start"))
			startOrEnd = true;
		else {
			if (actionCommand.equals("End"))
				startOrEnd = false;
			else {
				if (actionCommand.equals("Preview")){
					preview();
				} else {
					if (actionCommand.equals("Save")){
						origTrack.setStart(startOrEnd);
						origTrack.setIntensity(trackIntensity);
						if (!(origTrack.getPath().equals(trackPath))){
							Track newTrack = new Track(origTrack.getTrackName(), origTrack.getRelativeTo(), origTrack.getScript(), trackPath, origTrack.getStart(), origTrack.getIntensity(), origTrack.getSecondsOffset());
							origTrack.getScript().addTrack(newTrack);
							origTrack.getScript().deleteTrack(origTrack);
						}
						
						/*origTrack.setRelativeTo(whatever);
						origTrack.getScript().deleteTrack(origTrack);
						origTrack.getScript().addTrack(origTrack);*/
						
						//origTrack.setRelativeTo(relativeTrack);
						origTrack.getScript().addTrack(relativeTrack);
						origTrack.getScript().deleteTrack(origTrack.getRelativeTo());
						origTrack.getScript().saveScript();
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					}
					else {
						if (actionCommand.equals("Cancel")){
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
			trackIntensity = (int)source.getValue();
		}
	}
	public void preview(){
		//add a window listener to cancel review when x is clicked
		clipTimer = new Timer(origTrack.getDurationMilliseconds(), this);
		clipTimer.setActionCommand("Cancel Preview");
		clipTimer.addActionListener(this);
		previewWindow = new JFrame(origTrack.getTrackName());
		previewWindow.setSize(400, 180);
		previewWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		previewWindow.setVisible(true);
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel Preview");
		cancel.addActionListener(this);
		previewWindow.add(cancel);
		try {
			File file = new File(origTrack.getPath());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl gainControl =
					(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(((origTrack.getIntensity() * 86) / 100) - 80);
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
	public JFrame getFrame(){
		return frame;
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
