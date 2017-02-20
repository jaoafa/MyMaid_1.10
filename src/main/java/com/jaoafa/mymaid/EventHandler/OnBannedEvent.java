package com.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;
import com.mcbans.firestar.mcbans.events.PlayerBannedEvent;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnBannedEvent implements Listener {
	JavaPlugin plugin;
	public OnBannedEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerBanned(PlayerBannedEvent event){
		String player = event.getPlayerName();
		String sender = event.getSenderName();
		String reason = event.getReason();

		if(event.isGlobalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってGBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=gban&r="+reason);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[BANDATA] " + ChatColor.GREEN + "サーバの評価値を上げるため、MCBansに証拠画像を提供してください！ http://mcbans.com/server/jaoafa.com");
				}
			}
		}else if(event.isLocalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってLBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=lban&r="+reason);
		}else if(event.isTempBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってTBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=tban&r="+reason);
		}
	}
}
