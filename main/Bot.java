package main;

import java.util.ArrayList;

import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import economy.GatheringManager;
import maps.ConfigMap;
import maps.EntityMap;
import util.Manager;

public class Bot extends DefaultBWListener {
	
	public static Mirror mirror;
    public static Game game;
    public static Player self;
    
    private ArrayList<Manager> managers;

    public void run() {
    	mirror = new Mirror();
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
       // System.out.println("New unit discovered " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();
        managers = new ArrayList<>();

        BWTA.readMap();
        BWTA.analyze();
        
        managers.add(new GatheringManager());

		game.setLocalSpeed(ConfigMap.get().readInt("gamespeed"));
		
		managers.forEach(m -> m.onStart());
    }

    @Override
    public void onFrame() {
    	managers.forEach(m -> m.draw());
    	managers.forEach(m -> m.onFrame());
    	
    	game.drawTextScreen(10, 230, self.supplyUsed()/2 + "/" + self.supplyTotal()/2);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
        
        //if we're running out of supply and have enough minerals ...
        if (self.minerals() >= 150) {
        	//iterate over units to find a worker
        	for (Unit myUnit : self.getUnits()) {
        		if (myUnit.getType() == EntityMap.get().read("worker")) {
        			//get a nice place to build a supply depot
        			TilePosition buildTile =
        				getBuildTile(myUnit, UnitType.Protoss_Pylon, self.getStartLocation());
        			//and, if found, send the worker to build it (and leave others alone - break;)
        			if (buildTile != null) {
        				myUnit.build(UnitType.Protoss_Pylon, buildTile);
        				break;
        			}
        		}
        	}
        }
    }
    
 // Returns a suitable TilePosition to build a given building type near
    // specified TilePosition aroundTile, or null if not found. (builder parameter is our worker)
    public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile) {
    	TilePosition ret = null;
    	int maxDist = 2;
    	int stopDist = 10;

    	while ((maxDist < stopDist) && (ret == null)) {
    		for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
    			for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
    				if (game.canBuildHere(new TilePosition(i,j), buildingType, builder, false)) {
    					// units that are blocking the tile
    					boolean unitsInWay = false;
    					for (Unit u : game.getAllUnits()) {
    						if (u.getID() == builder.getID()) continue;
    						if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
    					}
    					if (!unitsInWay) {
    						return new TilePosition(i, j);
    					}
    				}
    			}
    		}
    		maxDist += 2;
    	}

    	if (ret == null) game.printf("Unable to find suitable build position for "+buildingType.toString());
    	return ret;
    }

}
