package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Report implements CommandExecutor {
	JavaPlugin plugin;
	public Report(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	String old = "";
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1){
			String name = sender.getName();
			String text = "";
			int c = 0;
			while(args.length > c){
				text += args[c];
				if(args.length != (c+1)){
					text+=" ";
				}
				c++;
			}
			if(old.equalsIgnoreCase(text)){
				Method.SendMessage(sender, cmd, "前回通報された内容と同じです。");
				return true;
			}
			Method.url_jaoplugin("report", "p="+name+"&t="+text);
			Method.SendMessage(sender, cmd, "送信しました。");
			old = text;
			return true;
		}
		return true;
	}
}
