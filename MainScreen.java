import java.sql.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.util.*;
import java.util.Date;
public class MainScreen extends JFrame {

	JPanel panel;
	JPanel buttonPanel;
	JButton playPause;
	JButton edit;
	JButton delete;
	
	JPanel tracks;
	JButton track;

	
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
	
	Script currentScript;
	
	private ArrayList<Track> trackList;
	private boolean previewing=false;
	
	public MainScreen(Script newScript) {
		currentScript=newScript;
		trackList=currentScript.getScriptTracks();
		setTracks();
		setPictures();
		setTimeline();
		
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(30,20,30,10));
		panel.setBackground(new Color(204,229,255));
		
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.setBackground(new Color(204,229,255));
		playPause=new JButton(new ImageIcon("PlayButton.png"));
		playPause.addActionListener(new PlayListener());
		edit=new JButton("Edit");
		delete=new JButton("Delete");
		playPause.setEnabled(false);
		edit.setEnabled(false);
		delete.setEnabled(false);
		buttonPanel.add(playPause);
		buttonPanel.add(edit);
		buttonPanel.add(delete);
		buttonPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		panel.add(buttonPanel);
	
	
		
		visualScroll=new JScrollPane(visualReps);
		visualScroll.addMouseListener(new VisualMouseListener());
		visualScroll.setPreferredSize(new Dimension(100,500));
		trackScroll=new JScrollPane(tracks);
		timelineScroll=new JScrollPane(timeline);
		
		smallerSplitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT, visualScroll, timelineScroll);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, trackScroll, smallerSplitPane);

		
		splitPane.setVisible(true);
		smallerSplitPane.setVisible(true);
		
		
		panel.add(splitPane);


		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panel);
		pack();
		setSize(1000,1000);
		setVisible(true); 

	}
	//sorts Tracks. 
	private void sortTracks(ArrayList<Track> list) {
		String trackNamePlaceholder=null;
		for (Track track: trackList) {
			if (track.getRelativeTo()!=null){
				if (trackNamePlaceholder.equals(track.getRelativeTo().getTrackName())) {
					//fix
				}

			} else {
				sortedTracks.set(0, track);
				trackName
			}
		}
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
			trackStats[counter][1]=new JLabel(current.getStartTime().toString());
			trackChart.add(trackStats[counter][1]);
			if (current.getRelativeTo()!=null)
				trackStats[counter][2]=new JLabel(current.getRelativeTo().getTrackName());
			else
				trackStats[counter][2]=new JLabel("Start of Script");
			trackChart.add(trackStats[counter][2]);
			trackStats[counter][3]=new JLabel(current.startOrEnd());
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
			if (trackList.get(counter).relativeTo==null){
				return trackList.get(counter);
			}
		}
		return null;
	}
	//Enter a track, gives array of tracks whose start times are relative to that track
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
		visualReps=new PicturesPanel();
		visualReps.setBackground(Color.WHITE);
		visualReps.setLayout(new BoxLayout(visualReps,BoxLayout.Y_AXIS));
	
		/*for (int counter=0;counter<trackList.size();counter++){
			Track current=trackList.get(counter);
			Track relative=current.getRelativeTo();
			BufferedImage toPrint=trackList.get(counter).generateGraphics("");
			ImageIcon image=new ImageIcon(toPrint);
			visualReps.add(new JLabel(image));
			//,secondsToPixels(current.getStartTimeSeconds()),30+(counter*50));
		}*/
		
	}
	//Just draws the timeline panel.
	private void setTimeline(){	
		timeline=new TimelinePanel();
		timeline.setBackground(Color.WHITE);
		
	}
	//Give it a time and it will convert it to the number of pixels that amount of time will take up.
	private int timeToPixels(Time convert){
		int hours=convert.getHours();
		int minutes=convert.getMinutes();
		int seconds=convert.getSeconds();
		int totalSeconds=seconds + (minutes*60) + (hours*60*60);
		return totalSeconds*5+10;
	}
	//This is to determine when someone clicks on the scrollable list which track they clicked on.
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
				new TrackDialog(currentTrack);
			} else if (e.getActionCommand().equals("Delete")) {
				if (JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(null,"Are you sure?","Conformation", JOptionPane.YES_NO_OPTION)){
					Script script=currentTrack.getScript();
					script.deleteTrack(currentTrack);
				} else {
					//nothing happens here
				}
			}
		}

	}
	//For the scrollable list, when someone clicks on it.
	class TrackMouseListener extends MouseInputAdapter {
		public void mouseClicked(MouseEvent e) {
		
			if (e.getButton()==MouseEvent.BUTTON3){
				//right click
				previewing=false;
				playPause.setEnabled(false);
				edit.setEnabled(false);
				delete.setEnabled(false);
				pausedAt=-1;
				startedAt=-2;
				RightClickOptions rco= new RightClickOptions(findWhichTrack((JLabel)e.getComponent()));
				rco.show(e.getComponent(), e.getX(), e.getY());

			} else if (e.getClickCount()==1) {
				//single click
				if (findWhichTrack((JLabel)e.getComponent())!=selected){
					previewing=false;
					pausedAt=-1;
					startedAt=-2;
					selected=findWhichTrack((JLabel)e.getComponent());
				}
				e.getComponent().setBackground(Color.gray);
				playPause.setEnabled(true);
				edit.setEnabled(true);
				delete.setEnabled(true);
			} else if (e.getClickCount()==2) {
				//double click
				previewing=false;
				playPause.setEnabled(false);
				edit.setEnabled(false);
				delete.setEnabled(false);
				pausedAt=-1;
				startedAt=-2;
				selected=findWhichTrack((JLabel)e.getComponent());
				new TrackDialog(selected);
			} 
		}
	}
	class VisualMouseListener extends MouseInputAdapter{
		public void MouseClicked(MouseEvent e) {
			if (e.getClickCount()==2) {
				previewing=false;
				pausedAt=-1;
				startedAt=-2;
				Track selected=null;
				String selectedName=null;
				int x=e.getX();
				int y=e.getY();
				for (int i=1; i<=trackList.size(); i++) {
					if (y<getHeight()*i/trackList.size() && y>getHeight()*(i-1)/trackList.size()) {
						selected=trackList.get(i-1);
						if (x<secondsToPixels(selected.getStartTimeSeconds()+selected.getLength())&& x>secondsToPixels(selected.getStartTimeSeconds())){
							new TrackDialog(selected);
						}
						//need to double check parameters of the click area

					}
				}
			}
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
			int incrimentW=width/5;
			g2.drawLine(0, 0, width, 0);
			for (int counter=width/5;counter<width;counter+=width/5){
				g2.drawLine(counter, 0, counter, height);
			}
			GridLayout layout=(GridLayout)getLayout();
			int numRows=layout.getRows();
			for (int counter=height/numRows;counter<height;counter+=height/numRows){
				g2.drawLine(0, counter, width, counter);
			}
		}
	}
	//This works but it isn't in components it's just drawn so I don't think they can be clicked on. A different method of drawing the pictures.
	class PicturesPanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			for (int counter=0;counter<trackList.size();counter++){
				Track current=trackList.get(counter);
				Track relative=current.getRelativeTo();
				BufferedImage toPrint=trackList.get(counter).generateGraphics("");
				g.drawImage(toPrint, secondsToPixels(current.getStartTimeSeconds()), 30+(counter*50), null);
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
	//this is for the play/pause button. I don't know how to pause it yet.
	class PlayListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (!previewing){
				if (pausedAt>0 & pausedAt<selected.getLength()) {
					selected.playTrackFrom();
					//clip allows users to specify from where they should start playing
				} else {
					now=new Date();
					startedAt=now.getTime();
					selected.playTrack();
					//this has to be changed too
				}
				playPause.setIcon(new ImageIcon("PauseButton.png"));
				previewing=true;
				
			}else{
				playPause.setIcon(new ImageIcon("PlayButton.png"));
				previewing=false;
				now=new Date();
				pausedAt=now.getTime()-startedAt;
			}
		}
	}
	public void playTrack(Track track, int frameStart) {
	    File soundFile = new File(track.getPath());
	    AudioInputStream sound=null;
		try {
			sound = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
	    Clip clip=null;
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
	    /*clip.addLineListener(new LineListener() {
	      public void update(LineEvent event) {
	        if (event.getType() == LineEvent.Type.STOP) {
	          event.getLine().close();
	          System.exit(0);
	          //this should probably open a popup
	        }
	      }
	    });*/
	    clip.start();

	}


}


