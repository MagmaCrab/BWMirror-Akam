package economy;

import java.util.List;
import java.util.ArrayList;

import bwapi.Color;
import bwapi.Unit;
import bwapi.UnitType;
import main.Bot;
import maps.ConfigMap;
import maps.EntityMap;
import util.Manager;


public class BaseManager implements Manager{
	private Unit headquarter;
	
	private List<Unit> workers;
	private List<Unit> minerals;
	private List<Unit> geysers;
	
	private int maxWorkers;
	private int gatherRadius;
	private int nextMineral;
	
	public BaseManager(Unit headquarter) {
		this.headquarter = headquarter;
		this.gatherRadius = ConfigMap.get().readInt("gatheringradius");
		this.maxWorkers = 10;
		this.nextMineral = 0;
		
		this.workers = new ArrayList<>();
		this.minerals = new ArrayList<>();
		this.geysers = new ArrayList<>();
	}
	
	@Override
	public void onStart() {
		findResources();
	}

	@Override
	public void onFrame() {
		// find unknown workers in range
		for (Unit unit : Bot.self.getUnits()) {
			if(unit.getDistance(headquarter) < gatherRadius && 
			   unit.getType().isWorker() && !workers.contains(unit)) {
				workers.add(unit);
			}
		}
			
		// allocate idle workers
		for(Unit worker : workers) {
			if(worker.isIdle() && !worker.isBeingConstructed()) {
                worker.gather(minerals.get(nextMineral), false);
                nextMineral++;
                if(nextMineral >= minerals.size()) {
                	nextMineral = 0;
                }
			}
		}
		
		// create workers
		if (headquarter.getTrainingQueue().isEmpty() && 
			Bot.self.minerals() >= 50 &&
			workers.size() < maxWorkers) {
			headquarter.train(EntityMap.get().read("worker"));
        }
	}
	
	@Override
	public void draw() {
		Bot.game.drawCircleMap(
				headquarter.getX(), 
				headquarter.getY(), 
				gatherRadius, 
				Color.Green);
		
		Bot.game.drawTextMap(
				headquarter.getX() - 20, 
				headquarter.getY() - 100, 
				"workers: " + workers.size() + "/" + maxWorkers);
	}
	
	public void findResources() {
		for (Unit resource : Bot.game.neutral().getUnits()) {
			if(resource.getDistance(headquarter) < gatherRadius) {
				if(resource.getType().isMineralField()) {
					minerals.add(resource);
				} 
				else if (resource.getType() == UnitType.Resource_Vespene_Geyser) {
					geysers.add(resource);
				}
			}
		}
		
		maxWorkers = minerals.size()*2;
	}
	
	public Unit getHeadquarter() { return headquarter; }

	public List<Unit> getWorkers() { return workers; }

	public List<Unit> getMinerals() { return minerals; }

	public List<Unit> getGeysers() { return geysers; }

	
}
