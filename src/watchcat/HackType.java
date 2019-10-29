package watchcat;

public enum HackType {
	REACH("Reach"),
	CLICK("AutoClicker"),
	SPEED("Speed"),
	FALL("Fall"),
	KILLAURA("KillAura"),
	KILLAURA_NPC("KillAuraNPC"),
	AKB("AntiKnockBack"),
	BHOP("BunnyHop");

	String name;
	private HackType(String name) {
		this.name = name;
	}

	public String getType() {
		return this.name;
	}
}
