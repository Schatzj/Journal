package components;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileExplorerNode extends DefaultMutableTreeNode{

	private static final long serialVersionUID = -813236420623469167L;
	private String filePath;
	
	public FileExplorerNode() {
		super();
	}
	
	public FileExplorerNode(Object userObject) {
		super(userObject);
	}
	
	public FileExplorerNode(Object userObject, String path) {
		super(userObject);
		this.filePath = path;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
