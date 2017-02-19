package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.manuelgu.discordmc.MessageAPI;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;

public class JaoJao implements CommandExecutor {
	JavaPlugin plugin;
	public JaoJao(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand jf
	 * jao afaします。
	 * /jf */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// 変数定義
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;

		int use = 1;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "jaojaoコマンド実行の為");

		String Msg = "";
		if(player.hasPermission("mymaid.pex.limited")){
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());

		}else if(Prison.prison.containsKey(player.getName())){
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
		}else if(Color.color.containsKey(player.getName())){
			Msg = player.getName().replaceFirst(player.getName(), Color.color.get(player.getName()) + "■" + ChatColor.WHITE + player.getName());
		}else if(MyMaid.chatcolor.containsKey(player.getName())){
			int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
			if(i >= 0 && i <= 5){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 6 && i <= 19){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 20 && i <= 33){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 34 && i <= 47){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 48 && i <= 61){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_AQUA + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 62 && i <= 76){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 77 && i <= 89){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 90 && i <= 103){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.YELLOW + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 104 && i <= 117){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 118 && i <= 131){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 132 && i <= 145){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 146 && i <= 159){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 160){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
			}
		}else{
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
		}
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + ": jaojao");
		MessageAPI.sendToDiscord("**" + player.getName() + "**: jaojao");
		Method.SendTipsALL("このjaojaoって言うのはこのサーバからログアウトする前にする挨拶だよ！やってみよう！");
		return true;
	}
}
