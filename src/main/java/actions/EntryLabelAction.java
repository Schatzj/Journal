package actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controllers.FileExplorerController;

public class EntryLabelAction implements MouseListener{
	
	FileExplorerController controller;
	
	public EntryLabelAction(FileExplorerController controller) {
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		controller.start();
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
