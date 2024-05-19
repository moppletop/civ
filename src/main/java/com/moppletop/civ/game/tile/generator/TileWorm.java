package com.moppletop.civ.game.tile.generator;

import com.moppletop.civ.game.tile.Tile;
import com.moppletop.civ.game.tile.TileDirection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileWorm {

    private final Random random;
    private final int maximum;
    private final TileDirection[] directionOptions;
    @Getter
    private final List<Tile> path;

    public TileWorm(Tile start, Random random, int maximum, TileDirection[] directionOptions) {
        this.random = random;
        this.maximum = maximum;
        this.directionOptions = directionOptions;
        this.path = new ArrayList<>();
        path.add(start);
    }

    public boolean isValid(Tile tile) {
        return tile != null;
    }

    public Tile next() {
        if (path.size() == maximum) {
            return null;
        }

        Tile current = path.get(path.size() - 1);
        List<Tile> options = new ArrayList<>(directionOptions.length);

        for (TileDirection direction : directionOptions) {
            Tile relative = current.getRelative(direction);
            if (!path.contains(relative) && isValid(relative)) {
                options.add(relative);
            }
        }

        if (options.isEmpty()) {
            return null;
        }

        Tile tile = options.get(random.nextInt(options.size()));
        path.add(tile);
        return tile;
    }

}
