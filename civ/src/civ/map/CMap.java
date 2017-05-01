package civ.map;

import java.awt.Dimension;

import civ.util.CProperties;


public class CMap {

	enum Terrain {
		LAND, WATER
	}

	private Terrain[][] _terrain;

	private CMap() {
		String cls = CProperties.getInstance().getProperty("map.generator");
		int w = CProperties.getInstance().getPropertyAsInt("map.width");
		int h = CProperties.getInstance().getPropertyAsInt("map.height");
		_terrain = new Terrain[w][h];
		try {
			IMapGenerator mg = (IMapGenerator) Class.forName(cls).newInstance();
			mg.fill(_terrain);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Dimension getSize() {
		return new Dimension(_terrain.length, _terrain[0].length);
	}
}
