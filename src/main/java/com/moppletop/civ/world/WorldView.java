package com.moppletop.civ.world;

import com.moppletop.civ.game.TileLocation;
import com.moppletop.civ.game.TileView;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class WorldView {

    List<TileView> tileViews = new ArrayList<>();

    public TileView getChunk(TileLocation location) {
        for (TileView tileView : tileViews) {
            if (location.equals(tileView.getLocation())) {
                return tileView;
            }
        }

        return null;
    }

    public TileView getChunk(int x, int z) {
        for (TileView tileView : tileViews) {
            if (tileView.getLocation().getX() == x && tileView.getLocation().getZ() == z) {
                return tileView;
            }
        }

        return null;
    }

}
