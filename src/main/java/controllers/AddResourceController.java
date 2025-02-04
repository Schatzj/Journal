package controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.HighlightFocusAction;
import components.ResourceModel;
import services.FileService;
import views.AddResourceView;

public class AddResourceController {
	
	private AddResourceView view;
	private JFrame mainFrame;
	private JPanel resourceListPanel;
	
	private List<File> selectedResources;
	private List<JTextField> names; 
	private String selectedResourcesBasePath;
	
	private static final Logger logger = LogManager.getLogger(AddResourceController.class);

	public AddResourceController() {
	}
	
	public void start() {
		selectedResources = new ArrayList<>();
		names = new ArrayList<>();
		
		view = new AddResourceView();
		
		mainFrame = view.getMainFrame();
		resourceListPanel = view.getResourceListPanel();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = (screenSize.width - (screenSize.width * 0.75));
		double height = (screenSize.height - (screenSize.height * 0.65));
		mainFrame.setPreferredSize(new Dimension((int)width, (int)height));
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		addEventListeners();
	}
	
	private void addEventListeners() {
		JButton addResource = view.getAddResource();
		addResource.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setMultiSelectionEnabled(true);

				// Open the save dialog
				int response = fileChooser.showSaveDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					File[] files = fileChooser.getSelectedFiles();
					for(File file : files) {
						processFile(file);
					}
				}
				
				//required to redraw the view. 
				mainFrame.revalidate();
			}
			
			private void processFile(File file) {
				selectedResources.add(file);
				JTextField field = new JTextField(file.getName());
				field.setMaximumSize(new Dimension(1000, 25));
				field.addFocusListener(new HighlightFocusAction());
				if(file.isDirectory()) {
					Color directoryColor = new Color(197, 223, 240);
					field.setBackground(directoryColor);
				}
				names.add(field);
				resourceListPanel.add(field);
				
				if(file.isDirectory()) {
					File[] files = file.listFiles();
					List<File> filesWithDirectoriesLast = new ArrayList<>();
					for(File f : files) {
						filesWithDirectoriesLast.add(f);
					}
					filesWithDirectoriesLast.sort(new Comparator<File>() {
						@Override
						public int compare(File o1, File o2) {
							if(o1.isDirectory() && o2.isDirectory()) {
								return 0;
							}else if(o1.isDirectory()) {
								return 1;
							}else {
								return -1;
							}
						}
					});
					
					for(File f : filesWithDirectoriesLast) {
						processFile(f);
					}
				}
			}
		});
		
		JButton move = view.getMove();
		move.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileService fileService = new FileService();
				List<ResourceModel> resources = new ArrayList<>();
				for(int i = 0; i<selectedResources.size(); i++) {
					ResourceModel model = new ResourceModel();
					model.setUserProvidedName(names.get(i).getText());
					model.setFile(selectedResources.get(i));
					resources.add(model);
				}
				
				Map<String, List<ResourceModel>>data = fileService.organizeResources(resources);
				Set<String> keys = data.keySet();
				int index = 0; 
				try {
					for(String key : keys) {
						List<ResourceModel> list = data.get(key);
						if(index < 1) {
							fileService.moveFilesAtSelectedRoot(list);
						}else {
							fileService.renameFilesInNestedDirectories(key, list);
						}
						index++;
					}
					
					//now that all files are properly renamed, ensure all directories are also renamed
					fileService.renameDirectories(data);
				}catch(Exception error) {
					logger.error("Error moving resource ", error);
				}
			}
		});
		
		JButton copy = view.getCopy();
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileService fileService = new FileService();
				List<ResourceModel> resources = new ArrayList<>();
				for(int i = 0; i<selectedResources.size(); i++) {
					ResourceModel model = new ResourceModel();
					model.setUserProvidedName(names.get(i).getText());
					model.setFile(selectedResources.get(i));
					resources.add(model);
				}
				
				Map<String, List<ResourceModel>>data = fileService.organizeResources(resources);
				Set<String> keys = data.keySet();
				int index = 0; 
				try {
					for(String key : keys) {
						List<ResourceModel> list = data.get(key);
						if(index < 1) {
							fileService.copyFilesAtSelectedRoot(list);
						}else {
							fileService.renameFilesInNestedDirectories(key, list);
						}
						index++;
					}
					
					//now that all files are properly renamed, ensure all directories are also renamed
					fileService.renameDirectories(data);
				}catch(Exception error) {
					logger.error("Error copying resource ", error);
				}
			}
		});
	}
}
