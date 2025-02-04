package journal;

import org.apache.logging.log4j.core.config.ConfigurationFactory;

import controllers.MainController;
import logging.CustomConfigurationFactory;
import views.MainView;

public class Application {
	
	public static void main(String[] args) {
		
		ConfigurationFactory.setConfigurationFactory(new CustomConfigurationFactory());
		
		MainView view = new MainView();
    	MainController controller = new MainController(view);
    	controller.startMainView();
	}
}
