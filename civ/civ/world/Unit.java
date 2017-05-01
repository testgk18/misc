package civ.world;

public abstract class Unit {

	
	private int _id;
	// private Nation _nation; ?? circular reference ??
	private UnitType _type;
	private double _energy;

	public Unit(int id, UnitType type) {
		_id = id;
		_type = type;
		_energy = type.getEnergy();
	}

	public void refreshEnergy() {
		_energy = _type.getEnergy();
	}
}
