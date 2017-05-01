package civ.map;

import civ.map.CMap.Terrain;

public class SimpleMapGenerator implements IMapGenerator {

	@Override
	public void fill(Terrain[][] terrain) {

		//Dimension size = map.getSize();
		for (int x = 0; x < terrain.length; ++x) {
			for (int y = 0; y < terrain[x].length; ++y) {
				terrain[x][y] = Terrain.values()[(int) Math.random()];
			}
		}

	}

}
