package com.moppletop.civ.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "name")
public class Team
{

    private final String name;
    private final List<UUID> members = new ArrayList<>();

}
