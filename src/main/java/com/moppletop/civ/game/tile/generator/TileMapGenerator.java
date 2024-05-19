package com.moppletop.civ.game.tile.generator;

import com.moppletop.civ.game.tile.*;
import com.moppletop.civ.util.FastNoiseLite;
import com.moppletop.civ.world.WorldSettings;

import java.util.Random;

public abstract class TileMapGenerator {

    protected final WorldSettings worldSettings;
    protected final Random random;

    protected final TileTypeHelper tileTypeHelper;

    protected final int fullSize;
    protected final int landSize;

    protected TileMap map;

    public TileMapGenerator(WorldSettings worldSettings) {
        this.worldSettings = worldSettings;
        this.random = new Random(worldSettings.getSeed());

        this.tileTypeHelper = new TileTypeHelper(worldSettings);

        this.fullSize = worldSettings.getSize();
        this.landSize = worldSettings.getSize() - worldSettings.getSurroundingOceanSize();
    }

    public final TileMap nextMap() {
        this.map = new TileMap(worldSettings);

        generateShape();

        generateTypes();

        generateTopography();

        postProcess();

        return map;
    }

    protected abstract void generateShape();

    protected void generateTypes() {
        FastNoiseLite biomeNoiseGenerator = getNoiseGenerator();
        biomeNoiseGenerator.SetFrequency(0.1F); // TODO magic

        for (int x = -landSize; x <= landSize; x++) {
            for (int z = -landSize; z <= landSize; z++) {
                Tile tile = map.getTile(x, z);

                if (tile.getType() != null) {
                    continue;
                }

                TileType type = tileTypeHelper.getTileType(biomeNoiseGenerator, x, z);

                tile.setType(type);
            }
        }
    }

    protected void generateTopography() {
        int betweenHills = worldSettings.getBetweenHills();

        TileZoner zoner = new TileZoner(random, map.getTilesFiltered(tile -> tile != null && tile.isLand()));
        Tile tile;

        while ((tile = zoner.next()) != null) {
            zoner.removeTilesWithin(tile.getLocation(), betweenHills);

            TileHeight height = random.nextFloat() < worldSettings.getMountainChance() ? TileHeight.MOUNTAIN : TileHeight.HILL;

            tile.setHeight(TileHeight.HILL);

            TileWorm worm = new TileWorm(tile, random, 10, TileDirection.ALL);
            float threshold = worldSettings.getHillSpread();

            while ((tile = worm.next()) != null) {
                tile.setHeight(height);

                if (height == TileHeight.MOUNTAIN) {
                    for (TileDirection direction : TileDirection.ALL) {
                        Tile relativeTile = tile.getRelative(direction);

                        if (relativeTile == null || relativeTile.getHeight() == TileHeight.MOUNTAIN) {
                            continue;
                        }

                        relativeTile.setHeight(TileHeight.HILL);
                    }
                }

                if (random.nextFloat() < threshold) {
                    break;
                }

                threshold /= 2;
            }
        }
    }

    protected void postProcess() {
        int fullSize = worldSettings.getSize();

        for (int x = -fullSize; x <= fullSize; x++) {
            for (int z = -fullSize; z <= fullSize; z++) {
                Tile tile = map.getTile(x, z);

                if (tile.getType() == null) {
                    tile.setType(TileType.OCEAN);
                }

                if (tile.getHeight() == null) {
                    tile.setHeight(TileHeight.FLAT);
                }
            }
        }
    }

    protected FastNoiseLite getNoiseGenerator() {
        return new FastNoiseLite((int) worldSettings.getSeed());
    }

//    protected Tile getRandomTile() {
//        return map.getTile(getIntBetween(-landSize, landSize), getIntBetween(-landSize, landSize));
//    }

    // Inclusive
    protected final int getIntBetween(int min, int max) {
        int bound = max - min + 1;
        return min + random.nextInt(bound);
    }

    protected final <T> T getRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }
}
