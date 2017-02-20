package com.jaoafa.mymaid.Command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class E implements CommandExecutor {
	JavaPlugin plugin;
	public E(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (sender instanceof Player) {
			Player player = (Player)sender;
			World World = player.getWorld();
			Location location = player.getLocation();
			int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
			Location tnt = new Location(World, x, y, z);
			Explode.explode.put(tnt, 100);
			Method.SendMessage(sender, cmd, "一時的にここから100ブロック範囲内の爆発を無効化しました。解除するには/explodeコマンドを使用してください。");
			return true;
		}
		if (sender instanceof BlockCommandSender) {
			BlockCommandSender cmb = (BlockCommandSender)sender;
			World World = cmb.getBlock().getWorld();
			Location location = cmb.getBlock().getLocation();
			int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
			Location tnt = new Location(World, x, y, z);
			Explode.explode.put(tnt, 100);
			Method.SendMessage(sender, cmd, "一時的にここから100ブロック範囲内の爆発を無効化しました。解除するには/explodeコマンドを使用してください。");
			return true;
		}
		Method.SendMessage(sender, cmd, "プレイヤーかコマンドブロックから実行してください");
		return true;
	}
}
