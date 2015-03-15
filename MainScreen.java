import java.sql.*;
import java.awt.event.*;

import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.util.*;
import java.util.Date;
import java.io.*;
import javax.sound.sampled.*;
public class MainScreen extends JFrame {
	int startYPos=-2;
	JPanel panel;
	JPanel buttonPanel;
	JButton playPause;
	JButton edit;
	JButton delete;

	JPanel tracks;
	JButton track;

	JMenuBar topMenu;
	JSplitPane splitPane;
	JSplitPane smallerSplitPane;
	JScrollPane visualScroll;
	JScrollPane trackScroll;
	JScrollPane timelineScroll;
	JPanel timeline;
	JPanel visualReps;
	Date now;
	long startedAt;
	long pausedAt;
	JLabel[][] trackStats;
	ArrayList<Track> sortedTracks;
	Track selected;
	JLabel[] trackImages;

	Script currentScript;
	private ArrayList<Track> trackList;
	boolean paused=false;
	Clip clip=null;
	int currentFrame;
	private boolean previewing=false;
	int timelineLength;
	TrackDialog currentDialog;
	public MainScreen(Script newScript, JMenuBar menu) {
		topMenu=menu;
		currentScript=newScript;
		this.setJMenuBar(topMenu);
		initializeWithScript();
	}
	public void updateMainScreen(Script script){
		currentScript=script;
		initializeWithScript();
	}
	
	private void initializeWithScript(){
		trackList=currentScript.getScriptTracks();
		setTracks();
		setPictures();
		setTimeline();
		setButtonPanel();

		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(30,20,30,10));
		panel.setBackground(new Color(204,229,255));
		visualScroll=new JScrollPane(visualReps);
		visualScroll.setPreferredSize(new Dimension(100,400));

		trackScroll=new JScrollPane(tracks);
		timelineScroll=new JScrollPane(timeline);
		smallerSplitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				visualScroll, timelineScroll);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, trackScroll,
				smallerSplitPane);
		timelineScroll.getHorizontalScrollBar().setModel(visualScroll.getHorizontalScrollBar().getModel());
		
		splitPane.setVisible(true);
		smallerSplitPane.setVisible(true);
		this.setJMenuBar(topMenu);
		panel.add(buttonPanel);
		panel.add(splitPane);

		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panel);
		pack();
		setSize(1000,1000);
		setVisible(true);

	}
	
	private void setButtonPanel(){
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.setBackground(new Color(204,229,255));
		playPause=new JButton(new ImageIcon("PlayButton.png"));
		playPause.addActionListener(new PlayListener());
		edit=new JButton("Edit");
		edit.addActionListener(new EditButtonListener());
		delete=new JButton("Delete");
		delete.addActionListener(new DeleteButtonListener());
		playPause.setEnabled(false);
		edit.setEnabled(false);
		delete.setEnabled(false);
		buttonPanel.add(playPause);
		buttonPanel.add(edit);
		buttonPanel.add(delete);
		buttonPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
	}

	private Time secondsToTime(int numSeconds){
		int hours, minutes, seconds;
		hours=numSeconds/3600;
		minutes=(numSeconds-(hours*3600))/60;
		seconds=numSeconds-(hours*3600)-(minutes*60);
		return new Time(hours,minutes,seconds);
	}

	//Draws Scrollable Track List
	private void setTracks(){
		tracks=new JPanel();
		JPanel labels=new JPanel();
		JPanel trackChart=new TracksPanel();
		trackChart.setLayout(new GridLayout(trackList.size(),5,20,20));
		labels.setLayout(new BoxLayout(labels,BoxLayout.X_AXIS));
		trackChart.setBackground(Color.white);
		labels.setBackground(Color.white);

		labels.add(new JLabel("File Name"));
		labels.add(Box.createHorizontalGlue());
		labels.add(Box.createRigidArea(new Dimension(10,0)));
		labels.add(new JLabel("Start Time"));
		labels.add(Box.createRigidArea(new Dimension(5,0)));
		labels.add(Box.createHorizontalGlue());
		labels.add(new JLabel("Relative to"));
		labels.add(Box.createRigidArea(new Dimension(20,0)));
		labels.add(Box.createHorizontalGlue());
		labels.add(new JLabel("From"));
		labels.add(Box.createRigidArea(new Dimension(20,0)));
		labels.add(Box.createHorizontalGlue());
		labels.add(new JLabel("Intensity"));
		labels.add(Box.createHorizontalGlue());

		trackStats=new JLabel[trackList.size()][5];

		for (int counter=0;counter<trackList.size();counter++){
			Track current=trackList.get(counter);
			trackStats[counter][0]=new JLabel(current.getTrackName());
			trackChart.add(trackStats[counter][0]);
			trackStats[counter][1]=new JLabel("" +
					secondsToTime(current.getSecondsOffset()));
			trackChart.add(trackStats[counter][1]);
			if (current.getRelativeTo()!=null)
				trackStats[counter][2]=new JLabel(current.getRelativeTo().getTrackName());
			else
				trackStats[counter][2]=new JLabel("Start of Script");
			trackChart.add(trackStats[counter][2]);
			String startOrEnd;
			if (current.getStart())
				startOrEnd="Start";
			else
				startOrEnd="End";
			trackStats[counter][3]=new JLabel(startOrEnd);
			trackChart.add(trackStats[counter][3]);
			trackStats[counter][4]=new JLabel("" + current.getIntensity());
			trackChart.add(trackStats[counter][4]);
		}
		tracks.setLayout(new BoxLayout(tracks,BoxLayout.Y_AXIS));
		tracks.add(labels);
		tracks.add(trackChart);
		trackSelect();
	}

	//Allows you to click on tracks

	private void trackSelect(){
		for (int counter1=0;counter1<trackList.size();counter1++){
			for (int counter2=0;counter2<5;counter2++){
				trackStats[counter1][counter2].addMouseListener(new TrackMouseListener());
			}
		}
	}
	//Finds track relative to start

	private Track findFirstTrack(){
		for (int counter=0;counter<trackList.size();counter++){
			if (trackList.get(counter).getRelativeTo()==null){
				return trackList.get(counter);
			}
		}
		return null;
	}

	//Enter a track, gives array of tracks whose start times are relativ to that track
	private Track[] tracksRelativeTo(Track root){
		ArrayList<Track> listTracks=new ArrayList<Track>();
		Track[] relativeTracks;
		int num=0;
		for (int counter=0;counter<trackList.size();counter++){
			if (trackList.get(counter).getRelativeTo()==root){
				num++;
				listTracks.add(trackList.get(counter));
			}
		}
		relativeTracks=new Track[num];
		for (int counter=0;counter<relativeTracks.length;counter++){
			relativeTracks[counter]=listTracks.get(counter);
		}
		return relativeTracks;
	}

	//This is what I started for displaying the pictures, but it doesn't work.
	private void setPictures(){

		visualReps=new JPanel();
		visualReps.setBackground(Color.WHITE);
		visualReps.setLayout(null);
		trackImages=new JLabel[trackList.size()];

		for (int counter=0;counter<trackList.size();counter++){
			Track current=trackList.get(counter);
			BufferedImage toPrint=current.generateGraphics();
			trackImages[counter]=new JLabel(new ImageIcon(toPrint));			
			trackImages[counter].setBorder(BorderFactory.createTitledBorder(current.getTrackName()));
			trackImages[counter].setBounds(secondsToPixels(current.startTime()),(counter*120),trackImages[counter].getPreferredSize().width,trackImages[counter].getPreferredSize().height);
			trackImages[counter].repaint();
			trackImages[counter].addMouseListener(new VisualMouseListener());
			visualReps.add(trackImages[counter]);
		}

		int farthestImage=0,index=0;
		for (int counter=0;counter<trackImages.length;counter++){
			if (trackImages[counter].getBounds().getX()>farthestImage){
				farthestImage=(int)trackImages[counter].getBounds().getX();
				index=counter;
			}
		}
		timelineLength=farthestImage+300;
		if (timelineLength%30<3){
			timelineLength+=10;
		}
		
		
		
		visualReps.setPreferredSize(new Dimension(timelineLength,500));

	}
	//Just draws the timeline panel.

	private void setTimeline(){
		timeline=new TimelinePanel();
		timeline.setPreferredSize(new Dimension(timelineLength, 100));
		timeline.setBackground(Color.WHITE);

	}

	//Give it a time and it will convert it to the number of pixels tha amount of time will take up.

	private int timeToPixels(Time convert){
		int hours=convert.getHours();
		int minutes=convert.getMinutes();
		int seconds=convert.getSeconds();
		int totalSeconds=seconds + (minutes*60) + (hours*60*60);
		return totalSeconds*5+10;
	}

	//This is to determine when someone clicks on the scrollable listwhich track they clicked on.

	private Track findWhichTrack(JLabel label){
		int rowIndex=0;
		for (int counter=0;counter<trackList.size();counter++){
			for (int counter2=0;counter2<5;counter2++){
				if (trackStats[counter][counter2]==label){
					rowIndex=counter;
				}
			}
		}
		String name=trackStats[rowIndex][0].getText();
		for (int counter=0;counter<trackList.size();counter++){
			if (trackList.get(counter).getTrackName().equals(name)){
				return trackList.get(counter);
			}
		}
		return null;
	}

	//Just to more easily break down a time into seconds.
	private int timeToSeconds(Time time){
		int seconds=time.getHours()*60*60 + time.getMinutes()*60 + time.getSeconds();
		return seconds;
	}

	//Not that necessary, but this is the conversion from seconds to pixels.
	private int secondsToPixels(int seconds){
		return seconds*5+10;
	}

	class RightClickOptions extends JPopupMenu implements ActionListener {
		Track currentTrack;
		public RightClickOptions(Track current) {
			JMenuItem editFromMenu=new JMenuItem("Edit");
			editFromMenu.addActionListener(this);
			JMenuItem deleteFromMenu=new JMenuItem("Delete");
			deleteFromMenu.addActionListener(this);
			add(editFromMenu);
			add(deleteFromMenu);
			currentTrack=current;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Edit")) {
				currentDialog=new TrackDialog(currentTrack);
				currentDialog.getFrame().addWindowListener(new DialogListener());

			} else if (e.getActionCommand().equals("Delete")) {
				if (JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(null,"Are you sure?","Conformation", JOptionPane.YES_NO_OPTION)){
					Script script=currentTrack.getScript();
					script.deleteTrack(currentTrack);
					currentScript=script;
					initializeWithScript();
				} else {
					//nothing happens here
				}
			}
		}
	}

	public void playTrack(Track track, int frameStart) {
		AudioInputStream sound=null;
		File soundFile = new File(track.getPath());
		try {
			sound = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if (sound!=null){
			DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());

			try {
				clip = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			}
			try {
				clip.open(sound);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			clip.setFramePosition(frameStart);
			clip.addLineListener(new LineListener() {
              public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                	if (!paused){
        				playPause.setIcon(new ImageIcon("PlayButton.png"));
        				currentFrame=0;
        				pausedAt=-1;
        				startedAt=-2;
                	}
                }
              }
            });
			clip.start();
		}
	}

	//For the scrollable list, when someone clicks on it.
	private void highlightTrack(Track selected) {
		for (int i=0; i<trackList.size(); i++) {

			if (selected.equals(trackList.get(i))) {
				for (int m=0; m<5; m++) {
					trackStats[i][m].setBorder(new EtchedBorder() {
						private static final long serialVersionUID = 1L;

						@Override
						public Insets getBorderInsets(Component c, Insets insets){
							// arbitrary insets for top and bottom.
							return new Insets(insets.top - 10,
									insets.left, insets.bottom - 10, insets.right);
						}});

				}
			} else {
				for (int m=0; m<5; m++) {
					trackStats[i][m].setBorder(BorderFactory.createEmptyBorder());
				}
			}
			tracks.repaint();

		}
	}
	class TrackMouseListener extends MouseInputAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getButton()==MouseEvent.BUTTON3){
				//right click
				previewing=false;
				//startYPos=-100;
				playPause.setEnabled(false);
				edit.setEnabled(false);
				delete.setEnabled(false);
				pausedAt=-1;
				startedAt=-2;
				RightClickOptions rco= new
						RightClickOptions(findWhichTrack((JLabel)e.getComponent()));
				rco.show(e.getComponent(), e.getX(), e.getY());
			} else if (e.getClickCount()==1) {
				//single click
				if (findWhichTrack((JLabel)e.getComponent())!=selected){
					previewing=false;
					pausedAt=-1;
					startedAt=-2;
					selected=findWhichTrack((JLabel)e.getComponent());
				}
				highlightTrack(selected);
				playPause.setEnabled(true);
				edit.setEnabled(true);
				delete.setEnabled(true);
			} else if (e.getClickCount()==2) {
				//double click
				previewing=false;
				//startYPos=-100;
				playPause.setEnabled(false);
				edit.setEnabled(false);
				delete.setEnabled(false);
				pausedAt=-1;
				startedAt=-2;
				selected=findWhichTrack((JLabel)e.getComponent());
				currentDialog=new TrackDialog(selected);
				currentDialog.getFrame().addWindowListener(new DialogListener());

			}
			String trackn="";
			if (selected==null) {
				trackn="null";
			} else {
				trackn=selected.getTrackName();
			}

		}
	}
	private Track whichTrack(JLabel label){
		for (int counter=0;counter<trackImages.length;counter++){
			if (label.getIcon()==trackImages[counter].getIcon()){
				return trackList.get(counter);
			}
		}
		return null;
	}
	class VisualMouseListener extends MouseInputAdapter{
		public void mouseClicked(MouseEvent e) {
			if (e.getButton()==MouseEvent.BUTTON3){
				//right click
				previewing=false;
				//startYPos=-100;
				playPause.setEnabled(false);
				edit.setEnabled(false);
				delete.setEnabled(false);
				pausedAt=-1;
				startedAt=-2;
				RightClickOptions rco= new
						RightClickOptions(findWhichTrack((JLabel)e.getComponent()));
				rco.show(e.getComponent(), e.getX(), e.getY());
			}else if (e.getClickCount()==2) {
				previewing=false;
				pausedAt=-1;
				startedAt=-2;
				selected=findWhichTrack((JLabel)e.getComponent());
				currentDialog=new TrackDialog(selected);			
				currentDialog.getFrame().addWindowListener(new DialogListener());

			}else if (e.getClickCount()==1){
				JLabel currentLabel=(JLabel)e.getComponent();
				selected=whichTrack(currentLabel);
				playPause.setEnabled(true);
				edit.setEnabled(true);
				delete.setEnabled(true);
				highlightTrack(selected);
			}
		}
	}
	class VisualPanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);

		}
	}
	//Draws the chart for the tracks
	class TracksPanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D)g;
			int width,height;
			width=getWidth();
			height=getHeight();
			g2.drawLine(0, 0, width, 0);
			for (int counter=width/5;counter<width;counter+=width/5){
				g2.drawLine(counter, 0, counter, height);
			}
			GridLayout layout=(GridLayout)getLayout();
			int numRows=layout.getRows();
			if (trackList.size()!=0){
				for (int counter=height/numRows;counter<height;counter+=height/numRows){
					g2.drawLine(0, counter, width, counter);
				}
			}
		}
	}

	//Draws the timeline
	class TimelinePanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2=(Graphics2D)g;
			int width=getWidth();
			int height=getHeight();
			g2.setStroke(new BasicStroke(3));
			g2.drawLine(5,height/2,width-5,height/2);
			g2.drawLine(5, height/2-5, 5, height/2+5);
			int numSeconds=0, numMinutes=0, numHours=0;
			g2.setStroke(new BasicStroke(1));
			String secondString;
			for (int counter=10;counter<width-5;counter+=5){
				if (numSeconds==30){
					g2.drawLine(counter, height/2-10, counter, height/2+10);
					if (numSeconds<10)
						secondString="0" + numSeconds;
					else
						secondString=numSeconds + "";
					g2.drawString(numMinutes + ":" + secondString, counter-13, height/2+30);
				}else if (numSeconds==60){
					g2.setStroke(new BasicStroke(2));
					g2.drawLine(counter, height/2-15, counter, height/2+15);
					numSeconds=0;
					g2.setStroke(new BasicStroke(1));
					numMinutes++;
					if (numMinutes%60==0)
						numHours++;
					if (numSeconds<10)
						secondString="0" + numSeconds;
					else
						secondString=numSeconds + "";
					g2.drawString(numMinutes + ":" + secondString, counter-13, height/2+30);
				}else{
					g2.drawLine(counter, height/2-5, counter, height/2+5);
				}
				numSeconds++;
			}
		}
	}

	//this is for the play/pause button.
	class PlayListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (!previewing){
				if (pausedAt>0 & pausedAt<selected.getLength()) {
					
				} else {
					now=new Date();
					startedAt=now.getTime();
					if (clip==null||currentFrame==clip.getFrameLength())
						currentFrame=0;
					playTrack(selected, currentFrame);
				}
				playPause.setIcon(new ImageIcon("PauseButton.png"));
				previewing=true;
				paused=false;
			}else{
				playPause.setIcon(new ImageIcon("PlayButton.png"));
				previewing=false;
				now=new Date();
				pausedAt=now.getTime()-startedAt;
				paused=true;
				if (clip.isActive()){
					currentFrame=clip.getFramePosition();
					clip.stop();
				}else{
					currentFrame=0;
					pausedAt=-1;
					startedAt=-2;
				}
			}
		}
	}
	class EditButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			currentDialog=new TrackDialog(selected);
			currentDialog.getFrame().addWindowListener(new DialogListener());
		}
	}
	class DeleteButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			currentScript.deleteTrack(selected);
			if (JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(null,"Are you sure?","Conformation", JOptionPane.YES_NO_OPTION)){
				Script script=selected.getScript();
				script.deleteTrack(selected);
				currentScript=script;
				initializeWithScript();
			} else {
				//nothing happens here
			}
		}
	} 
	class DialogListener extends WindowAdapter{
		
		public void windowClosing(WindowEvent e){
			initializeWithScript();
		}
	}
}
