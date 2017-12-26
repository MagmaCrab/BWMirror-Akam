package maps;

import java.util.HashMap;

import bwapi.UnitType;
import util.PropertiesReader;
import util.Race;

/*
 * Entity Wrapper Map
 * 
 * Wrapper abstraction layer for the most basic entities for each race
 * Most of the tier one entities are here
 */

public class EntityMap {
	private Race race;
	
	private PropertiesReader reader;
	private HashMap<String, UnitType> map;
	
	private static EntityMap instance;
	
	public static EntityMap get() {
		if(instance == null) {
			instance = new EntityMap();
		}
		return instance;
	}
	
	public EntityMap(){
		this.race = Race.valueOf(ConfigMap.get().read("race").toUpperCase());
		this.reader = new PropertiesReader(race.getName());
		this.map = new HashMap<>();
				
		try {
			for(String key : reader.getAllPropertyNames()) {
				String value = reader.read(key);
				map.put(key, (UnitType) UnitType.class.getField(value).get(UnitType.class));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public UnitType read(String key) {
		return  map.get(key);
	}
	
	public String getRace() {
		return race.getName();
	}
}


