package com.moppletop.civ;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.moppletop.civ.game.TileLocation;
import com.moppletop.civ.game.ViewState;
import com.moppletop.civ.game.tile.TileMap;
import com.moppletop.civ.game.tile.generator.pangaea.PangaeaMapGenerator;
import com.moppletop.civ.world.WorldGen;
import com.moppletop.civ.world.WorldSettings;
import com.moppletop.civ.world.WorldViewManager;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CivPlugin extends JavaPlugin implements Listener {
    private ProtocolManager protocolManager;
    private WorldViewManager worldViewManager;

    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();

//        worldViewManager = new WorldViewManager(this);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();

        switch (message) {
            case "/world":
                event.setCancelled(true);
                WorldSettings worldSettings = new WorldSettings();
                PangaeaMapGenerator generator = new PangaeaMapGenerator(worldSettings);
                TileMap tileMap = generator.nextMap();
                new WorldGen(worldSettings, tileMap).generate(event.getPlayer());
                break;
            case "/a":
                event.setCancelled(true);
                worldViewManager.update(event.getPlayer(), new TileLocation(event.getPlayer().getLocation().add(16, 0, 0)), ViewState.UNSEEN);
                break;
            case "/b":
                event.setCancelled(true);
                worldViewManager.update(event.getPlayer(), new TileLocation(event.getPlayer().getLocation().add(16, 0, 0)), ViewState.SEEN);
                break;
            case "/c":
                event.setCancelled(true);
                worldViewManager.update(event.getPlayer(), new TileLocation(event.getPlayer().getLocation().add(16, 0, 0)), ViewState.SEEING);
                break;
        }
    }
}
