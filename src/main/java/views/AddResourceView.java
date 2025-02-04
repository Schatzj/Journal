package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AddResourceView {
	
	private JFrame mainFrame;
	private JButton addResource;
	private JButton move; 
	private JButton copy;
	private JPanel resourceListPanel;
	
	public AddResourceView() {		
		mainFrame = new JFrame();
		mainFrame.setTitle("Journal - Add Resources");
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setResizable(true);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JScrollPane scrollViewer = new JScrollPane(mainPanel,
		         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		addResource = new JButton("Add Resource");
		move = new JButton("Move");
		copy = new JButton("Copy");
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(addResource);
		buttonPanel.add(move);
		buttonPanel.add(copy);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		
		resourceListPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(resourceListPanel, BoxLayout.Y_AXIS);
		resourceListPanel.setLayout(boxLayout);
		
		mainPanel.add(resourceListPanel, BorderLayout.CENTER);
		
		mainFrame.add(scrollViewer);
	}
	
	

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JButton getAddResource() {
		return addResource;
	}

	public void setAddResource(JButton addResource) {
		this.addResource = addResource;
	}

	public JButton getMove() {
		return move;
	}

	public void setMove(JButton move) {
		this.move = move;
	}

	public JButton getCopy() {
		return copy;
	}

	public void setCopy(JButton copy) {
		this.copy = copy;
	}

	public JPanel getResourceListPanel() {
		return resourceListPanel;
	}

	public void setResourceListPanel(JPanel resourceListPanel) {
		this.resourceListPanel = resourceListPanel;
	}

}
