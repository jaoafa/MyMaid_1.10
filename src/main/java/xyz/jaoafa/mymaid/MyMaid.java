package xyz.jaoafa.mymaid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.LunaChatAPI;
import com.ittekikun.plugin.eewalert.EEWAlert;

import eu.manuelgu.discordmc.MessageAPI;
import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.Access;
import xyz.jaoafa.mymaid.Command.ArrowShotter;
import xyz.jaoafa.mymaid.Command.AutoHeal;
import xyz.jaoafa.mymaid.Command.Chat;
import xyz.jaoafa.mymaid.Command.Ck;
import xyz.jaoafa.mymaid.Command.Cmd_Messenger;
import xyz.jaoafa.mymaid.Command.Cmdb;
import xyz.jaoafa.mymaid.Command.Cmdmymaid;
import xyz.jaoafa.mymaid.Command.Cmdsearch;
import xyz.jaoafa.mymaid.Command.Color;
import xyz.jaoafa.mymaid.Command.DOT;
import xyz.jaoafa.mymaid.Command.Data;
import xyz.jaoafa.mymaid.Command.Ded;
import xyz.jaoafa.mymaid.Command.DedMsg;
import xyz.jaoafa.mymaid.Command.DiscordLink;
import xyz.jaoafa.mymaid.Command.Discordsend;
import xyz.jaoafa.mymaid.Command.Dynamic;
import xyz.jaoafa.mymaid.Command.Dynmap_Compass;
import xyz.jaoafa.mymaid.Command.Dynmap_Teleporter;
import xyz.jaoafa.mymaid.Command.E;
import xyz.jaoafa.mymaid.Command.Explode;
import xyz.jaoafa.mymaid.Command.Eye;
import xyz.jaoafa.mymaid.Command.Gamemode_Change;
import xyz.jaoafa.mymaid.Command.Gettissue;
import xyz.jaoafa.mymaid.Command.Guard;
import xyz.jaoafa.mymaid.Command.Head;
import xyz.jaoafa.mymaid.Command.Home;
import xyz.jaoafa.mymaid.Command.Inv;
import xyz.jaoafa.mymaid.Command.InvEnder;
import xyz.jaoafa.mymaid.Command.InvLoad;
import xyz.jaoafa.mymaid.Command.InvSave;
import xyz.jaoafa.mymaid.Command.Ip_To_Host;
import xyz.jaoafa.mymaid.Command.Ja;
import xyz.jaoafa.mymaid.Command.Jao;
import xyz.jaoafa.mymaid.Command.JaoJao;
import xyz.jaoafa.mymaid.Command.Jf;
import xyz.jaoafa.mymaid.Command.Lag;
import xyz.jaoafa.mymaid.Command.Land;
import xyz.jaoafa.mymaid.Command.MakeCmd;
import xyz.jaoafa.mymaid.Command.MyBlock;
import xyz.jaoafa.mymaid.Command.MyMaid_NetworkApi;
import xyz.jaoafa.mymaid.Command.MyMob;
import xyz.jaoafa.mymaid.Command.Pexup;
import xyz.jaoafa.mymaid.Command.Pin;
import xyz.jaoafa.mymaid.Command.Prison;
import xyz.jaoafa.mymaid.Command.Quiz;
import xyz.jaoafa.mymaid.Command.Report;
import xyz.jaoafa.mymaid.Command.RuleLoad;
import xyz.jaoafa.mymaid.Command.SSK;
import xyz.jaoafa.mymaid.Command.SaveWorld;
import xyz.jaoafa.mymaid.Command.SignLock;
import xyz.jaoafa.mymaid.Command.Spawn;
import xyz.jaoafa.mymaid.Command.TNTReload;
import xyz.jaoafa.mymaid.Command.Unko;
import xyz.jaoafa.mymaid.Command.UpGallery;
import xyz.jaoafa.mymaid.Command.Var;
import xyz.jaoafa.mymaid.Command.VarCmd;
import xyz.jaoafa.mymaid.Command.Vote;
import xyz.jaoafa.mymaid.Command.WO;
import xyz.jaoafa.mymaid.Command.Where;
import xyz.jaoafa.mymaid.Command.WorldTeleport;
import xyz.jaoafa.mymaid.EventHandler.DefaultCheck;
import xyz.jaoafa.mymaid.EventHandler.EyeMove;
import xyz.jaoafa.mymaid.EventHandler.Menu;
import xyz.jaoafa.mymaid.EventHandler.MoveLocationName;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerChatEvent;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerPreLoginEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBannedEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockBreakEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockIgniteEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockPlaceEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockRedstoneEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBreak;
import xyz.jaoafa.mymaid.EventHandler.OnEEWReceiveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnEntityChangeBlockEvent;
import xyz.jaoafa.mymaid.EventHandler.OnExplosion;
import xyz.jaoafa.mymaid.EventHandler.OnFrom;
import xyz.jaoafa.mymaid.EventHandler.OnHeadClick;
import xyz.jaoafa.mymaid.EventHandler.OnInventoryClickEvent;
import xyz.jaoafa.mymaid.EventHandler.OnJoin;
import xyz.jaoafa.mymaid.EventHandler.OnLunaChatChannelMemberChangedEvent;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidCommandblockLogs;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidJoinLeftChatCmdLogs;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBedEnterEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBedLeaveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBucketEmptyEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommand;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommandPreprocessEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommandSendAdmin;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerDeathEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerGameModeChangeEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerInteractEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerItemHeldEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerJoinEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerKickEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerMoveAFK;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerMoveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerPickupItemEvent;
import xyz.jaoafa.mymaid.EventHandler.OnQuitGame;
import xyz.jaoafa.mymaid.EventHandler.OnServerCommandEvent;
import xyz.jaoafa.mymaid.EventHandler.OnSignClick;
import xyz.jaoafa.mymaid.EventHandler.OnVehicleCreateEvent;
import xyz.jaoafa.mymaid.EventHandler.OnVotifierEvent;

public class MyMaid extends JavaPlugin implements Listener {
	public static Boolean nextbakrender = false;

	public static Map<String,String> chatcolor = new HashMap<String,String>();
	public static TitleSender TitleSender;
	public static FileConfiguration conf;
	public static LunaChatAPI lunachatapi;
	public static LunaChat lunachat;
	public static int maxplayer;
	public static String maxplayertime;
	public static Connection c = null;
	public static String sqluser;
	public static String sqlpassword;
	private static JavaPlugin instance;
	/**
	 * プラグインが起動したときに呼び出し
	 * @author mine_book000
	 * @since 2016/04/04
	 */
	@Override
	public void onEnable() {
		getLogger().info("--------------------------------------------------");
		// クレジット
		getLogger().info("(c) jao Minecraft Server MyMaid Project.");
		getLogger().info("Product by tomachi.");

		//連携プラグインの確認
		Load_Plugin("EEWAlert");
		Load_Plugin("PermissionsEx");
		Load_Plugin("WorldEdit");
		Load_Plugin("LunaChat");
		Load_Plugin("CoreProtect");
		Load_Plugin("Votifier");
		Load_Plugin("MCBans");
		Load_Plugin("DiscordMC");

		//EEWAlertの設定
		EEWAlert eew = (EEWAlert)getServer().getPluginManager().getPlugin("EEWAlert");
		String eewnoticeold;
		if(eew.eewAlertConfig.notificationMode){
			eewnoticeold = "true";
		}else{
			eewnoticeold = "false";
		}
		eew.eewAlertConfig.notificationMode = false;
		getLogger().info("EEWAlertの通知動作設定を" + eewnoticeold + "からfalseに変更しました。");

		//初期設定(TitleSender, Lunachat設定)
		FirstSetting();
		//リスナーを設定
		Import_Listener();
		//スケジュールタスクをスケジュ―リング
		Import_Task();
		//コマンドを設定
		Import_Command_Executor();
		//コンフィグ読み込み
		Load_Config();
		//レシピ(クラフト)追加
		Add_Recipe();

		//Compromised Accountのキャッシュ処理
		Compromised_Account_Cacher();
		//BukkitRunnableの動作確認
		BukkitRunnableTester();

		getLogger().info("--------------------------------------------------");

		MessageAPI.sendToDiscord("**Server Started.**");
	}
	/**
	 * 初期設定
	 * @author mine_book000
	 */
	private void FirstSetting(){
		TitleSender = new TitleSender();

		lunachat = (LunaChat)getServer().getPluginManager().getPlugin("LunaChat");
		lunachatapi = lunachat.getLunaChatAPI();

		instance = this;
	}
	/**
	 * 連携プラグイン確認
	 * @author mine_book000
	 */
	private void Load_Plugin(String PluginName){
		if(getServer().getPluginManager().isPluginEnabled(PluginName)){
			getLogger().info("MyMaid Success(LOADED: " + PluginName + ")");
			getLogger().info("Using " + PluginName);
		}else{
			getLogger().warning("MyMaid ERR(NOTLOADED: " + PluginName + ")");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	/**
	 * コマンドの設定
	 * @author mine_book000
	 */
	private void Import_Command_Executor(){
		//Command Executor
		getCommand("access").setExecutor(new Access(this));
		getCommand("afk").setExecutor(new AFK(this));
		getCommand("as").setExecutor(new ArrowShotter(this));
		getCommand("autoheal").setExecutor(new AutoHeal(this));
		getCommand("chat").setExecutor(new Chat(this));
		getCommand("ck").setExecutor(new Ck(this));
		getCommand("messenger").setExecutor(new Cmd_Messenger(this));
		getCommand("cmdb").setExecutor(new Cmdb(this));
		getCommand("mymaid").setExecutor(new Cmdmymaid(this));
		getCommand("cmdsearch").setExecutor(new Cmdsearch(this));
		getCommand("color").setExecutor(new Color(this));
		getCommand("color").setTabCompleter(new Color(this));
		getCommand("data").setExecutor(new Data(this));
		getCommand("ded").setExecutor(new Ded(this));
		getCommand("dedmsg").setExecutor(new DedMsg(this));
		getCommand("discordlink").setExecutor(new DiscordLink(this));
		getCommand("discordsend").setExecutor(new Discordsend(this));
		getCommand(".").setExecutor(new DOT(this));
		getCommand("dynamic").setExecutor(new Dynamic(this));
		getCommand("dc").setExecutor(new Dynmap_Compass(this));
		getCommand("dc").setTabCompleter(new Dynmap_Compass(this));
		getCommand("dt").setExecutor(new Dynmap_Teleporter(this));
		getCommand("dt").setTabCompleter(new Dynmap_Teleporter(this));
		getCommand("e").setExecutor(new E(this));
		getCommand("explode").setExecutor(new Explode(this));
		getCommand("eye").setExecutor(new Eye(this));
		getCommand("g").setExecutor(new Gamemode_Change(this));
		getCommand("gettissue").setExecutor(new Gettissue(this));
		getCommand("guard").setExecutor(new Guard(this));
		getCommand("head").setExecutor(new Head(this));
		getCommand("home").setExecutor(new Home(this));
		getCommand("inv").setExecutor(new Inv(this));
		getCommand("invsave").setExecutor(new InvSave(this));
		getCommand("invload").setExecutor(new InvLoad(this));
		getCommand("invender").setExecutor(new InvEnder(this));
		getCommand("iphost").setExecutor(new Ip_To_Host(this));
		getCommand("ja").setExecutor(new Ja(this));
		getCommand("jao").setExecutor(new Jao(this));
		getCommand("j2").setExecutor(new JaoJao(this));
		getCommand("jf").setExecutor(new Jf(this));
		getCommand("lag").setExecutor(new Lag(this));
		getCommand("land").setExecutor(new Land(this));
		getCommand("makecmd").setExecutor(new MakeCmd(this));
		getCommand("myblock").setExecutor(new MyBlock(this));
		getCommand("mymaid_networkapi").setExecutor(new MyMaid_NetworkApi(this));
		getCommand("mymob").setExecutor(new MyMob(this));
		getCommand("pexup").setExecutor(new Pexup(this));
		getCommand("pin").setExecutor(new Pin(this));
		getCommand("player").setExecutor(new xyz.jaoafa.mymaid.Command.Player(this));
		getCommand("jail").setExecutor(new Prison(this));
		getCommand("jail").setTabCompleter(new Prison(this));
		getCommand("quiz").setExecutor(new Quiz(this));
		getCommand("report").setExecutor(new Report(this));
		getCommand("ruleload").setExecutor(new RuleLoad(this));
		getCommand("save-world").setExecutor(new SaveWorld(this));
		getCommand("sign").setExecutor(new xyz.jaoafa.mymaid.Command.Sign(this));
		getCommand("signlock").setExecutor(new SignLock(this));
		getCommand("spawn").setExecutor(new Spawn(this));
		getCommand("skk").setExecutor(new SSK(this));
		getCommand("tnt").setExecutor(new TNTReload(this));
		getCommand("unko").setExecutor(new Unko(this));
		getCommand("upgallery").setExecutor(new UpGallery(this));
		getCommand("var").setExecutor(new Var(this));
		getCommand("var").setTabCompleter(new Var(this));
		getCommand("varcmd").setExecutor(new VarCmd(this));
		getCommand("vote").setExecutor(new Vote(this));
		getCommand("where").setExecutor(new Where(this));
		getCommand("wo").setExecutor(new WO(this));
		getCommand("wt").setExecutor(new WorldTeleport(this));
	}
	/**
	 * スケジューリング
	 * @author mine_book000
	 */
	private void Import_Task(){
		new World_saver().runTaskTimer(this, 0L, 36000L);
		new Dynmap_Update_Render().runTaskTimer(this, 0L, 36000L);
		new AFKChecker(this).runTaskTimer(this, 0L, 1200L);
		new AutoMessage().runTaskTimer(this, 0L, 36000L);
	}
	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){
		//Listener
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new DefaultCheck(this), this);
		getServer().getPluginManager().registerEvents(new EyeMove(this), this);
		getServer().getPluginManager().registerEvents(new Menu(this), this);
		getServer().getPluginManager().registerEvents(new MoveLocationName(this), this);
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerChatEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLoginEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBannedEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBlockBreakEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBlockIgniteEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBlockPlaceEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBlockRedstoneEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBreak(this), this);
		getServer().getPluginManager().registerEvents(new OnEEWReceiveEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnEntityChangeBlockEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnExplosion(this), this);
		getServer().getPluginManager().registerEvents(new OnFrom(this), this);
		getServer().getPluginManager().registerEvents(new OnHeadClick(this), this);
		getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnJoin(this), this);
		getServer().getPluginManager().registerEvents(new OnLunaChatChannelMemberChangedEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnMyMaidCommandblockLogs(this), this);
		getServer().getPluginManager().registerEvents(new OnMyMaidJoinLeftChatCmdLogs(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerBedEnterEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerBedLeaveEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerBucketEmptyEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerCommand(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerCommandPreprocessEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerCommandSendAdmin(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerGameModeChangeEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerItemHeldEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerKickEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerMoveAFK(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerPickupItemEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnQuitGame(this), this);
		getServer().getPluginManager().registerEvents(new OnServerCommandEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnSignClick(this), this);
		getServer().getPluginManager().registerEvents(new OnVehicleCreateEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnVotifierEvent(this), this);
		getServer().getPluginManager().registerEvents(new Land(this), this);
	}
	/**
	 * コンフィグ読み込み
	 * @author mine_book000
	 */
	private void Load_Config(){
		ConfigurationSerialization.registerClass(SerializableLocation.class);
		conf = getConfig();

		if(conf.contains("prison")){
			//Prison.prison = (Map<String,Boolean>) conf.getConfigurationSection("prison").getKeys(false);
			Map<String, Object> pl = conf.getConfigurationSection("prison").getValues(true);
			if(pl.size() != 0){
				for(Entry<String, Object> p: pl.entrySet()){
					Prison.prison.put(p.getKey(), (Boolean) p.getValue());
				}
			}
		}else{
			Prison.prison = new HashMap<String,Boolean>();
			conf.set("prison",Prison.prison);
		}
		if(conf.contains("prison_block")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> pl = conf.getConfigurationSection("prison_block").getValues(true);
			if(pl.size() != 0){
				for(Entry<String, Object> p: pl.entrySet()){
					Prison.prison_block.put(p.getKey(), (Boolean) p.getValue());
				}
			}
		}else{
			Prison.prison_block = new HashMap<String,Boolean>();
			conf.set("prison_block",Prison.prison_block);
		}
		if(conf.contains("prison_lasttext")){
			Map<String, Object> pl = conf.getConfigurationSection("prison_lasttext").getValues(true);
			if(pl.size() != 0){
				for(Entry<String, Object> p: pl.entrySet()){
					Prison.jail_lasttext.put(p.getKey(), p.getValue().toString());
				}
			}
		}else{
			Prison.jail_lasttext = new HashMap<String,String>();
			conf.set("prison_lasttext",Prison.jail_lasttext);
		}
		if(conf.contains("var")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> var = conf.getConfigurationSection("var").getValues(true);
			if(var.size() != 0){
				for(Entry<String, Object> p: var.entrySet()){
					Var.var.put(p.getKey(), (String) p.getValue());
				}
			}
		}else{
			Var.var = new HashMap<String, String>();
			conf.set("var",Var.var);
		}
		if(conf.contains("color")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> color = conf.getConfigurationSection("color").getValues(true);
			if(color.size() != 0){
				for(Entry<String, Object> p: color.entrySet()){
					String colorstring = (String) p.getValue();
					if(!colorstring.equalsIgnoreCase("null")){
						ChatColor chatcolor;
						if(colorstring.equalsIgnoreCase("AQUA")){
							chatcolor = ChatColor.AQUA;
						}else if(colorstring.equalsIgnoreCase("BLACK")){
							chatcolor = ChatColor.BLACK;
						}else if(colorstring.equalsIgnoreCase("BLUE")){
							chatcolor = ChatColor.BLUE;
						}else if(colorstring.equalsIgnoreCase("DARK_AQUA")){
							chatcolor = ChatColor.DARK_AQUA;
						}else if(colorstring.equalsIgnoreCase("DARK_BLUE")){
							chatcolor = ChatColor.DARK_BLUE;
						}else if(colorstring.equalsIgnoreCase("DARK_GRAY")){
							chatcolor = ChatColor.DARK_GRAY;
						}else if(colorstring.equalsIgnoreCase("DARK_GREEN")){
							chatcolor = ChatColor.DARK_GREEN;
						}else if(colorstring.equalsIgnoreCase("DARK_PURPLE")){
							chatcolor = ChatColor.DARK_PURPLE;
						}else if(colorstring.equalsIgnoreCase("DARK_RED")){
							chatcolor = ChatColor.DARK_RED;
						}else if(colorstring.equalsIgnoreCase("GOLD")){
							chatcolor = ChatColor.GOLD;
						}else if(colorstring.equalsIgnoreCase("GREEN")){
							chatcolor = ChatColor.GREEN;
						}else if(colorstring.equalsIgnoreCase("LIGHT_PURPLE")){
							chatcolor = ChatColor.LIGHT_PURPLE;
						}else if(colorstring.equalsIgnoreCase("RED")){
							chatcolor = ChatColor.RED;
						}else if(colorstring.equalsIgnoreCase("WHITE")){
							chatcolor = ChatColor.WHITE;
						}else if(colorstring.equalsIgnoreCase("YELLOW")){
							chatcolor = ChatColor.YELLOW;
						}else if(colorstring.equalsIgnoreCase("GRAY")){
							chatcolor = ChatColor.GRAY;
						}else{
							chatcolor = null;
						}
						Color.color.put(p.getKey(), chatcolor);
					}
				}
			}
		}else{
			Color.color = new HashMap<String, ChatColor>();
			conf.set("color",Color.color);
		}
		if(conf.contains("maxplayer")){
			maxplayer = conf.getInt("maxplayer");
		}else{
			maxplayer = 0;
		}
		if(conf.contains("maxplayertime")){
			maxplayertime = conf.getString("maxplayertime");
		}else{
			maxplayertime = "無し";
		}
		if(conf.contains("home")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> home = conf.getConfigurationSection("home").getValues(true);
			if(home.size() != 0){
				for(Entry<String, Object> p: home.entrySet()){
					SerializableLocation sloc = (SerializableLocation)p.getValue();
					Location loc = sloc.getLocation();
					Home.home.put(p.getKey(), loc);
				}
			}
		}else{
			Home.home = new HashMap<String, Location>();
			conf.set("home",Home.home);
		}
		if(conf.contains("sqluser") && conf.contains("sqlpassword")){
			MyMaid.sqluser = conf.getString("sqluser");
			MyMaid.sqlpassword = conf.getString("sqlpassword");
			MySQL_Enable(conf.getString("sqluser"), conf.getString("sqlpassword"));
		}else{
			getLogger().info("MySQL Connect err. [conf NotFound]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
		}

		//jaoポイントをロード
		try {
			Pointjao.Loadjao();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("jaoPointのロードに失敗しました。");
		}
	}
	/**
	 * MySQLの初期設定
	 * @author mine_book000
	 */
	private void MySQL_Enable(String user, String password){
		MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", user, password);

		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [ClassNotFoundException]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [SQLException: " + e.getSQLState() + "]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("MySQL Connect successful.");
	}
	/**
	 * レシピ追加設定
	 * @author mine_book000
	 */
	private void Add_Recipe(){
		/* Ekusas83を以下の工程で作るの図
		 *
		 * [腐った肉] [ソウルサンド] [腐った肉]
		 * [ガストの涙] [エンドポータル] [ガストの涙]
		 * [腐った肉] [ソウルサンド] [腐った肉]
		 */
		ItemStack Ekusas83 = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_Ekusas83 = (SkullMeta) Ekusas83.getItemMeta();
		Ekusas83.setDurability((short) 3);
		skullMeta_Ekusas83.setDisplayName("Ekusas83");
		skullMeta_Ekusas83.setOwner("Ekusas83");
		Ekusas83.setItemMeta(skullMeta_Ekusas83);
		ShapedRecipe Ekusas83_sr = new ShapedRecipe(Ekusas83);

		Ekusas83_sr.shape("*+*","$%$","*+*");

		Ekusas83_sr.setIngredient('*', Material.ROTTEN_FLESH);
		Ekusas83_sr.setIngredient('+', Material.SOUL_SAND);
		Ekusas83_sr.setIngredient('$', Material.GHAST_TEAR);
		Ekusas83_sr.setIngredient('%', Material.ENDER_PORTAL_FRAME);
		Ekusas83_sr.setIngredient('%', Material.ENDER_PORTAL_FRAME);

		getServer().addRecipe(Ekusas83_sr);

		// -------------------------------------------- //
		/* X4Zを以下の工程で作るの図
		 *
		 * [Ekusas83] [砂利] [Ekusas83]
		 * [砂利] [レッドストーンブロック] [砂利]
		 * [Ekusas83] [砂利] [Ekusas83]
		 */
		ItemStack X4Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X4Z = (SkullMeta) X4Z.getItemMeta();
		X4Z.setDurability((short) 3);
		skullMeta_X4Z.setDisplayName("X4Z");
		skullMeta_X4Z.setOwner("X4Z");
		X4Z.setItemMeta(skullMeta_X4Z);
		ShapedRecipe X4Z_sr = new ShapedRecipe(X4Z);

		X4Z_sr.shape("*+*","+$+","*+*");

		X4Z_sr.setIngredient('*', Ekusas83.getData());
		X4Z_sr.setIngredient('+', Material.GRAVEL);
		X4Z_sr.setIngredient('$', Material.REDSTONE_BLOCK);

		getServer().addRecipe(X4Z_sr);
		// -------------------------------------------- //
		/* X5Zを以下の工程で作るの図
		 *
		 * [水バケツ] [X4Z] [火打ち石]
		 * [火薬] [ラピスラズリブロック] [火薬]
		 * [TNT] [Ekusas83] [溶岩バケツ]
		 */
		ItemStack X5Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X5Z = (SkullMeta) X5Z.getItemMeta();
		X5Z.setDurability((short) 3);
		skullMeta_X5Z.setDisplayName("X5Z");
		skullMeta_X5Z.setOwner("X5Z");
		X5Z.setItemMeta(skullMeta_X5Z);
		ShapedRecipe X5Z_sr = new ShapedRecipe(X5Z);

		X5Z_sr.shape("WZF","SRS","TEL");

		X5Z_sr.setIngredient('W', Material.WATER_BUCKET);
		X5Z_sr.setIngredient('Z', X4Z.getData());
		X5Z_sr.setIngredient('F', Material.FLINT_AND_STEEL);
		X5Z_sr.setIngredient('S', Material.SULPHUR);
		X5Z_sr.setIngredient('R', Material.LAPIS_BLOCK);
		X5Z_sr.setIngredient('F', Material.FLINT_AND_STEEL);
		X5Z_sr.setIngredient('T', Material.TNT);
		X5Z_sr.setIngredient('E', Ekusas83.getData());
		X5Z_sr.setIngredient('L', Material.LAVA_BUCKET);

		getServer().addRecipe(X5Z_sr);
		// -------------------------------------------- //
		/* X6Zを以下の工程で作るの図
		 *
		 * [AIR] [AIR] [AIR]
		 * [X5Z] [スライムボール] [Ekusas83]
		 * [AIR] [AIR] [AIR]
		 */
		ItemStack X6Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X6Z = (SkullMeta) X6Z.getItemMeta();
		X6Z.setDurability((short) 3);
		skullMeta_X6Z.setDisplayName("X6Z");
		skullMeta_X6Z.setOwner("X6Z");
		X6Z.setItemMeta(skullMeta_X6Z);
		ShapedRecipe X6Z_sr = new ShapedRecipe(X6Z);

		X6Z_sr.shape("+*$");

		X6Z_sr.setIngredient('+', X5Z.getData());
		X6Z_sr.setIngredient('*', Material.SLIME_BALL);
		X6Z_sr.setIngredient('$', Ekusas83.getData());

		getServer().addRecipe(X6Z_sr);
		// -------------------------------------------- //
		/* X8Zを以下の工程で作るの図
		 *
		 * [AIR] [AIR] [AIR]
		 * [X4Z] [スライムボール] [X4Z]
		 * [AIR] [AIR] [AIR]
		 */
		ItemStack X8Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X8Z = (SkullMeta) X8Z.getItemMeta();
		X8Z.setDurability((short) 3);
		skullMeta_X8Z.setDisplayName("X8Z");
		skullMeta_X8Z.setOwner("X8Z");
		X8Z.setItemMeta(skullMeta_X8Z);
		ShapedRecipe X8Z_sr = new ShapedRecipe(X8Z);

		X8Z_sr.shape("+*+");

		X8Z_sr.setIngredient('+', X4Z.getData());
		X8Z_sr.setIngredient('*', Material.SLIME_BALL);

		getServer().addRecipe(X8Z_sr);
		// -------------------------------------------- //
	}

	public static Map<String, String> cac = new HashMap<String, String>();
	public static Map<String, Map<String, String>> mcjppvp_banned = new HashMap<String, Map<String, String>>();
	/**
	 * Compromised Accountをログインさせないようにする前提キャッシュ
	 * @author mine_book000
	 * @see https://github.com/unchama/BanAssist
	 */
	private void Compromised_Account_Cacher(){
		try {
			URL url = new URL("https://pvp.minecraft.jp/punishments/banned-players.json");
			URLConnection urlCon = url.openConnection();
			urlCon.setRequestProperty("User-Agent", "MyMaid - Compromised Account Checker");
			InputStream in = urlCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String jstr = reader.readLine();

			// 不要な配列[]を除去、},{でsplitするために一時トークンを付与
			String[] banlist = jstr.replace("[", "").replace("]", "").replace("},{", "}},{{").split(Pattern.quote("},{"));
			// 各プレイヤーに対してチェック
			for (String p : banlist) {
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(p);
				if (jsonObject.get("reason").equals("Compromised Account")) {
					cac.put((String) jsonObject.get("uuid"), (String) jsonObject.get("name"));
				}else{
					Map<String, String> mcjppvp_data = new HashMap<String, String>();
					mcjppvp_data.put("name", (String) jsonObject.get("name"));
					mcjppvp_data.put("created", (String) jsonObject.get("created"));
					mcjppvp_data.put("reason", (String) jsonObject.get("reason"));
					mcjppvp_banned.put((String) jsonObject.get("uuid"), mcjppvp_data);
				}
			}
		} catch (Exception e) {
			getLogger().info("Compromised Account Cache Error.");
			e.printStackTrace();
		}
	}

	/**
	 * BukkitRunnableが動かないときの対処(強制再起動)
	 * @author mine_book000
	 */
	private void BukkitRunnableTester(){
		try{
			new BukkitRunnabletest().runTaskLater(this, 200);
		}catch(java.lang.NoClassDefFoundError e){
			restart();
		}
	}


	@Override
	public void onDisable() {
		conf.set("prison",Prison.prison);
		conf.set("prison_block",Prison.prison_block);
		conf.set("prison_lasttext",Prison.jail_lasttext);
		conf.set("var",Var.var);
		Map<String,String> colorstr = new HashMap<String,String>();
		for(Entry<String, ChatColor> p: Color.color.entrySet()){
			ChatColor color = p.getValue();
			String chatcolor;
			if(color.equals(ChatColor.AQUA)){
				chatcolor = "AQUA";
			}else if(color.equals(ChatColor.BLACK)){
				chatcolor = "BLACK";
			}else if(color.equals(ChatColor.BLUE)){
				chatcolor = "BLUE";
			}else if(color.equals(ChatColor.DARK_AQUA)){
				chatcolor = "DARK_AQUA";
			}else if(color.equals(ChatColor.DARK_BLUE)){
				chatcolor = "DARK_BLUE";
			}else if(color.equals(ChatColor.DARK_GRAY)){
				chatcolor = "DARK_GRAY";
			}else if(color.equals(ChatColor.DARK_GREEN)){
				chatcolor = "DARK_GREEN";
			}else if(color.equals(ChatColor.DARK_PURPLE)){
				chatcolor = "DARK_PURPLE";
			}else if(color.equals(ChatColor.DARK_RED)){
				chatcolor = "DARK_RED";
			}else if(color.equals(ChatColor.GOLD)){
				chatcolor = "GOLD";
			}else if(color.equals(ChatColor.GREEN)){
				chatcolor = "GREEN";
			}else if(color.equals(ChatColor.LIGHT_PURPLE)){
				chatcolor = "LIGHT_PURPLE";
			}else if(color.equals(ChatColor.RED)){
				chatcolor = "RED";
			}else if(color.equals(ChatColor.WHITE)){
				chatcolor = "WHITE";
			}else if(color.equals(ChatColor.YELLOW)){
				chatcolor = "YELLOW";
			}else if(color.equals(ChatColor.GRAY)){
				chatcolor = "GRAY";
			}else{
				chatcolor = null;
			}
			colorstr.put(p.getKey(), chatcolor);
		}
		conf.set("color", colorstr);
		conf.set("jao",Pointjao.jao);
		conf.set("maxplayer",maxplayer);
		conf.set("maxplayertime",maxplayertime);

		Map<String, SerializableLocation> home = new HashMap<String, SerializableLocation>();
		for(Entry<String, Location> home_: Home.home.entrySet()){
			SerializableLocation sloc = new SerializableLocation(home_.getValue());
			home.put(home_.getKey(), sloc);
		}
		conf.set("home", home);
		saveConfig();
		MessageAPI.sendToDiscord("**Server Stopped.**");
	}

	/* ----- タスク系 ----- */

	/**
	 * ワールド保存タスク(30分毎)
	 * @author mine_book000
	 */
	private class World_saver extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				List<World> worlds = Bukkit.getServer().getWorlds();
				for (World w : worlds)
				{
					w.save();
				}
			}
		}
	}

	/**
	 * 定期メッセージ(30分毎)
	 * @author mine_book000
	 */
	private class AutoMessage extends BukkitRunnable{
		@Override
		public void run() {
			Messenger.BroadcastMessage();
		}
	}

	/**
	 * Dynmapの自動レンダリング(30分毎)
	 * @author mine_book000
	 */
	private class Dynmap_Update_Render extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				long i = 0;
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					new Dynmap_Update_Render_User(player).runTaskLater(MyMaid.this, i);
					i += 100;
				}
			}
		}
	}
	/**
	 * Dynmapの自動レンダリング(時間差タスク)
	 * @author mine_book000
	 */
	private class Dynmap_Update_Render_User extends BukkitRunnable{
		Player player;
		public Dynmap_Update_Render_User(Player player){
			this.player = player;
		}
		@Override
		public void run() {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap updaterender Jao_Afa " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockZ() );
		}
	}

	public static Map<String,Long> afktime = new HashMap<String,Long>();
	/**
	 * AFKチェックタスク
	 * @author mine_book000
	 */
	private class AFKChecker extends BukkitRunnable{
		JavaPlugin plugin;
		public AFKChecker(JavaPlugin plugin) {
			this.plugin = plugin;
		}
		@Override
		public void run() {
			if(nextbakrender){
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(AFK.tnt.containsKey(player.getName())){
						continue;
					}
					if(!afktime.containsKey(player.getName())){
						continue;
					}
					if(player.getGameMode() == GameMode.SPECTATOR){
						continue;
					}
					long nowtime = System.currentTimeMillis();
					long lastmovetime = afktime.get(player.getName());
					long sa = nowtime - lastmovetime;
					if(sa >= 180000){
						ItemStack[] is = player.getInventory().getArmorContents();
						ItemStack[] after={
								new ItemStack(is[0]),
								new ItemStack(is[1]),
								new ItemStack(is[2]),
								new ItemStack(Material.ICE)};
						player.getInventory().setArmorContents(after);
						player.updateInventory();
						Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is afk!");
						MessageAPI.sendToDiscord(player.getName() + " is afk!");
						MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
						MyMaid.TitleSender.sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
						MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
						AFK.tnt.put(player.getName(), new AFK.afking(plugin, player).runTaskTimer(plugin, 0L, 5L));
					}
				}
			}
		}
	}

	private class BukkitRunnabletest extends BukkitRunnable{
		@Override
		public void run() {
		}
	}

	public void restart() {
		try {
			Class<?> c = Class.forName("java.lang.ApplicationShutdownHooks");
			Field f = c.getDeclaredField("hooks");
			f.setAccessible(true);
			List<Thread> list = new ArrayList<Thread>();
			Map<?, ?> hooks = (Map<?, ?>)f.get(null);
			for(Object thread : hooks.values()) {
				list.add((Thread)thread);
			}
			for(Thread thread : list) {
				Runtime.getRuntime().removeShutdownHook(thread);
			}
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						String command = "/var/minecraft/autorestart.sh";
						new ProcessBuilder(command).start();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
			Bukkit.shutdown();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static JavaPlugin getJavaPlugin(){
		return instance;
	}
}