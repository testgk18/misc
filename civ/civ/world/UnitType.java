package civ.world;

import java.awt.Image;

public class UnitType {

	private String _name;
	private double _attack;
	private double _defense;
	private double _energy;

	public void setName(String name) {
		_name = name;
	}

	public void setAttack(double attack) {
		_attack = attack;
	}

	public void setDefense(double defense) {
		_defense = defense;
	}

	public void setEnergy(double energy) {
		_energy = energy;
	}

	public double getEnergy() {
		return _energy;
	}

}
