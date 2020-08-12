package watchcat.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import watchcat.Cat;
import watchcat.HackType;
import watchcat.data.FightData;
import watchcat.listener.sub.KillAuraCheck;

public class AutoClicker implements Listener {
	public HashMap<UUID, Long> Times = new HashMap<>();

	@EventHandler
	public void remove(PlayerQuitEvent event) {
		UUID key = event.getPlayer().getUniqueId();
		if (Times.containsKey(key)) {
			Times.remove(key);
		}
	}

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			long now = System.currentTimeMillis();
			long old = 1000;
			Player p = (Player) event.getDamager();
			UUID key = p.getUniqueId();
			if (Times.containsKey(key)) {
				old = Times.get(key);
			}

			long current = now - old;

			if (current < 100) {
				System.out.println(key.toString() + " Times Per " + current);
			}

			if (current <= Cat.LimitCPS) {
				Cat.broadcast(p, HackType.CLICK);
				event.setCancelled(true);
			}

			Times.put(key, now);

			FightData data = FightData.getData(p);
			data.setLastAttackedEntity(data.getAttackedEntity());
			data.setAttackedEntity(event.getEntity());

			KillAuraCheck.check(data, p);
		}
	}
}
