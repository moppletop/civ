package com.moppletop.civ.game;

import lombok.Value;
import org.bukkit.Chunk;
import org.bukkit.Location;

@Value
public class TileLocation
{
    int x, z;

    public TileLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public TileLocation(Location location) {
        Chunk chunk = location.getChunk();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public int distanceSquared(TileLocation other) {
        return square(x - other.x) +  square(z - other.z);
    }

    public static int square(int a) {
        return a * a;
    }
}
