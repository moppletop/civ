package com.moppletop.civ.game.tile.generator;

import com.moppletop.civ.game.TileLocation;
import com.moppletop.civ.game.tile.Tile;

import java.util.List;
import java.util.Random;

public class TileZoner {

    private final Random random;
    private final List<Tile> tiles;

    public TileZoner(Random random, List<Tile> tiles) {
        this.random = random;
        this.tiles = tiles;
    }

    public Tile next() {
        if (tiles.isEmpty()) {
            return null;
        }

        return tiles.get(random.nextInt(tiles.size()));
    }

    public void removeTilesWithin(TileLocation location, int distance) {
        int distanceSquared = distance * distance;

        tiles.removeIf(tile -> tile.getLocation().distanceSquared(location) < distanceSquared);
    }
}
