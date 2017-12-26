package util;

public enum Race {
	TERRAN("Terran"), 
	PROTOSS("Protoss"),
	ZERG("Zerg");
	
	private final String name;
	
	Race(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
