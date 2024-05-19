package com.moppletop.civ.world;

import lombok.Data;

@Data
public class WorldSettings {

    long seed = 1;

    int size = 10;
    int surroundingOceanSize = 1;

    int baysPerSide = 3;
    int minBayDepth = 3;
    int maxBayDepth = 8;

    int betweenHills = 4;
    float hillSpread = 0.5F;
    float mountainChance = 0.3F;

}
