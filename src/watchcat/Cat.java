package watchcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import watchcat.listener.AutoClicker;
import watchcat.listener.JoinListener;
import watchcat.listener.ReachCheck;
import watchcat.listener.SpeedCheck;


public class Cat extends JavaPlugin {
	public static Cat Instance;
	public static String byPassPermission = "cat.bypass";

	/**
	 * Configuration
	 */

	public static long LimitCPS = 100;
	public static int LimitCount = 3;
	public static boolean forceKick = false;
	public static boolean forceBan = false;
	public static double MAX_REACH = 3.4;
	public static Double MAX_XZ_SPEED = 0.77D;
	public static Double MAX_XZ_SPEED_WITH = 0.44D;
	public static double maxAngle = 1.2;
	public static HashMap<UUID, NPC> Npcs = new HashMap<>();

	public static HashMap<UUID, Integer> Count = new HashMap<>();

	@Override
	public void onDisable() {

		Count.clear();

		super.onDisable();
	}

	@Override
	public void onEnable() {

		Instance = this;

		saveDefaultConfig();

		FileConfiguration config = getConfig();
		LimitCPS = config.getLong("limitCPS", 100);
		LimitCount = config.getInt("limitCount", 3);
		forceKick = config.getBoolean("forceKick", false);
		forceBan = config.getBoolean("forceBan", false);
		MAX_REACH = config.getDouble("maxReach", 3.4);
		MAX_XZ_SPEED = config.getDouble("maxXZSpeed", 0.77D);
		MAX_XZ_SPEED_WITH = config.getDouble("maxXZSpeedWith", 0.44D);

		Bukkit.getPluginManager().registerEvents(new AutoClicker(), this);
		Bukkit.getPluginManager().registerEvents(new ReachCheck(), this);
		Bukkit.getPluginManager().registerEvents(new SpeedCheck(), this);
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DeBan");
		Bukkit.getPluginCommand("test").setExecutor(new TestCommand());

		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				ArrayList<UUID> Removes = new ArrayList<>();
				for (UUID key : Count.keySet()) {
					Player p = Bukkit.getPlayer(key);
					if (p != null) {
						if (Count.get(key) > LimitCount) {
							executeCommand("kick", p.getName(), "[WATCHCAT] Kicked by ");
						}
					}else {
						Removes.add(key);
					}
				}

				for (UUID key : Removes) {
					if (Count.containsKey(key)) {
						Count.remove(key);
					}
				}

				Removes.clear();
			}
		}, 0L, 20L);

		try {
			if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
				ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
					@Override
					public void onPacketReceiving(PacketEvent event) {
						if (event.getPacketType().equals(PacketType.Play.Client.USE_ENTITY)) {
							PacketContainer packet = event.getPacket();
							Player p = event.getPlayer();
							int id = packet.getIntegers().read(0);
							UUID key = p.getUniqueId();

							if (Npcs.containsKey(key)) {
								NPC npc = Npcs.get(key);
								if (npc.getId() == id) {
									Cat.broadcast(p, HackType.KILLAURA_NPC);
									npc.remove(p);
									Npcs.remove(key);
								}
							}
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onEnable();
	}

	public static void broadcast(Player target, HackType type) {
		broadcast(target, type, true);
	}
	public static void broadcast(Player target, HackType type, boolean bypass) {

		if (bypass) {
			if (target.hasPermission(Cat.byPassPermission)){
				return;
			}
		}

		Bukkit.broadcast(String.format("§b%s §7use hack §c%s", target.getName(), type.getType()), "cat.notify");
		if (Cat.forceBan) {
			executeCommand("tban", target.getName());
		}
		if (Cat.forceKick) {
			executeCommand("kick", target.getName(), "[WATCHCAT] Kicked by " + type.getType());
		}

		if (type.equals(HackType.KILLAURA_NPC)) {
			//executeCommand("tban", target.getName());
		}

		if (type.equals(HackType.KILLAURA) && Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			final double fDiffX = target.getLocation().getDirection().getX();
			final double fDiffZ = target.getLocation().getDirection().getZ();
			final float yaw = target.getLocation().getYaw();
			final Player fTarget = target.getPlayer();
			Cat.regTask(new Runnable() {
				@Override
				public void run() {
					try {

						if (Npcs.containsKey(fTarget.getUniqueId())) {
							Npcs.get(fTarget.getUniqueId()).remove(fTarget);
							Npcs.remove(fTarget.getUniqueId());
						}

						double diffX = fDiffX;
						double diffZ = fDiffZ;
						if (yaw > -90 && yaw < 90) {
							diffZ += -3;
						}else {
							diffZ += 3;
						}

						UUID uuid = UUID.randomUUID();
						NPC npc = new NPC("Cat" + uuid.toString().replace("-", "").substring(0, 8), fTarget.getLocation().add(diffX, 2.5, diffZ), uuid);
						npc.spawn(fTarget);
						Npcs.put(fTarget.getUniqueId(), npc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// add Count
		final UUID key = target.getUniqueId();
		int cnt = 0;
		if (Count.containsKey(key)) {
			cnt = Count.get(key);
		}

		cnt++;
		Count.put(key, cnt);

		//executeCommand("watch", target.getName(), type.getType());

		/*
		final HackType typeOf = type;

		Bukkit.getScheduler().runTask(Cat.Instance, new Runnable() {
			@Override
			public void run() {
				Player p = Bukkit.getPlayer(key);

				StringBuilder builder = new StringBuilder();

				builder.append("【重大】 WatchCat によるチートの検出$");
				builder.append("$");
				builder.append("UUID " + key.toString() + "$");
				builder.append("Name " + (p != null ? p.getName() : "Unknown") + "$");
				builder.append("Type " + typeOf.getType() + "$");
				builder.append("$");
				builder.append("現在のカウント " + Count.get(key) + "$");
				builder.append("一時バン、永久バンの対応をしてください。$");


				String body = builder.toString();

				Mail mail = new Mail();
				try {
					mail.send("devras.cf@gmail.com", body);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		*/
	}

	public static void regTask(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(Instance, runnable);
	}
	public static void executeCommand(final String subChannel, final String... args) {
		regTask(new Runnable() {
			@Override
			public void run() {
				try {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF(subChannel);
					for (String k : args) {
						out.writeUTF(k);
					}

					Bukkit.getServer().sendPluginMessage(Cat.Instance, "DeBan", out.toByteArray());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
