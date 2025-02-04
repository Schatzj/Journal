package logging;

import java.io.File;
import java.net.URI;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import journal.AppConstants;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(1)
public class CustomConfigurationFactory extends ConfigurationFactory {
	
//	//https://logging.apache.org/log4j/2.x/manual/customconfig.html
//	//https://www.baeldung.com/log4j2-programmatic-config
	//https://stackoverflow.com/questions/14862770/log4j2-assigning-file-appender-filename-at-runtime 

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setStatusLevel(Level.ERROR);
        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL).
            addAttribute("level", Level.DEBUG));
        
        Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
        String defaultPath = System.getProperty("user.home");
		String saveLocation = prefs.get(AppConstants.PREF_LOG_SAVE_LOCATION, defaultPath);
		String logSavepath = saveLocation + File.separator + AppConstants.TOP_LEVEL_DIRECTORY_NAME + File.separator + "logs" + File.separator + "Journal-log.log";
		
        AppenderComponentBuilder file = builder.newAppender("log", "File"); 
        file.addAttribute("fileName", logSavepath);

		LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
		standard.addAttribute("pattern", "%d %C{5} | %msg%n%throwable");

		file.add(standard);
		builder.add(file);
		
		RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
		rootLogger.add(builder.newAppenderRef("log"));
		builder.add(rootLogger);
		
		LoggerComponentBuilder logger = builder.newLogger("controller", Level.DEBUG);
		logger.add(builder.newAppenderRef("log"));
		logger.addAttribute("additivity", false);

		builder.add(logger);
        
//		try {
//			builder.writeXmlConfiguration(System.out);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }
}