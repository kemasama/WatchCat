package watchcat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import watchcat.Cat;
import watchcat.HackType;

public class ReachCheck implements Listener {

	@EventHandler
	public void Check(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			if (p.getLocation().distance(event.getEntity().getLocation()) >= Cat.MAX_REACH) {
				if (Math.abs(p.getLocation().getBlockY() - event.getEntity().getLocation().getBlockY()) <= 2) {
					Cat.broadcast(p, HackType.REACH);
				}
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void CheckKB(PlayerVelocityEvent event) {
		Player p = event.getPlayer();
		if (!p.isFlying()) {
			int blocks = (int) p.getLocation().distance(event.getVelocity().toLocation(p.getWorld()));
			if (blocks < 1) {
				Cat.broadcast(p, HackType.AKB);
				event.setCancelled(true);
			}
		}
		
	}
}
