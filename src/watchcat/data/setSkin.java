package watchcat.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;

public class setSkin {
	public static boolean AutoSetSkin(Player from, UUID to){
		GameProfile profile = getProfile(from);
		boolean flag = set(profile, to);
		return flag;
	}

	public static GameProfile getProfile(Player p){
		GameProfile profile = new GameProfile(p.getUniqueId(), p.getName());
		return profile;
	}

	public static GameProfile getProfileWithSkin(UUID uuid, String name){
		GameProfile profile = new GameProfile(UUID.randomUUID(), name);
		try{
			String reply = getSession(uuid);
			if (reply == null){
				return profile;
			}
			String skin = reply.split("\"value\":\"")[1].split("\"")[0];
			String signature = reply.split("\"signature\":\"")[1].split("\"")[0];
			profile.getProperties().put("textures", new Property("textures", skin, signature));
			return profile;
		} catch (Exception e){
			e.printStackTrace();
			return profile;
		}
	}

	public static boolean set(GameProfile profile, UUID uuid){
		try{

			String reply = null;
			HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
				reply = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
			}

			if (reply == null){
				return false;
			}
			System.out.println(reply);
			String skin = reply.split("\"value\":\"")[1].split("\"")[0];
			System.out.println(skin);
			String signature = reply.split("\"signature\":\"")[1].split("\"")[0];
			System.out.println(signature);
			profile.getProperties().put("textures", new Property("textures", skin, signature));
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public static String getSession(UUID uuid){
		try{
			HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
				String reply = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
				return reply;
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
