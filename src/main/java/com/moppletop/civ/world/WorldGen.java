package com.moppletop.civ.world;

import com.moppletop.civ.game.tile.Tile;
import com.moppletop.civ.game.tile.TileMap;
import com.moppletop.civ.game.tile.TileType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;
import java.util.UUID;

public class WorldGen extends ChunkGenerator {

    public void generate(Player player) {
        String name = UUID.randomUUID().toString();

        World world = new WorldCreator(name)
                .type(WorldType.FLAT)
                .seed(worldSettings.getSeed())
                .generator(this)
                .createWorld();

        player.teleport(world.getSpawnLocation());
    }

    private final WorldSettings worldSettings;
    private final TileMap tileMap;

    public WorldGen(WorldSettings worldSettings, TileMap tileMap) {
        this.worldSettings = worldSettings;
        this.tileMap = tileMap;
    }


    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        chunkData.setRegion(0, chunkData.getMinHeight(), 0, 16, chunkData.getMinHeight() + 1, 16, Material.BEDROCK);
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        int size = tileMap.getSize();

        if (chunkX > size || chunkZ > size || chunkX < -size || chunkZ < -size) {
            generateOutBounds(chunkX, chunkZ, chunkData);
        } else {
            generateInBound(chunkX, chunkZ, chunkData);
        }
    }

    private void generateOutBounds(int chunkX, int chunkZ, ChunkData chunkData) {
        int distance = Math.max(Math.abs(chunkX), Math.abs(chunkZ)) + 16;
        int y = Math.max(chunkData.getMinHeight(), -distance + tileMap.getSize()) + 1;

        chunkData.setRegion(0, chunkData.getMinHeight() + 1, 0, 16, y, 16, Material.STONE);

        if (y <= 0) {
            chunkData.setRegion(0, y, 0, 16, 0, 16, Material.WATER);
        }
    }

    private void generateInBound(int chunkX, int chunkZ, ChunkData chunkData) {
        Tile tile = tileMap.getTile(chunkX, chunkZ);

        if (tile.isWater()) {
            generateSea(tile, chunkData);
        } else {
            generateLand(tile, chunkData);
        }
    }

    private void generateSea(Tile tile, ChunkData chunkData) {
        int floor = tile.getType() == TileType.COAST ? -2 : -8;

        chunkData.setRegion(0, chunkData.getMinHeight() + 1, 0, 16, floor, 16, Material.DIRT);
        chunkData.setRegion(0, floor, 0, 16, 0, 16, Material.WATER);
    }

    private void generateLand(Tile tile, ChunkData chunkData) {
        Material material = switch (tile.getType()) {
            case SNOW -> Material.SNOW_BLOCK;
            case TUNDRA -> Material.STONE;
            case DESERT -> Material.SAND;
            case GRASSLAND -> Material.GRASS_BLOCK;
            case PLAINS -> Material.COARSE_DIRT;
            default -> null;
        };

        if (material == null) {
            return;
        }

        chunkData.setRegion(0, chunkData.getMinHeight() + 1, 0, 16, 0, 16, Material.STONE);

        int maxY = switch (tile.getHeight()) {
            case FLAT -> 1;
            case HILL -> 4;
            case MOUNTAIN -> 12;
        };

        chunkData.setRegion(0, 0, 0, 16, maxY, 16, material);
    }


    //        for (int y = chunkData.getMinHeight(); y < 0 && y < chunkData.getMaxHeight(); y++) {
//            for (int x = 0; x < 16; x++) {
//                for (int z = 0; z < 16; z++) {
//                    float noise2 = terrainNoise.GetNoise(x + (chunkX * 16), z + (chunkZ * 16)) + (detailNoise.GetNoise(x + (chunkX * 16), z + (chunkZ * 16)) / 5);
//
//                    if (10 * noise2 > y) {
//                        chunkData.setBlock(x, y, z, Material.STONE);
//                    }
//                }
//            }
//        }
}
