package com.moppletop.civ.game.tile;

import com.moppletop.civ.game.TileLocation;
import com.moppletop.civ.world.WorldSettings;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class TileMap {

    private final int size;

    private final Tile[][] map;

    public TileMap(WorldSettings worldSettings) {
        this.size = worldSettings.getSize();

        int arraySize = worldSettings.getSize() * 2 + 1;
        this.map = new Tile[arraySize][arraySize];
    }

    public Tile getTile(int x, int z) {
        if (x < -size || z < -size || x > size || z > size) {
            return null;
        }

        Tile tile = map[x + size][z + size];

        if (tile == null) {
            tile = new Tile(this, new TileLocation(x, z));
            map[x + size][z + size] = tile;
        }

        return tile;
    }

    public List<Tile> getTilesFiltered(Predicate<Tile> filter) {
        return Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(filter)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
