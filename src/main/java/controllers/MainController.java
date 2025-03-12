package controllers;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.EntryLabelAction;
import journal.AppConstants;
import utilities.GeneralUtilities;
import views.MainView;

public class MainController {

	private MainView view;
	private String secondPartOfSavePath;
//	private Calendar cal;
	
	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	public MainController(MainView view) {
		this.view = view;
	}

	public void startMainView() { 
		//we want the second part of the path, and a calendar instance need to be saved here in the controller which calls the save. 
		//this state needs to be saved, so if we open a past file to edit, we don't save it as the current days entry.
		//as long as we keep this state up to date, saving should not be an issue. 
//		AppConstants.cal = Calendar.getInstance();
		
		JFrame frame = view.getMainFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = (screenSize.width - (screenSize.width * 0.25));
		double height = (screenSize.height - (screenSize.height * 0.15));
		frame.setPreferredSize(new Dimension((int)width, (int)height));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		loadData();

		addActionListners();
	}
	
	/**
	 * This attempts to load a file from the file Explorer. 
	 * The application is not meant as a file explorer, editor. 
	 * We are fairly picky about what files we will load. We really only want to load existing entries that were created with this application. 
	 * 
	 * In the case of an error, we either do nothing, or load the current entry depending on how quickly we find the error. 
	 * @param path
	 */
	public void loadEntryFromPath(String path) {
		//
		save();
		//we are not a general file viewer, only load the file if it is a .md file. 
		String extention = path.substring((path.lastIndexOf(".") + 1));
		if(!extention.equalsIgnoreCase("md")){ 
			try {
				Desktop desktop = Desktop.getDesktop();
	            File file = new File(path);
	            desktop.open(file);
			}catch(Exception e) {
				logger.error("Error opening file", e);
			}
			return;
		}
		
		try {
			//parse out the parts of the date. 
			String standardPath = GeneralUtilities.getFirstPartOfSavePath(true);
			String workingPath = path.substring(standardPath.length());
			String yearString = workingPath.substring(0, workingPath.indexOf("\\"));
			workingPath = workingPath.substring(yearString.length() + 1);
			String monthName = workingPath.substring(0, workingPath.indexOf("\\"));
			workingPath = workingPath.substring(monthName.length() + 1);
			String dayOfMonth = workingPath.substring((workingPath.indexOf("-") + 1), workingPath.indexOf("."));
			
			int year = Integer.parseInt(yearString);
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
			Date month = sdf.parse(monthName);
			int day = Integer.parseInt(dayOfMonth);
			
			//set the calendar object. 
			AppConstants.cal.setTime(month);
			AppConstants.cal.set(Calendar.YEAR, year);
			AppConstants.cal.set(Calendar.DAY_OF_MONTH, day);
			
			//load the data for the entry selected by the calendar. 
			loadData();			
		}catch(Exception e) {
			logger.error("error loading entry from path: " + path, e);
			//load the current entry. 
			//this ensure we know the state of the application, and that the error doesn't trigger something weird. 
			AppConstants.cal.setTime(new Date(System.currentTimeMillis()));
			loadData();
		}
	}
	
	/*
	 * Loads the entry for the given day (current save location based on secondPartOfSavePath vairable) if such a file exists. 
	 */
	private void loadData() {
		try {
			view.getTextArea().setText("");
			secondPartOfSavePath = GeneralUtilities.getSecondPartOfSavePath(AppConstants.cal, false);
			String savePath = GeneralUtilities.getFirstPartOfSavePath(true) + secondPartOfSavePath;
			String filename = GeneralUtilities.getFileName(AppConstants.cal);
			String fullpath = savePath + File.separator + filename;
			File file = new File(fullpath);
			if(file.exists()) {
				StringBuilder entryText = new StringBuilder();
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				while(line != null) {
					entryText.append(line + System.lineSeparator());
					line = br.readLine();
				}
				
				br.close();
				view.getTextArea().setText(entryText.toString());
			}
			
			String currentEntry = GeneralUtilities.dateToString(AppConstants.cal);
			view.getEntryLabel().setText("Entry: " + currentEntry);
			
			view.getProcessMarkupAction().performAction();
			view.getTextArea().requestFocus();
			view.getTextArea().setCaretPosition(view.getTextArea().getDocument().getLength());
		}catch(Exception e) {
			logger.error("Error loading data", e);
		}
	}
	
	private void addActionListners() {
		JFrame frame = view.getMainFrame();
		
		//exit listener
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				save();
				frame.dispose();
				System.exit(0);
			}
		});
		
		view.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//save the current entry; 
				save();
				//modify the calendar and load the data
				AppConstants.cal.add(Calendar.DAY_OF_MONTH, -1);
				//System.out.println("Cal has been set to " + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
				loadData();
			}
		});
		
		view.getForwardButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//save the current entry; 
				save();
				//modify the calendar and load the data
				AppConstants.cal.add(Calendar.DAY_OF_MONTH, 1);
				//System.out.println("Cal has been set to " + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
				loadData();
			}
		});
		
		JLabel entryLabel = view.getEntryLabel();
		entryLabel.addMouseListener(new EntryLabelAction(new FileExplorerController(this)));

		JMenuBar menuBar = view.getMenuBar();
		for(int i = 0; i< menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			if(menu == null) {
				continue;
			}
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if(comp.getName().equalsIgnoreCase(AppConstants.FILE_EXPLORER)) {
						FileExplorerController feController = new FileExplorerController(this);
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								feController.start();
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_SAVE_LOCATION)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

								// Open the save dialog
								int response = fileChooser.showSaveDialog(null);
								if (response == JFileChooser.APPROVE_OPTION) {
									Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
									prefs.put(AppConstants.PREF_SAVE_LOCATION,
											fileChooser.getSelectedFile().getAbsolutePath());
								}
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.MENU_LOG_LOCATION)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

								// Open the save dialog
								int response = fileChooser.showSaveDialog(null);
								if (response == JFileChooser.APPROVE_OPTION) {
									Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
									prefs.put(AppConstants.PREF_LOG_SAVE_LOCATION,
											fileChooser.getSelectedFile().getAbsolutePath());
								}
							}
						});
					}
				}
			}
			
			if (menu.getName().equalsIgnoreCase(AppConstants.FILE_MENU)) {
				Component[] comps = menu.getMenuComponents();
				for (Component comp : comps) {
					if (comp.getName().equalsIgnoreCase(AppConstants.ADD_RESOURCES)) {
						JMenuItem item = (JMenuItem) comp;
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								AddResourceController addResourceController = new AddResourceController();
								addResourceController.start();
							}
						});
					}
				}
			}
		}
		
	}
	
	private void save() {
		JTextArea textArea = view.getTextArea();
		String text = textArea.getText();
		
		try {
			if(text != null && text.isEmpty() == false) {
				String savePath = GeneralUtilities.getFirstPartOfSavePath(true) + secondPartOfSavePath;
				//create the necessary directories
				Path filePath = Paths.get(savePath);
				Files.createDirectories(filePath);
				
				String filename = GeneralUtilities.getFileName(AppConstants.cal);
				BufferedWriter writer = null;
				try {
					File file = new File(savePath + File.separator + filename);
					file.createNewFile();
					writer = new BufferedWriter(new FileWriter(file));
					writer.write(text);
	
				} catch (Exception e) {
					logger.error("error saving. Inner try", e);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
		}catch(Exception e) {
			logger.error("Error saving", e);
		}
	}
}
