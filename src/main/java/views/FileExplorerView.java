package views;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import components.FileExplorerNode;
import utilities.GeneralUtilities;

public class FileExplorerView {
	
	private TreePath treePath;
	private JFrame mainFrame;
	private JTree tree;
	
	public FileExplorerView(){//File topLevelDirectory) {
		mainFrame = new JFrame();
		mainFrame.setTitle("Journal");
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setResizable(true);
		
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout());
		
		String targetPath = GeneralUtilities.getFullFileSavePath();
		File topLevelDirectory = new File(GeneralUtilities.getFirstPartOfSavePath(false));
		FileExplorerNode topLevel = new FileExplorerNode(topLevelDirectory, topLevelDirectory.getPath());
		FileExplorerNode rootNode = addNodes(topLevel, topLevelDirectory, targetPath);
		
		treePath = finalizeTreePathForExpansion(rootNode);
		
		tree = new JTree(rootNode);
		tree.expandPath(treePath);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(tree,
		         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		mainPanel.add(scrollPane);
		mainFrame.add(mainPanel);
	}
	
	private TreePath finalizeTreePathForExpansion(FileExplorerNode rootNode) {
		Object[] rootPath = rootNode.getPath();
		Object[] path = treePath.getPath();
		List<Object> finalPath = new ArrayList<>();
		for(Object o : rootPath) {
			finalPath.add(o);
		}
		for(Object o : path) {
			finalPath.add(o);
		}
		treePath = new TreePath(finalPath.toArray());
		return treePath;
	}

	private FileExplorerNode addNodes(FileExplorerNode parentDirectory, File dir, String targetPath) {
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				String fullPath = file.getPath();
				String fileName = extractFileName(fullPath);
				FileExplorerNode currentNode = new FileExplorerNode(fileName, fullPath);
				addNodes(currentNode, file, targetPath);
				parentDirectory.add(currentNode);
				if(fullPath.equalsIgnoreCase(targetPath)) {
					treePath = new TreePath(currentNode.getPath());
				}
			}else {
				//file
				String fullPath = file.getPath();
				String fileName = extractFileName(fullPath);
				parentDirectory.add(new FileExplorerNode(fileName, file.getPath()));
			}
		}
	    
	    return parentDirectory;
	}

	private String extractFileName(String fullPath) {
		fullPath = fullPath.substring((fullPath.lastIndexOf("\\") + 1));
		return fullPath;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}
}
