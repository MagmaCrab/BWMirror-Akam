package util;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesReader {
	private Properties properties = new Properties();
	private InputStream stream;
		
	public PropertiesReader(String fileName) {
		try {
			stream = getClass().getClassLoader().getResourceAsStream(fileName + ".properties") ;
			properties.load(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public String read(String value) {
		return properties.getProperty(value);
	}
	
	public int readInt(String value) {
		return Integer.valueOf(properties.getProperty(value));
	}
	
	public Set<String> getAllPropertyNames(){
		return properties.stringPropertyNames();
	}
}
