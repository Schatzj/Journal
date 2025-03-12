package actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;

import components.FileExplorerNode;
import controllers.FileExplorerController;

public class FileExplorerMouseListener implements MouseListener{
	
	private FileExplorerController controller;
	private int clickCount = 0;
	private long lastClickTime = 0;
	private String clickedItemName = "defualt";
	
	public FileExplorerMouseListener(FileExplorerController controller) {
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clickCount++;
		long currentTime = System.currentTimeMillis();
		long timeBetweenClicks = currentTime - lastClickTime;
		if(timeBetweenClicks > 1000) {
			clickCount = 1;
		}
		lastClickTime = currentTime;
		
		JTree tree = (JTree)e.getSource();
		FileExplorerNode node = (FileExplorerNode)tree.getLastSelectedPathComponent();
		
		if(clickCount > 1 && clickedItemName.equals(node.getFilePath())) {
			clickCount = 0;
			controller.handleClickEvent(node);
		}
		if(node != null) {
			clickedItemName = node.getFilePath();
		}		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
