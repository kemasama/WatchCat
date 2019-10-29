package watchcat.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import watchcat.Cat;
import watchcat.Distance;
import watchcat.HackType;
import watchcat.listener.sub.BunnyHopCheck;

public class SpeedCheck implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();

		if (p.hasPotionEffect(PotionEffectType.SPEED)) {
			return;
		}
		if (p.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		
		if (p.getAllowFlight()) {
			return;
		}

		//Location from = event.getFrom();
		Location to = event.getTo();
		Distance d = new Distance(event);

		Double xz_speed = (d.getxDiff() > d.getzDiff() ? d.getxDiff() : d.getzDiff());

		if (xz_speed >= Cat.MAX_XZ_SPEED) {
			if (!p.isFlying()) {
				if (p.getLastDamageCause() != null && p.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)) {
					p.setLastDamageCause(null);
					return;
				}
				Cat.broadcast(p, HackType.SPEED);
				//event.setCancelled(true);
			}
		}

		if (p.isSneaking() || to.getBlock().getType().equals(Material.WEB) || to.getBlock().getType().equals(Material.WATER) || to.getBlock().getType().equals(Material.STATIONARY_WATER)) {
			if (xz_speed >= Cat.MAX_XZ_SPEED_WITH) {
				if (p.getLastDamageCause() != null && p.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)) {
					p.setLastDamageCause(null);
					return;
				}
				
				Cat.broadcast(p, HackType.SPEED);
				//event.setCancelled(true);
			}
		}

		BunnyHopCheck.onCheck(event);

	}
}
