package com.moppletop.civ.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "location")
public class TileView
{
    final TileLocation location;
    ViewState state;

}
