package xyz.jaoafa.mymaid.EventHandler;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnMyMaidJoinLeftChatCmdLogs implements Listener {
	JavaPlugin plugin;
	public OnMyMaidJoinLeftChatCmdLogs(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void Join(PlayerJoinEvent event) {
		log(plugin, "join", event.getPlayer(), "");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void Chat(AsyncPlayerChatEvent event){
		log(plugin, "chat", event.getPlayer(), event.getMessage());
	}

	@EventHandler(priority = EventPriority.MONITOR)
    public void Command(PlayerCommandPreprocessEvent event){
		log(plugin, "command", event.getPlayer(), event.getMessage());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void Quit(PlayerQuitEvent event){
		log(plugin, "left", event.getPlayer(), "");
	}

	@SuppressWarnings("unchecked")
	public static void log(final JavaPlugin plugin, final String type, final Player player, final String message) {
		try{
			new BukkitRunnable() {
	            @Override
	            public void run() {
	            	JSONObject obj = new JSONObject();
	            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            	SimpleDateFormat sdfall = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	            	String file = plugin.getDataFolder() + File.separator + "log" + File.separator + sdf.format(new Date()) + ".log";

	            	if(type.equalsIgnoreCase("join")){
	            		obj.put("type", "join");
	            		obj.put("player", player.getName());
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}else if(type.equalsIgnoreCase("chat")){
	            		obj.put("type", "chat");
	            		obj.put("player", player.getName());
	            		obj.put("message", message);
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}else if(type.equalsIgnoreCase("command")){
	            		obj.put("type", "command");
	            		obj.put("player", player.getName());
	            		obj.put("command", message);
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}else if(type.equalsIgnoreCase("left")){
	            		obj.put("type", "left");
	            		obj.put("player", player.getName());
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}

	            	String str = obj.toJSONString();
	            	FileWriter writer = null;
	            	try {
	            		writer = new FileWriter(file, true);
	            		writer.write(str + "\r\n");
	            		writer.flush();
	            	} catch (Exception e) {
	            		e.printStackTrace();
	            	} finally {
	            		if ( writer != null ) {
	            			try {
	            				writer.close();
	            			} catch (Exception e) {
	            			}
	            		}
	            	}
	            }
			}.runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static void log(final JavaPlugin plugin, final String type, final String player, final String message) {
		try{
			new BukkitRunnable() {
	            @Override
	            public void run() {
	            	JSONObject obj = new JSONObject();
	            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            	SimpleDateFormat sdfall = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	            	String file = plugin.getDataFolder() + File.separator + "log" + File.separator + sdf.format(new Date()) + ".log";

	            	if(type.equalsIgnoreCase("join")){
	            		obj.put("type", "join");
	            		obj.put("player", player);
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}else if(type.equalsIgnoreCase("chat")){
	            		obj.put("type", "chat");
	            		obj.put("player", player);
	            		obj.put("message", message);
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}else if(type.equalsIgnoreCase("command")){
	            		obj.put("type", "command");
	            		obj.put("player", player);
	            		obj.put("command", message);
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}else if(type.equalsIgnoreCase("left")){
	            		obj.put("type", "left");
	            		obj.put("player", player);
	            		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
	            		for(String group : groups){
	            			if(PermissionsEx.getUser(player).inGroup(group)){
	            				obj.put("permission", group);
	            			}
	            		}
	            		obj.put("time", sdfall.format(new Date()));
	            	}

	            	String str = obj.toJSONString();
	            	FileWriter writer = null;
	            	try {
	            		writer = new FileWriter(file, true);
	            		writer.write(str + "\r\n");
	            		writer.flush();
	            	} catch (Exception e) {
	            		e.printStackTrace();
	            	} finally {
	            		if ( writer != null ) {
	            			try {
	            				writer.close();
	            			} catch (Exception e) {
	            			}
	            		}
	            	}
	            }
			}.runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
				}
			}
		}

	}
}