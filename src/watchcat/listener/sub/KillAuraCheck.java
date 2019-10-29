package watchcat.listener.sub;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import watchcat.Cat;
import watchcat.HackType;
import watchcat.data.FightData;

public class KillAuraCheck {
	public static boolean check(Player target) {
		return check(FightData.getData(target), target);
	}
	public static boolean check(FightData data, Player player) {
		Entity last = data.getLastAttackedEntity();
		Entity entity = data.getAttackedEntity();
		
		Vector playerLocation = player.getLocation().toVector();
		Vector entityLocation = entity.getLocation().toVector();
		double angle = entityLocation.subtract(playerLocation).angle(player.getLocation().getDirection());
		
		if (last != null && !entity.equals(last)) {
			Vector lastLocation = last.getLocation().toVector();
			double angleToLast = lastLocation.subtract(playerLocation).angle(player.getLocation().getDirection());
			double angleDifference = Math.abs(angleToLast - angle);
			
			if (angleDifference > Cat.maxAngle) {
				Cat.broadcast(player, HackType.KILLAURA);
				return true;
			}
		}
		
		return false;
	}
}
