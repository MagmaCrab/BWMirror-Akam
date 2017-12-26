package maps;

import java.util.HashMap;

import util.PropertiesReader;

public class ConfigMap {
	private PropertiesReader reader;
	private HashMap<String, String> map;
	
	private static ConfigMap instance;
	
	public static ConfigMap get() {
		if(instance == null) {
			instance = new ConfigMap();
		}
		return instance;
	}
	
	private ConfigMap() {
		this.reader = new PropertiesReader("config");
		this.map = new HashMap<>();
		
		try {
			for(String key : reader.getAllPropertyNames()) {
				map.put(key, reader.read(key));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public String read(String key) {
		return map.get(key);
	}
	
	public int readInt(String key) {
		return Integer.parseInt(map.get(key));
	}
}
