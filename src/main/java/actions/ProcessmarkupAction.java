package actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public class ProcessmarkupAction implements ActionListener{
	
	private JTextArea textArea; 
	private JEditorPane viewerPane;
	
	private Parser parser;
    private HtmlRenderer renderer;
	
	public ProcessmarkupAction(JTextArea textArea, JEditorPane viewerPane) {
		this.textArea = textArea;
		this.viewerPane = viewerPane;
		
		parser = Parser.builder().build();
		renderer = HtmlRenderer.builder().build();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		performAction();
	}
	
	public void performAction() {
		String text = this.textArea.getText();
		Node document = parser.parse(text);
        String html = renderer.render(document);
        
        //TODO make the default text size configurable. 
		viewerPane.setText("<html><head> <style>p {font-size: 12px;}</style></head><body>" + html + "</body></html>");
	}

}
