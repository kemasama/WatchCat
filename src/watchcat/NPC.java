package watchcat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import watchcat.data.setSkin;

public class NPC {
	public EntityPlayer npc;

	public NPC(String displayName, Location loc, UUID skin) {
		try {
			MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
			WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

			GameProfile profile = setSkin.getProfileWithSkin(skin, displayName);

			npc = new EntityPlayer(server,
					world,
					profile,
					new PlayerInteractManager(world));

			npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

			byte flag = (byte) 0xFF;

			npc.getDataWatcher().a(13, flag);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return npc.getId();
	}

	public void spawn(Player p) {
		try {
			PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
			con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
			con.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(Player p) {
		try {
			PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
			con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
			con.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void walk(Location to) {
		npc.teleportTo(to, false);
	}
}
