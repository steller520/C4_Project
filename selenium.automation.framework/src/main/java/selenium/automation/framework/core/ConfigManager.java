package selenium.automation.framework.core;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties props = new Properties(); 
    static {
        try {
        InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties");
        if(input == null) {
            throw new RuntimeException("config.properties file not found in resources folder");
        }
        props.load(input);
        input.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties file: " + e.getMessage())  ;
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static Boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(props.getProperty(key));
    }
    public static Integer getIntProperty(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
    
}
