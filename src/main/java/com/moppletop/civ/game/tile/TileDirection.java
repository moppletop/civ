package com.moppletop.civ.game.tile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TileDirection {

    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),

    NORTH_EAST(NORTH, EAST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    NORTH_WEST(NORTH, WEST);

    public static final TileDirection[] CARDINAL = {
            NORTH,
            SOUTH,
            EAST,
            WEST
    };

    public static final TileDirection[] ALL = values();

    private final int xMod, zMod;

    TileDirection(TileDirection a, TileDirection b) {
        this(a.xMod + b.xMod, a.zMod + b.zMod);
    }

    public TileDirection getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case NORTH_EAST -> SOUTH_WEST;
            case SOUTH_EAST -> NORTH_WEST;
            case SOUTH_WEST -> NORTH_EAST;
            case NORTH_WEST -> SOUTH_EAST;
        };
    }
}
