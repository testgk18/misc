package civ.world;

import java.util.Vector;

import civ.map.CMap;
import civ.util.CProperties;

public class World {

	private CMap _map;

	private Vector<Nation> _nations;

	private UnitType[] _unitTypes;

	private Unit[] _unitRegistry;
	
	public World() {
		int maxUnits=CProperties.getInstance().getPropertyAsInt("max.units");
		_unitRegistry =new Unit[maxUnits];
	}
	
	
	
}
