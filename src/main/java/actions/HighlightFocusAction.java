package actions;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HighlightFocusAction implements FocusListener{

	@Override
	public void focusGained(FocusEvent e) {
		Component component = e.getComponent();
		if(component.getClass().equals(JTextField.class)) {
			JTextField field = (JTextField) component;
			field.select(0, field.getText().length());
		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		Component component = e.getComponent();
		if(component.getClass().equals(JTextField.class)) {
			JTextField field = (JTextField) component;
			field.select(0, 0);
		}
	}

}
