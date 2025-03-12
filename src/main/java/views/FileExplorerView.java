package views;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import components.FileExplorerNode;
import journal.AppConstants;
import utilities.GeneralUtilities;

public class FileExplorerView {
	
	private TreePath treePath;
	private JFrame mainFrame;
	private JTree tree;
	private static final Logger logger = LogManager.getLogger(FileExplorerView.class);
	
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
		List<File> fileList = Arrays.asList(files);
		fileList.sort(new Comparator<File>() {	

			@Override
			public int compare(File o1, File o2) {
				Map<String, Integer> correctMonthOrder = new HashMap<>();
				correctMonthOrder.put("January", 1);
				correctMonthOrder.put("February", 2);
				correctMonthOrder.put("March", 3);
				correctMonthOrder.put("April", 4);
				correctMonthOrder.put("May", 5);
				correctMonthOrder.put("June", 6);
				correctMonthOrder.put("July", 7);
				correctMonthOrder.put("August", 8);
				correctMonthOrder.put("September", 9);
				correctMonthOrder.put("October", 10);
				correctMonthOrder.put("November", 11);
				correctMonthOrder.put("December", 12);
				try {
					String name1 = o1.getName();
					String name2 = o2.getName();
					if(name1.contains(AppConstants.ENTRY) && name2.contains(AppConstants.ENTRY)) {
						//sort entries by date
						Integer date1 = Integer.parseInt(name1.substring((name1.indexOf("-") + 1), (name1.indexOf("."))));
						Integer date2 = Integer.parseInt(name2.substring((name2.indexOf("-") + 1), (name2.indexOf("."))));
						return date1.compareTo(date2);
					}else if(correctMonthOrder.get(name1) != null && correctMonthOrder.get(name2) != null) {
						return correctMonthOrder.get(name1).compareTo(correctMonthOrder.get(name2));
						//sort months.
					}else {
						//this should handle the sorting by years, and anything else we missed. 
						return o1.compareTo(o2);			
					}
				}catch(Exception e) {
					logger.error("sorting files", e);
					e.printStackTrace();
				}
				return o1.compareTo(o2);
			}
			
		});
		for(File file : fileList) {
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
