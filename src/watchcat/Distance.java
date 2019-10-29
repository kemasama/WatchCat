package watchcat;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

public class Distance {
	private Location from, to;

	private Double xDiff, yDiff, zDiff;

	public Distance(PlayerMoveEvent e){
		this.from = e.getFrom();
		this.to = e.getTo();

		this.xDiff = (from.getX() > to.getX() ? from.getX() : to.getX()) - (from.getX() < to.getX() ? from.getX() : to.getX());
		this.yDiff = (from.getY() > to.getY() ? from.getY() : to.getY()) - (from.getY() < to.getY() ? from.getY() : to.getY());
		this.zDiff = (from.getZ() > to.getZ() ? from.getZ() : to.getZ()) - (from.getZ() < to.getZ() ? from.getZ() : to.getZ());

	}

	/**
	 * @return from
	 */
	public Location getFrom() {
		return from;
	}

	/**
	 * @param from セットする from
	 */
	public void setFrom(Location from) {
		this.from = from;
	}

	/**
	 * @return to
	 */
	public Location getTo() {
		return to;
	}

	/**
	 * @param to セットする to
	 */
	public void setTo(Location to) {
		this.to = to;
	}

	public Double getxDiff() {
		return xDiff;
	}

	public void setxDiff(Double xDiff) {
		this.xDiff = xDiff;
	}

	public Double getyDiff() {
		return yDiff;
	}

	public void setyDiff(Double yDiff) {
		this.yDiff = yDiff;
	}

	public Double getzDiff() {
		return zDiff;
	}

	public void setzDiff(Double zDiff) {
		this.zDiff = zDiff;
	}


}
