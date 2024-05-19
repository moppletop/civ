package com.moppletop.civ.game.tile.generator.pangaea;

import com.moppletop.civ.game.tile.Tile;
import com.moppletop.civ.game.tile.TileDirection;
import com.moppletop.civ.game.tile.TileMap;
import com.moppletop.civ.game.tile.TileType;
import com.moppletop.civ.game.tile.generator.TileMapGenerator;
import com.moppletop.civ.game.tile.generator.TileWorm;
import com.moppletop.civ.world.WorldSettings;

import java.util.Arrays;
import java.util.List;

public class PangaeaMapGenerator extends TileMapGenerator {

    public static void main(String[] args) {
        TileMap tileMap = new PangaeaMapGenerator(new WorldSettings()).nextMap();

        for (Tile[] tiles : tileMap.getMap()) {
            System.out.println(Arrays.toString(tiles));
        }
    }

    public PangaeaMapGenerator(WorldSettings worldSettings) {
        super(worldSettings);
    }

    @Override
    protected void generateShape() {
        int baysPerSide = (landSize * 2 + 1) / worldSettings.getBaysPerSide();

        TileDirection[] withoutNorth = new TileDirection[]{TileDirection.SOUTH, TileDirection.EAST, TileDirection.WEST};
        TileDirection[] withoutSouth = new TileDirection[]{TileDirection.NORTH, TileDirection.EAST, TileDirection.WEST};
        TileDirection[] withoutEast = new TileDirection[]{TileDirection.NORTH, TileDirection.SOUTH, TileDirection.WEST};
        TileDirection[] withoutWest = new TileDirection[]{TileDirection.NORTH, TileDirection.SOUTH, TileDirection.EAST};

        for (int i = -landSize; i <= landSize; i += baysPerSide) {
            generateCoast(-landSize, i, withoutWest);
            generateCoast(landSize, i, withoutEast);
            generateCoast(i, -landSize, withoutNorth);
            generateCoast(i, landSize, withoutSouth);
        }
    }

    private void generateCoast(int x, int z, TileDirection[] directions) {
        int depth = getIntBetween(worldSettings.getMinBayDepth(), worldSettings.getMaxBayDepth());
        Tile startingTile = map.getTile(x, z);

        startingTile.setType(TileType.OCEAN);

        TileWorm worm = new TileWorm(startingTile, random, depth, directions);
        Tile tile;

        while ((tile = worm.next()) != null) {
            tile.setType(TileType.OCEAN);
        }

        List<Tile> path = worm.getPath();
        for (Tile pathTile : path) {
            for (TileDirection direction : TileDirection.ALL) {
                Tile relativePathTile = pathTile.getRelative(direction);

                if (relativePathTile == null ||
                        relativePathTile.isWater() ||
                        Math.abs(relativePathTile.getLocation().getX()) > landSize ||
                        Math.abs(relativePathTile.getLocation().getZ()) > landSize) {
                    continue;
                }

                relativePathTile.setType(TileType.COAST);
            }
        }
    }
}
