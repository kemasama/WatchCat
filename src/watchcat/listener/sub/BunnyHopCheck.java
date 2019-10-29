package watchcat.listener.sub;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import watchcat.Cat;
import watchcat.HackType;

public class BunnyHopCheck{
	public static void onCheck(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		int dy = Math.abs(event.getFrom().getBlockY() - event.getTo().getBlockY());

		if (dy > 3) {
			Cat.broadcast(p, HackType.FALL);
		}

		double xDiff = Math.abs(event.getTo().getX() - event.getFrom().getX());
		double zDiff = Math.abs(event.getTo().getZ() - event.getFrom().getZ());

		if (xDiff > 1.0D || zDiff > 1.0D) {
			Location loc = p.getLocation();
			if (!loc.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
				Cat.broadcast(p, HackType.BHOP);
			}
		}

	}
}
