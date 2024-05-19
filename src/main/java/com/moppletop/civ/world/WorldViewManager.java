package com.moppletop.civ.world;

import com.moppletop.civ.CivPlugin;
import com.moppletop.civ.game.TileLocation;
import com.moppletop.civ.game.TileView;
import com.moppletop.civ.game.ViewState;
import com.moppletop.civ.world.interceptors.ChunkPacketInterceptor;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.chunk.Chunk;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldViewManager {

    private final CivPlugin plugin;

    private final Map<UUID, WorldView> worldViews = new HashMap<>();

    public WorldViewManager(CivPlugin plugin) {
        this.plugin = plugin;

        plugin.getProtocolManager().addPacketListener(new ChunkPacketInterceptor(plugin, this));
    }

    public void update(Player player, TileLocation location, ViewState state) {
        WorldView worldView = worldViews.get(player.getUniqueId());

        if (worldView == null) {
            worldView = new WorldView();
            worldViews.put(player.getUniqueId(), worldView);
        }

        TileView tileView = worldView.getChunk(location);

        if (tileView == null) {
            tileView = new TileView(location);
            worldView.getTileViews().add(tileView);
        }

        tileView.setState(state);

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
//        ChunkCoordIntPair coordIntPair = new ChunkCoordIntPair(location.getX(), location.getZ());
        WorldServer worldServer = ((CraftWorld) entityPlayer.getBukkitEntity().getWorld()).getHandle();
        Chunk chunk = worldServer.getChunkIfLoaded(location.getX(), location.getZ());

        ClientboundLevelChunkWithLightPacket chunkPacket = new ClientboundLevelChunkWithLightPacket(chunk, worldServer.z_(), (BitSet) null, (BitSet) null);

//        entityPlayer.c.b(new PacketPlayOutUnloadChunk(coordIntPair));
        entityPlayer.c.b(chunkPacket);

        player.sendMessage("Reload chunk: " + state);

//        new PlayerChunkSender(true)
//                .a(entityPlayer.c, worldServer, chunk);
    }

    public TileView getTileViewFor(Player player, int x, int z) {
        WorldView worldView = worldViews.get(player.getUniqueId());

        if (worldView == null) {
            return null;
        }

        return worldView.getChunk(x, z);
    }

}
