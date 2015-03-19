import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class TrackDialog extends JFrame implements ActionListener, ChangeListener, WindowListener {
	JFrame frame, previewWindow;
	Track origTrack, relativeTrack;
	boolean startOrEnd, openBoolean;
	int trackIntensity, returnOpenVal;
	JPanel contentPane, drawTracks, drawStartOrEnd, drawIntensity, drawButtons;
	JLabel emptyLabel, trackLabel, relativeTrackLabel, startEnd, intensity, beginningOfScript;
	JComboBox trackName, relativeTrackName;
	JRadioButton start, end;
	ButtonGroup group;
	JSlider changeIntensity;
	JButton track, preview, save, cancel;
	Clip clip;
	Timer clipTimer;
	String[] trackNames;
	String trackPath;

	public TrackDialog(Track track){
		origTrack = track;
		startOrEnd = origTrack.getStart();
		trackIntensity = origTrack.getIntensity();
		relativeTrack = origTrack.getRelativeTo();
		trackPath = origTrack.getPath();
		frame = new JFrame(origTrack.getTrackName());
		frame.setSize(540, 215);
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
		int relativeIndex = 0;

		drawTracks = new JPanel();
		
		trackLabel = new JLabel("Current Track:");
		//trackLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
		drawTracks.add(trackLabel, BorderLayout.LINE_END);
		
		track = new JButton(origTrack.getTrackName());
		track.setBorder(new EmptyBorder(0, 0, 0, 10));
		track.setPreferredSize(new Dimension(85, 25));
		track.setForeground(Color.black);
		track.setActionCommand("TrackName");
		track.addActionListener(this);
		drawTracks.add(track, BorderLayout.LINE_START);
		
		emptyLabel = new JLabel();
		emptyLabel.setPreferredSize(new Dimension(40, 25));
		drawTracks.add(emptyLabel, BorderLayout.LINE_START);

		relativeTrackLabel = new JLabel("Relative Track:");
		drawTracks.add(relativeTrackLabel, BorderLayout.LINE_END);
		
		trackNames = new String[origTrack.getScript().getScriptTracks().size()];
		//System.out.println(origTrack.getScript().getScriptTracks().size());
		for (int i = 0; i < origTrack.getScript().getScriptTracks().size(); i++){
			trackNames[i] = origTrack.getScript().getScriptTracks().get(i).getTrackName();
		}
		//for (int i = 0; i < trackNames.length; i++)
		//	System.out.println(trackNames[i]);
		for (int i = 0; i < trackNames.length; i++){
			if (trackNames[i].equals(origTrack.getRelativeTo().getTrackName())){
				relativeIndex = i;
			}
		}
		relativeTrackName = new JComboBox(trackNames);
		relativeTrackName.setPreferredSize(new Dimension(85, 25));
		relativeTrackName.setSelectedIndex(relativeIndex);
		relativeTrackName.setActionCommand("Relative TrackName");
		relativeTrackName.addActionListener(this);
		drawTracks.add(relativeTrackName, BorderLayout.LINE_END);

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
		changeIntensity.setPreferredSize(new Dimension(250, 43));
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
	public static void main(String [] args){
		/*Script currentScript;
        MainScreen theScreen;
        Menu menu=new Menu();
        currentScript=new Script("First Script", "C:\\Users\\Andrew\\workspace\\DubbingTool\\src\\begu.xml");
        try {
            currentScript.addTrack(new Track("Hi bro", currentScript, 0, "hi"));
            currentScript.addTrack(new Track("Sup bro", currentScript, 0, "hi"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        theScreen = new MainScreen(currentScript, menu);*/
		//TrackDialog test = new TrackDialog(new Track("Test", new Script("Test Script", "C:\\Users\\Andrew\\workspace\\DubbingTool\\src"),
		//		"C:\\Users\\Andrew\\workspace\\DubbingTool\\src\\Test.wav"));
		
		Script currentScript=new Script("First Script", "Z:\\begu.xml");
        currentScript=new Script("First Script", "Z:\\begu.xml");
        Track hiBro = new Track("Hi bro", currentScript, 0, "Z:\\My Documents\\DubbingTool\\src\\Test.wav");
        Track supBro = new Track("Sup bro", hiBro, currentScript, "Z:\\My Documents\\DubbingTool\\src");
        try {
            currentScript.addTrack(hiBro);
            currentScript.addTrack(supBro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrackDialog test = new TrackDialog(hiBro);

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
							origTrack.getScript().deleteTrack(origTrack);
							try {
								origTrack.getScript().addTrack(newTrack);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						/*origTrack.setRelativeTo(whatever);
                        origTrack.getScript().deleteTrack(origTrack);
                        origTrack.getScript().addTrack(origTrack);*/

						if (relativeTrack != origTrack.getRelativeTo()){
							origTrack.setRelativeTo(relativeTrack);
							origTrack.getScript().deleteTrack(origTrack);
							try {
								origTrack.getScript().addTrack(origTrack);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}   
						}

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
							} else {
								if (actionCommand.equals("Relative TrackName")){
									JComboBox cb = (JComboBox)e.getSource();
									String trackName = (String)cb.getSelectedItem();
									for (int i = 0; i < origTrack.getScript().getScriptTracks().size(); i++){
										if (trackName.equals(origTrack.getScript().getScriptTracks().get(i).getTrackName())){
											relativeTrack = origTrack.getScript().getScriptTracks().get(i);
											relativeTrackName.setSelectedIndex(i);
										}
									}
								}
								else {
									if (actionCommand.equals("TrackName")){
										openBoolean=true;
										FileChooser chooser=new FileChooser();
										if(returnOpenVal==JFileChooser.APPROVE_OPTION){
											trackPath=chooser.getChooser().getSelectedFile().getAbsolutePath();
										}
									}
								}
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
		//make it look nice 
		clipTimer = new Timer(origTrack.getDurationMilliseconds(), this);
		clipTimer.setActionCommand("Cancel Preview");
		clipTimer.addActionListener(this);
			
		previewWindow = new JFrame(origTrack.getTrackName());
		previewWindow.setLayout(new BorderLayout());
		previewWindow.setSize(300, 200);
		previewWindow.addWindowListener(this);
		previewWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		previewWindow.setVisible(true);
		
		JLabel previewLabel = new JLabel("Preview:");
		previewLabel.setPreferredSize(new Dimension(100, 100));
		previewWindow.add(previewLabel, BorderLayout.PAGE_START);
		
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel Preview");
		cancel.addActionListener(this);
		previewWindow.add(cancel, BorderLayout.CENTER);
		
		JLabel emptyLabel = new JLabel();
		emptyLabel.setPreferredSize(new Dimension(300, 20));
		previewWindow.add(emptyLabel, BorderLayout.PAGE_END);
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
	public void windowClosed(WindowEvent e) {
		if(e.getSource() == previewWindow){
			clip.stop();
		}

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
