
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FileNotFound extends JFrame {
	
	JFrame frame;
	JPanel panel;

	public FileNotFound(String faultyFilePath) {
		
		panel=new JPanel();
		
		if (faultyFilePath.length()>60) {
			this.setSize(1000, 100);
		} else if (faultyFilePath.length()>36) {
			this.setSize(600, 100);
		} else {
			this.setSize(400, 100);
		}
		
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		panel.add(new JLabel(new ImageIcon("src/FILE NOT FOUND.png")));
		panel.add(new JLabel("File \"" + faultyFilePath + "\" not found."));
		this.add(panel);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		FileNotFound f=new FileNotFound("Z:\\Drive\\AOOD\\blablah\\Sample.xml");

	}

}
