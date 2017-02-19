package xyz.jaoafa.mymaid.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import xyz.jaoafa.mymaid.Command.Prison;

public class OnPlayerItemHeldEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerItemHeldEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Boolean tntexplode = true;

	@EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e){
		Player player = e.getPlayer();
		try {
			ItemStack hand = player.getInventory().getItem(e.getNewSlot());
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				for (PotionEffect po : potion.getCustomEffects()) {
					if(Prison.prison.containsKey(player.getName())){
						return;
					}
					if(po.getAmplifier() == 29){
						// アウト
						Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
						player.getInventory().clear(e.getNewSlot());
						player.getInventory().clear();
						player.getEnderChest().clear();
						Prison.prison.put(player.getName(), false);
						Prison.prison_block.put(player.getName(), false);
						World World = Bukkit.getServer().getWorld("Jao_Afa");
						Location prison = new Location(World, 1767, 70, 1767);
						player.teleport(prison);
						Date Date = new Date();
						SimpleDateFormat H = new SimpleDateFormat("H");
						SimpleDateFormat m = new SimpleDateFormat("m");
						SimpleDateFormat s = new SimpleDateFormat("s");
						String Hs = H.format(Date);
						String ms = m.format(Date);
						String ss = s.format(Date);
						String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "話を聞けば、「jaoium所有」という理由でここにきたみたいだね。");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "なにをしてしまったのか詳しい話は知らないけどさっき言ったような理由でここに来たんだと思うんだ。");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(player.getName())) {
								p.sendMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「jaoium所有」という理由で牢獄リストに追加しました。");
							}
						}
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「jaoium所有」という理由で牢獄リストに追加しました。");
					}
				}
			}else if(hand.getType() == Material.EXPLOSIVE_MINECART){
				player.getInventory().clear(e.getNewSlot());
			}
		}catch(java.lang.NullPointerException ex){
		}
	}
}

