package xyz.jaoafa.mymaid.EventHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class OnAsyncPlayerPreLoginEvent implements Listener {
	JavaPlugin plugin;
	public OnAsyncPlayerPreLoginEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Boolean> LD = new HashMap<String,Boolean>();
	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e){
		String name = e.getName();
		InetAddress ip = e.getAddress();
		UUID uuid = e.getUniqueId();
		String host = e.getAddress().getHostName();

		String data = Method.url_jaoplugin("login2", "p="+name+"&u="+uuid+"&i="+ip+"&h="+host);
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(data);
		} catch (ParseException e2) {
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "jsonを解析できませんでした。Nubescoサーバが停止しているか、予期しないエラーがlogin2.phpで起きた可能性があります。");
				}
			}
			return;
		}
		if(((String) obj.get("message")).equalsIgnoreCase("subaccount")){
			String player = ((String) obj.get("player"));
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.RED + "[Login Denied! Reason: SubAccount]\n"
					+ ChatColor.RESET + ChatColor.WHITE + "あなたのアカウントはサブアカウントと判定されました。詳細は以下の通りです。\n"
					+ ChatColor.RESET + ChatColor.AQUA + e.getName() + " = " + player + "\n"
					+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
					+ "公式Discordでお問い合わせをして頂いても構いません。");
			Bukkit.getLogger().info(e.getName()+": Connection to server failed! (Detect SubAccount.)");
			LoginErrBackupSaveTxt(e.getName(), DisAllowLoginType.SubAccount, player);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>複垢(" + player + ")");
				}
			}
			return;
		}

		if(((String) obj.get("message")).equalsIgnoreCase("pban")){
			String message = ((String) obj.get("player"));
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.RED + "[Login Denied! Reason: PBan]\n"
					+ ChatColor.RESET + ChatColor.WHITE + "あなたのアカウントはPBanをされています。詳細は以下の通りです。\n"
					+ ChatColor.RESET + ChatColor.AQUA + message + "\n"
					+ ChatColor.RESET + ChatColor.WHITE + "もしこのBanに異議がある場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
					+ "公式Discordでお問い合わせをして頂いても構いません。");
			Bukkit.getLogger().info(e.getName()+": Connection to server failed!([PBan] " + message + ")");
			LoginErrBackupSaveTxt(e.getName(), DisAllowLoginType.PBan, message);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>[PBan] " + message + ")");
				}
			}
			return;
		}

		if(((String) obj.get("message")).equalsIgnoreCase("countoryout")){
			String message = ((String) obj.get("player"));
			URLCodec codec = new URLCodec();
			String country = null;
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.RED + "[Login Denied! Reason: Region Error]\n"
					+ ChatColor.RESET + ChatColor.WHITE + "海外からのログインと判定されました。\n"
					+ ChatColor.RESET + ChatColor.AQUA + "当サーバでは、日本国外からのログインを禁止しています。\n"
					+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
					+ "公式Discordでお問い合わせをして頂いても構いません。");
			try {
				country = codec.decode(message, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e1) {
				// TODO 自動生成された catch ブロック
				Bukkit.getLogger().info("err");
			} catch (DecoderException e1) {
				Bukkit.getLogger().info("err");
			}
			Bukkit.getLogger().info(e.getName()+": Connection to server failed!(Region Error "+country+")");
			LoginErrBackupSaveTxt(e.getName(), DisAllowLoginType.RegionErr, country);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>許可されていない地域からのログイン("+country+")");
				}
			}
			return;
		}

		if(((String) obj.get("message")).equalsIgnoreCase("mcbans_reputation")){
			String message = ((String) obj.get("player"));
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.RED + "[Login Denied! Reason: MCBansReputationErr]\n"
					+ ChatColor.RESET + ChatColor.WHITE + "あなたのMCBansにおける評価値はこのサーバーが指定する評価値を下回っています。\n"
					+ ChatColor.RESET + ChatColor.WHITE + "(Your MCBans Reputation is below this servers threshold!)\n"
					+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
					+ "公式Discordでお問い合わせをして頂いても構いません。");
			LoginErrBackupSaveTxt(e.getName(), DisAllowLoginType.MCBansRepErr, message);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>鯖指定評価値下回り(" + message + ")");
				}
			}
			return;
		}
		if(!((String) obj.get("message")).equalsIgnoreCase("CAUnCheck")){
			if(MyMaid.cac.containsKey(e.getUniqueId().toString())){
				e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.RED + "[Login Denied! Reason: Compromised Account]\n"
						+ ChatColor.RESET + ChatColor.WHITE + "あなたのアカウントは不正なアカウントと判定されました。\n"
						+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
						+ "公式Discordでお問い合わせをして頂いても構いません。");
				LoginErrBackupSaveTxt(e.getName(), DisAllowLoginType.CompromisedAccount, "");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>Compromised Account(不正アカウント)");
					}
				}
				return;
			}
		}

		if(MyMaid.mcjppvp_banned.containsKey(e.getUniqueId().toString())){
			Map<String, String> mcjppvp_data = MyMaid.mcjppvp_banned.get(e.getUniqueId().toString());
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>MCJPPvPにおいて過去にBan情報有り。(" + mcjppvp_data.get("reason") + " [" + mcjppvp_data.get("created") + "])");
				}
			}
			return;
		}

		try{
			new netaccess(plugin, name, uuid, ip, host).runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError ex){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + ex.getMessage());
				}
			}
		}


		Bukkit.getLogger().info("------------------------------------------");
		Bukkit.getLogger().info("Player:"+name+" Log in.");
		Bukkit.getLogger().info("PlayerUUID:"+uuid);
		Bukkit.getLogger().info("PlayerIP:"+e.getAddress());
		Bukkit.getLogger().info("PlayerHost:"+host);
		Bukkit.getLogger().info("------------------------------------------");
	}
	private class netaccess extends BukkitRunnable{
		String player;
		UUID uuid;
		InetAddress ip;
		String host;
		public netaccess(JavaPlugin plugin, String player, UUID uuid, InetAddress ip, String host) {
			this.player = player;
			this.uuid = uuid;
			this.ip = ip;
			this.host = host;
		}
		@Override
		public void run() {
			Method.url_jaoplugin("mccheck_anotherlogin", "p=" + player + "&u=" + uuid + "&i=" + ip + "&host=" + host);
		}
	}
	public enum DisAllowLoginType {
		SubAccount("サブアカウント"),
		PBan("PBan"),
		RegionErr("海外からのログイン"),
		MCBansRepErr("MCBansでの評価値下回り"),
		CompromisedAccount("MCJPPvPにてCompromisedAccountでBan");



		private String name;

		DisAllowLoginType(String name) {
			this.name = name;
		}
	}
	private void LoginErrBackupSaveTxt(String player, DisAllowLoginType type, String reason){
		try{
			File file = new File(plugin.getDataFolder() + File.separator + "loginerrlog.txt");

			if(file.exists()){
				file.createNewFile();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String text = "["+ sdf.format(new Date()) + "|" + type.name + "] ";
			StringBuffer TextBuf = new StringBuffer();
			TextBuf.append(text);

			if(type == DisAllowLoginType.SubAccount){
				TextBuf.append(player + "はサブアカウント判定をされました。(関連ID: " + reason + ")");
			}else if(type == DisAllowLoginType.PBan){
				TextBuf.append(player + "はPBanをされています。(理由: " + reason + ")");
			}else if(type == DisAllowLoginType.RegionErr){
				TextBuf.append(player + "は海外からのログインと判定されました。(" + reason +")");
			}else if(type == DisAllowLoginType.MCBansRepErr){
				TextBuf.append(player + "はMCBansでの評価値が鯖指定評価値を下回っています。");
			}else if(type == DisAllowLoginType.CompromisedAccount){
				TextBuf.append(player + "はMCJPPvPにてCompromisedAccountでBanされています。");
			}

			text = TextBuf.toString();

			FileWriter filewriter = new FileWriter(file, true);

			filewriter.write(text + System.getProperty("line.separator"));

			filewriter.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
}
