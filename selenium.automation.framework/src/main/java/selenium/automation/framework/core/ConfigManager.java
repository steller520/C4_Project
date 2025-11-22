package selenium.automation.framework.core;

import java.io.InputStream;
import java.util.Properties;

/**
 * Manages loading and accessing configuration properties from the `config.properties` file.
 * This utility class ensures that properties are loaded once and are easily accessible throughout the framework.
 */
public class ConfigManager {
    // Properties object to store configuration data
    private static Properties props = new Properties(); 
    
    // Static initializer block to load properties from the config file when the class is loaded
    static {
        try {
            // Load the properties file from the classpath resources
            InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties");
            if(input == null) {
                // Throw an exception if the properties file is not found
                throw new RuntimeException("config.properties file not found in resources folder");
            }
            // Load properties from the input stream
            props.load(input);
            // Close the input stream
            input.close();
        } catch (Exception e) {
            // Throw a runtime exception if loading fails
            throw new RuntimeException("Failed to load config.properties file: " + e.getMessage())  ;
        }
    }

    /**
     * Retrieves a property value as a String.
     *
     * @param key The key of the property to retrieve.
     * @return The property value as a String.
     */
    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Retrieves a property value as a Boolean.
     *
     * @param key The key of the property to retrieve.
     * @return The property value as a Boolean.
     */
    public static Boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(props.getProperty(key));
    }

    /**
     * Retrieves a property value as an Integer.
     *
     * @param key The key of the property to retrieve.
     * @return The property value as an Integer.
     */
    public static Integer getIntProperty(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
    
}
