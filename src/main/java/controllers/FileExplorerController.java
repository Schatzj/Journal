package controllers;

import java.awt.Dimension;

import javax.swing.JFrame;

import actions.FileExplorerMouseListener;
import components.FileExplorerNode;
import views.FileExplorerView;

public class FileExplorerController {
	
	private FileExplorerView view;
	private MainController mainController;
	
	public FileExplorerController(MainController mainController) {
		view = new FileExplorerView();
		this.mainController = mainController;
		
		view.getTree().addMouseListener(new FileExplorerMouseListener(this));
	}
	
	public void start() {
		view = new FileExplorerView(); //get a new view. We want to refresh the view to pick up any changes since it was last opened.
		view.getTree().addMouseListener(new FileExplorerMouseListener(this));
		JFrame mainFrame = view.getMainFrame();
		
		double width = (300);
		double height = (500);
		mainFrame.setPreferredSize(new Dimension((int)width, (int)height));
		mainFrame.pack();
		mainFrame.setLocation(2, 5);
		mainFrame.setVisible(true);
	}
	
	public void handleClickEvent(FileExplorerNode clickedNode) {
		if(clickedNode != null) {
			mainController.loadEntryFromPath(clickedNode.getFilePath());
		}		
	}
}
