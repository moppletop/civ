package com.moppletop.civ.game.tile;

import com.moppletop.civ.game.TileLocation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile {

    private final TileMap tileMap;
    private final TileLocation location;

    private TileType type;
    private TileHeight height;

    public Tile(TileMap tileMap, TileLocation location) {
        this.tileMap = tileMap;
        this.location = location;
    }

    public Tile getRelative(TileDirection direction) {
        return getRelative(direction.getXMod(), direction.getZMod());
    }

    public Tile getRelative(int x, int z) {
        return tileMap.getTile(location.getX() + x, location.getZ() + z);
    }

    public boolean isLand() {
        return type != TileType.OCEAN && type != TileType.COAST;
    }

    public boolean isWater() {
        return type == TileType.OCEAN || type == TileType.COAST;
    }

    @Override
    public String toString() {
        return type.toString().charAt(0) + "" + height.toString().charAt(0);
    }
}
