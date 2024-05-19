package com.moppletop.civ.game.tile.generator;

import com.moppletop.civ.game.tile.TileType;
import com.moppletop.civ.game.tile.generator.exception.TileMapGeneratorException;
import com.moppletop.civ.util.FastNoiseLite;
import com.moppletop.civ.world.WorldSettings;

import java.util.ArrayList;
import java.util.List;

public class TileTypeHelper {

    private final WorldSettings worldSettings;

    private final List<TemperatureMapping> temperatureMappings;

    public TileTypeHelper(WorldSettings worldSettings) {
        this.worldSettings = worldSettings;

        this.temperatureMappings = new ArrayList<>(TileType.values().length);

        temperatureMappings.add(new TemperatureMapping(-0.8F, TileType.SNOW));
        temperatureMappings.add(new TemperatureMapping(-0.6F, TileType.TUNDRA));
        temperatureMappings.add(new TemperatureMapping(0, TileType.PLAINS));
        temperatureMappings.add(new TemperatureMapping(0.6F, TileType.GRASSLAND));
        temperatureMappings.add(new TemperatureMapping(1, TileType.DESERT));
    }

    public TileType getTileType(FastNoiseLite biomeNoiseGenerator, int x, int z) {
        float noise = biomeNoiseGenerator.GetNoise(x, z);

        for (TemperatureMapping temperatureMapping : temperatureMappings) {
            if (noise <= temperatureMapping.threshold()) {
                return temperatureMapping.tileType();
            }
        }

        throw new TileMapGeneratorException("No temperature mapping found for " + noise + " using " + temperatureMappings);
    }

    record TemperatureMapping(float threshold, TileType tileType) {
    }

}
