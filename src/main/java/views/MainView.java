package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import actions.ProcessmarkupAction;
import journal.AppConstants;
import utilities.GeneralUtilities;

public class MainView {
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private JLabel entryLabel;
	
	private JTextArea textArea;
	private JButton backButton;
	private JButton forwardButton;	
	
	private ProcessmarkupAction processMarkupAction;

	public MainView() {		
		mainFrame = new JFrame();
		mainFrame.setTitle("Journal");
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setResizable(true);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		backButton = new JButton("<");
		mainPanel.add(backButton, BorderLayout.LINE_START);
		forwardButton = new JButton(">");
		mainPanel.add(forwardButton, BorderLayout.LINE_END);
		
		JPanel editorPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(editorPanel, BoxLayout.Y_AXIS);
		editorPanel.setLayout(boxLayout);
		
		textArea = new JTextArea(5, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        textArea.setFont(font);
        
        JScrollPane editor = new JScrollPane(textArea,
		         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editor.getVerticalScrollBar().setUnitIncrement(16);
        
        editorPanel.add(editor);
        editorPanel.setPreferredSize(new Dimension(5000, 5000));
		
        JPanel viewingPanel = new JPanel();
		BoxLayout boxLayout2 = new BoxLayout(viewingPanel, BoxLayout.Y_AXIS);
		viewingPanel.setLayout(boxLayout2);
		
		JEditorPane viewerPane = new JEditorPane();
		viewerPane.setContentType( "text/html" );
		viewerPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
		
		JScrollPane viewerScrollViewer = new JScrollPane(viewerPane,
		         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		viewerScrollViewer.getVerticalScrollBar().setUnitIncrement(16);
		viewingPanel.add(viewerScrollViewer);
		viewingPanel.setPreferredSize(new Dimension(5000, 5000));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorPanel, viewerScrollViewer);
		splitPane.setResizeWeight(0.5);
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		processMarkupAction = new ProcessmarkupAction(textArea, viewerPane);
		textArea.registerKeyboardAction(processMarkupAction, KeyStroke.getKeyStroke(KeyEvent.VK_U,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		textArea.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String header = GeneralUtilities.majorHeader(Calendar.getInstance());
            	textArea.insert(header, textArea.getCaretPosition());
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		textArea.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String header = GeneralUtilities.minorHeader(Calendar.getInstance());
            	textArea.insert(header, textArea.getCaretPosition());
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.ALT_DOWN_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW );
		
		addMenu();
		
		mainFrame.add(mainPanel);
		
		//TODO: List:
		//Basics:
		//advanced. 
		//improve look and feel
		//can we add a spell check?
		//can we add navigation shortcuts (ctr + F). 
		//need to learn markdown better so we can work on how we can search and extract data.  
 
	}
	
	private void addMenu() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu(AppConstants.FILE_MENU);
		menu.setName(AppConstants.FILE_MENU);
		
		JMenuItem explorer = new JMenuItem(AppConstants.FILE_EXPLORER);
		explorer.setName(AppConstants.FILE_EXPLORER);
		menu.add(explorer);
		
		JMenuItem saveLocation = new JMenuItem(AppConstants.MENU_SAVE_LOCATION);
		saveLocation.setName(AppConstants.MENU_SAVE_LOCATION);
		menu.add(saveLocation);
		
		JMenuItem logLocation = new JMenuItem(AppConstants.MENU_LOG_LOCATION);
		logLocation.setName(AppConstants.MENU_LOG_LOCATION);
		menu.add(logLocation);
		
		JMenuItem addResources = new JMenuItem(AppConstants.ADD_RESOURCES);
		addResources.setName(AppConstants.ADD_RESOURCES);
		menu.add(addResources);
		
		entryLabel = new JLabel();
		entryLabel.setBorder(new EmptyBorder(0, 12, 0, 0));
		
		menuBar.add(menu);
		menuBar.add(entryLabel);
		mainFrame.setJMenuBar(menuBar);
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public JLabel getEntryLabel() {
		return entryLabel;
	}

	public void setEntryLabel(JLabel entryLabel) {
		this.entryLabel = entryLabel;
	}

	public JButton getBackButton() {
		return backButton;
	}

	public void setBackButton(JButton backButton) {
		this.backButton = backButton;
	}

	public JButton getForwardButton() {
		return forwardButton;
	}

	public void setForwardButton(JButton forwardButton) {
		this.forwardButton = forwardButton;
	}

	public ProcessmarkupAction getProcessMarkupAction() {
		return processMarkupAction;
	}
}
