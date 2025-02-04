package actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;

import components.FileExplorerNode;
import controllers.FileExplorerController;

public class FileExplorerMouseListener implements MouseListener{
	
	FileExplorerController controller;
	
	public FileExplorerMouseListener(FileExplorerController controller) {
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JTree tree = (JTree)e.getSource();
		FileExplorerNode node = (FileExplorerNode)tree.getLastSelectedPathComponent();
		controller.handleClickEvent(node);
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
