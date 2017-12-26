package economy;

import java.util.ArrayList;
import java.util.List;

import bwapi.Unit;
import main.Bot;
import maps.EntityMap;
import util.Manager;

/*
 * Gathering Manager
 * 
 * Makes sure resources get optimally harvested with a strong workforce
 */

public class GatheringManager implements Manager{
	private List<BaseManager> bases;
	
	public GatheringManager() {
		bases = new ArrayList<>();
		
	}
	
	@Override
	public void onStart() {
		for (Unit myUnit : Bot.self.getUnits()) {
			if (myUnit.getType() == EntityMap.get().read("headquarter")) {
				bases.add(new BaseManager(myUnit));
			}
		}
		
		bases.forEach(b -> b.onStart());
	}

	@Override
	public void onFrame() {
		bases.forEach(b -> b.onFrame());
	}

	@Override
	public void draw() {
		bases.forEach(b -> b.draw());
	}
}
