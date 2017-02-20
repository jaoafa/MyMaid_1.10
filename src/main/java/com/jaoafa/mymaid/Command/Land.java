package com.jaoafa.mymaid.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;
import com.jaoafa.mymaid.MyMaid;
import com.jaoafa.mymaid.MySQL;
import com.jaoafa.mymaid.Pointjao;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.RegionSelector;

import ru.tehkode.permissions.bukkit.PermissionsEx;

@SuppressWarnings("deprecation")
public class Land implements CommandExecutor, Listener {
	JavaPlugin plugin;
	public Land(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	private double BasicLandCalculation(int blockcount, double distance){
		return blockcount / (distance / 100);
	}

	private double MarkLandCalculation(double value, double distance){
		return ( value * ( 3 - ( distance / 100 ) ) / 100 ) +  1;
	}

	private double LandMarkjaoAdd(CommandSender sender, Command cmd, String commandLabel, String[] args, double basicjao, Location LandLoc){
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return basicjao;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return basicjao;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land_mark");
			while(res.next()){
				Location markloc = new Location(Bukkit.getWorld("ReJao_Afa"), res.getInt("x"), LandLoc.getBlockY(), res.getInt("z"));
				double distance = LandLoc.distance(markloc);
				double value = res.getDouble("value");
				if(distance <= 300){
					basicjao = basicjao * MarkLandCalculation(value, distance);
				}

				/*
				Map<String, String> data = new HashMap<String, String>();
				data.put("id", "" + id);
				data.put("distance", "" + distance);
				data.put("value", "" + value);
				data.put("x", "" + res.getInt("x"));
				data.put("y", "" + res.getInt("y"));
				data.put("z", "" + res.getInt("z"));
				landmark.put(distance, data);
				*/
			}
			basicjao += 1;
			/*
			Iterator<Double> it = landmark.keySet().iterator();
			int c = 0;
			while (it.hasNext()) {
				if(c == 3){
					break;
				}
				double distance = it.next();
				Map<String, String> data = landmark.get(distance);
				double value = Double.parseDouble(data.get("value"));

				basicjao = basicjao * MarkLandCalculation(value, distance);
				c += 1;
			}
			*/
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return basicjao;
		}

		return Math.abs(basicjao);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			Method.SendMessage(sender, cmd, "このコマンドが使用できるワールドではありまぜん。");
			return true;
		}
		if(PermissionsEx.getUser(player).inGroup("Limited")){
			Method.SendMessage(sender, cmd, "このコマンドが使用できる権限を持っていません。");
			return true;
		}
		if(PermissionsEx.getUser(player).inGroup("QPPE")){
			Method.SendMessage(sender, cmd, "このコマンドが使用できる権限を持っていません。");
			return true;
		}
		if(PermissionsEx.getUser(player).inGroup("Default")){
			Method.SendMessage(sender, cmd, "このコマンドが使用できる権限を持っていません。");
			return true;
		}
		if(args.length == 0){
			Command_Land(sender, cmd, commandLabel, args);
			return true;
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("new")){
				Command_LandNew(sender, cmd, commandLabel, args);
				return true;
			}else if(args[0].equalsIgnoreCase("list")){
				Command_LandList(sender, cmd, commandLabel, args);
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("get")){
				Command_LandGet(sender, cmd, commandLabel, args);
				return true;
			}else if(args[0].equalsIgnoreCase("sell")){
				Command_LandSell(sender, cmd, commandLabel, args);
				return true;
			}else if(args[0].equalsIgnoreCase("tp")){
				Command_LandTp(sender, cmd, commandLabel, args);
				return true;
			}else if(args[0].equalsIgnoreCase("search")){
				Command_LandSearch(sender, cmd, commandLabel, args);
				return true;
			}else if(args[0].equalsIgnoreCase("newmark")){
				Command_LandNewMark(sender, cmd, commandLabel, args);
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "--- Land Help ---");
		Method.SendMessage(sender, cmd, "/land: いま居る場所の土地所有者を確認します。");
		Method.SendMessage(sender, cmd, "/land new: 土地を登録します。(管理部のみ)");
		Method.SendMessage(sender, cmd, "/land list: 自分が所有する土地を表示します");
		Method.SendMessage(sender, cmd, "/land get <LandID>: 土地IDの土地を取得します。(1人につき10つの土地が取得可能)");
		Method.SendMessage(sender, cmd, "/land sell <LandID>: 土地IDの土地を売ります。(現在所有している土地のみ)");
		Method.SendMessage(sender, cmd, "/land tp <LandID>: 土地IDの土地中心にテレポートします。");
		Method.SendMessage(sender, cmd, "/land search <LandID>: 土地IDの土地について調べます。");
		Method.SendMessage(sender, cmd, "/land newmark <LandID>: ランドマークを登録します。(管理部のみ)");
		return true;

	}

	/*
	 * コマンド実行時メゾット
	 */

	private void Command_Land(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + player.getLocation().getBlockX() + " AND y1 >= " + player.getLocation().getBlockY() + " AND z1 >= " + player.getLocation().getBlockZ() + " AND x2 <= " + player.getLocation().getBlockX() + " AND y2 <= " + player.getLocation().getBlockY() + " AND z2 <= " + player.getLocation().getBlockZ() + ";");
			if(!res.next()){
				Method.SendMessage(sender, cmd, "この土地は登録されていません。");
				return;
			}else{
				int id = res.getInt("id");
				if(!res.getBoolean("isplayerland")){

					int blocki = 0;
					for(int i1=res.getInt("x2"); i1<=res.getInt("x1"); i1++){
						for(int k=res.getInt("z2"); k<=res.getInt("z1"); k++){
							blocki += 1;
						}
					}
					Location spawncenterloc = new Location(player.getWorld(), 0, 0, 0);
					Location landcenter = new Location(player.getWorld(), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
					double distance = spawncenterloc.distance(landcenter);

					double land_jaop = BasicLandCalculation(blocki, distance);

					land_jaop = LandMarkjaoAdd(sender, cmd, commandLabel, args, land_jaop, landcenter);

					Method.SendMessage(sender, cmd, "この土地は土地ID「" + id + "」で登録されていますが、まだ取得されていません。(購入必要ポイント: " + ((int) land_jaop) + ")");
				}else{
					int blocki = 0;
					for(int i1=res.getInt("x2"); i1<=res.getInt("x1"); i1++){
						for(int k=res.getInt("z2"); k<=res.getInt("z1"); k++){
							blocki += 1;
						}
					}
					Location spawncenterloc = new Location(player.getWorld(), 0, 0, 0);
					Location landcenter = new Location(player.getWorld(), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
					double distance = spawncenterloc.distance(landcenter);

					double land_jaop = BasicLandCalculation(blocki, distance);

					land_jaop = LandMarkjaoAdd(sender, cmd, commandLabel, args, land_jaop, landcenter);

					Method.SendMessage(sender, cmd, "この土地は土地ID「" + id + "」で登録されています。既に" + res.getString("player") + "が取得しています。(購入必要ポイント: " + ((int) land_jaop) + ")");
				}
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
		}
	}

	private void Command_LandNew(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		if(!PermissionsEx.getUser(player).inGroup("Admin")){
			Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
			return;
		}
		LocalSession session = WorldEdit.getInstance().getSession(player.getName());
		RegionSelector regionSelector;
		try {
			regionSelector = session.getRegionSelector(session.getSelectionWorld());
		}catch(java.lang.NullPointerException e){
			Method.SendMessage(sender, cmd, "範囲を指定してください。");
			return;
		}
		// セレクタが立方体セレクタか判定
		if (!(session.getRegionSelector() instanceof CuboidRegionSelector)){
			Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
			return;
		}else{
			Statement statement;
			try {
				statement = MyMaid.c.createStatement();
			} catch (NullPointerException e) {
				MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
				try {
					MyMaid.c = MySQL.openConnection();
					statement = MyMaid.c.createStatement();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}

			statement = MySQL.check(statement);

			try {
				@SuppressWarnings("null")
				String worldname = regionSelector.getRegion().getWorld().getName();
				if(!worldname.equalsIgnoreCase("ReJao_Afa")){
					Method.SendMessage(sender, cmd, "このコマンドが使用できるワールドではありまぜん。");
					return;
				}
				//int i = regionSelector.getRegion().getArea();
				int x1 = regionSelector.getRegion().getMinimumPoint().getBlockX();
				int z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
				int x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
				int z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();

				if(x1 < x2){
					int x2_ = x1;
					x1 = x2;
					x2 = x2_;
				}

				int y1 = 255;
				int y2 = 0;

				if(z1 < z2){
					int z2_ = z1;
					z1 = z2;
					z2 = z2_;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

				statement.executeUpdate("INSERT INTO land (`isplayerland`, `x1`, `y1`, `z1`, `x2`, `y2`, `z2`, `createdate`) VALUES ('false', " + x1 + ", " + y1 + ", " + z1 + ", " + x2 + ", " + y2 + ", " + z2 + ", '" + sdf.format(new Date()) + "');");
				Method.SendMessage(sender, cmd, "操作に成功しました。");
				return;
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				e.printStackTrace();
				return;
			} catch (IncompleteRegionException e){
				Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
				return;
			}catch(java.lang.NullPointerException e){
				Method.SendMessage(sender, cmd, "範囲を指定してください。");
				return;
			}
		}
	}

	private void Command_LandList(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE uuid = '" + player.getUniqueId() + "';");
			int i = 0;
			while(res.next()){
				Method.SendMessage(sender, cmd, "ID: " + res.getInt("id") + " XYZ: " + res.getInt("x1") + " " + res.getInt("y1") + " " + res.getInt("z1") + " - " +  res.getInt("x2") + " " + res.getInt("y2") + " " + res.getInt("z2"));
				i += 1;
			}
			if(i == 0){
				Method.SendMessage(sender, cmd, "データがありません。");
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void Command_LandGet(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		int i;
		try{
			i = Integer.parseInt(args[1]);
		}catch (NumberFormatException e){
			Method.SendMessage(sender, cmd, "土地IDは数値で指定してください。");
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT COUNT(*) FROM land WHERE uuid = '" + player.getUniqueId() + "';");
			int count = 0;
			if(res.next()){
				count = res.getInt(1);
			}
			if(count >= 10){
				Method.SendMessage(sender, cmd, "あなたは既に土地を10つ取得しています。");
				return;
			}
			res = statement.executeQuery("SELECT * FROM land WHERE id = " + i + ";");
			if(!res.next()){
				Method.SendMessage(sender, cmd, "指定された土地はありません。");
				return;
			}else{
				int id = res.getInt("id");
				if(res.getBoolean("isplayerland")){
					Method.SendMessage(sender, cmd, "指定された土地は既に" + res.getString("player") + "が取得しています。");
					return;
				}

				int blocki = 0;
				for(int i1=res.getInt("x2"); i1<=res.getInt("x1"); i1++){
					for(int k=res.getInt("z2"); k<=res.getInt("z1"); k++){
						blocki += 1;
					}
				}
				Location spawncenterloc = new Location(player.getWorld(), 0, 0, 0);
				Location landcenter = new Location(Bukkit.getWorld("ReJao_Afa"), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
				double distance = spawncenterloc.distance(landcenter);

				double land_jaop = BasicLandCalculation(blocki, distance);

				land_jaop = LandMarkjaoAdd(sender, cmd, commandLabel, args, land_jaop, landcenter);

				if(!Pointjao.hasjao(player, (int) land_jaop)){
					Method.SendMessage(sender, cmd, "jaoPointが足りないため土地を購入できませんでした。(" + ((int) land_jaop) + "/" + Pointjao.getjao(player) + ")");
					return;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				statement.executeUpdate("UPDATE land SET player = '" + player.getName() + "', uuid = '" + player.getUniqueId() + "', isplayerland = true, date = '" + sdf.format(new Date()) + "' WHERE id = " + id + ";");

				Pointjao.usejao(player, (int) land_jaop, "landコマンドによる土地取得");

				Method.SendMessage(sender, cmd, "土地を取得しました。");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			e.printStackTrace();
			return;
		}
	}

	private void Command_LandSell(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		int i;
		try{
			i = Integer.parseInt(args[1]);
		}catch (NumberFormatException e){
			Method.SendMessage(sender, cmd, "土地IDは数値で指定してください。");
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE id = " + i + ";");
			if(!res.next()){
				Method.SendMessage(sender, cmd, "指定された土地はありません。");
				return;
			}else{
				int id = res.getInt("id");
				if(!res.getBoolean("isplayerland")){
					Method.SendMessage(sender, cmd, "この土地はまだ取得されていません。");
					return;
				}
				if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
					Method.SendMessage(sender, cmd, "この土地はあなたの土地では無いようです。");
					return;
				}

				int blocki = 0;
				for(int i1=res.getInt("x2"); i1<=res.getInt("x1"); i1++){
					for(int k=res.getInt("z2"); k<=res.getInt("z1"); k++){
						blocki += 1;
					}
				}
				Location spawncenterloc = new Location(player.getWorld(), 0, 0, 0);
				Location landcenter = new Location(player.getWorld(), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
				double distance = spawncenterloc.distance(landcenter);

				double land_jaop = BasicLandCalculation(blocki, distance);

				land_jaop = LandMarkjaoAdd(sender, cmd, commandLabel, args, land_jaop, landcenter);

				double land_jaop_sell = land_jaop / 2;

				for(int x=res.getInt("x2"); x<=res.getInt("x1"); x++){
					for(int y=res.getInt("y2"); y<=res.getInt("y1"); y++){
						for(int z=res.getInt("z2"); z<=res.getInt("z1"); z++){
							Material blocktype = null;
							if(y == 0){
								blocktype = Material.BEDROCK;
							}
							if(1 <= y && y <= 62){
								blocktype = Material.STONE;
							}
							if(63 <= y && y <= 66){
								blocktype = Material.DIRT;
							}
							if(y == 67){
								blocktype = Material.GRASS;
							}
							if(y >= 68){
								blocktype = Material.AIR;
							}
							Location blockloc = new Location(player.getWorld(), x, y, z);
							player.getWorld().getBlockAt(blockloc).setType(blocktype);
						}
					}
				}
				statement.executeUpdate("UPDATE land SET `player` = '', `uuid` = '', `isplayerland` = '0', `date` = '' WHERE `land`.`id` = " + id + ";");

				Pointjao.addjao(player, (int) land_jaop_sell, "landコマンドによる土地の売却");

				Method.SendMessage(sender, cmd, "土地を売りました。");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			e.printStackTrace();
			return;
		}
	}

	private void Command_LandTp(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		int i;
		try{
			i = Integer.parseInt(args[1]);
		}catch (NumberFormatException e){
			Method.SendMessage(sender, cmd, "土地IDは数値で指定してください。");
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE id = " + i + ";");
			if(!res.next()){
				Method.SendMessage(sender, cmd, "指定された土地はありません。");
				return;
			}else{
				Location landcenter = new Location(player.getWorld(), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
				landcenter.setY(getGroundPos(landcenter));

				player.teleport(landcenter);
				Method.SendMessage(sender, cmd, "土地ID「"+i+"」にテレポートしました。");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			e.printStackTrace();
			return;
		}
	}

	private void Command_LandSearch(CommandSender sender, Command cmd, String commandLabel, String[] args){
		int i;
		try{
			i = Integer.parseInt(args[1]);
		}catch (NumberFormatException e){
			Method.SendMessage(sender, cmd, "土地IDは数値で指定してください。");
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE id = " + i + ";");
			if(!res.next()){
				Method.SendMessage(sender, cmd, "指定された土地はありません。");
				return;
			}
			if(res.getBoolean("isplayerland")){
				int blocki = 0;
				for(int i1=res.getInt("x2"); i1<=res.getInt("x1"); i1++){
					for(int k=res.getInt("z2"); k<=res.getInt("z1"); k++){
						blocki += 1;
					}
				}
				Location spawncenterloc = new Location(Bukkit.getWorld("ReJao_Afa"), 0, 0, 0);
				Location landcenter = new Location(Bukkit.getWorld("ReJao_Afa"), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
				double distance = spawncenterloc.distance(landcenter);

				double land_jaop = BasicLandCalculation(blocki, distance);

				land_jaop = LandMarkjaoAdd(sender, cmd, commandLabel, args, land_jaop, landcenter);

				Method.SendMessage(sender, cmd, "指定された土地「" + i + "」は所有されている土地です。");
				Method.SendMessage(sender, cmd, "所有者: " + res.getString("player"));
				Method.SendMessage(sender, cmd, "XYZ1: " + res.getInt("x1") + " " + res.getInt("y1") + " " + res.getInt("z1"));
				Method.SendMessage(sender, cmd, "XYZ2: " + res.getInt("x2") + " " + res.getInt("y2") + " " + res.getInt("z2"));
				Method.SendMessage(sender, cmd, "値段: " + ((int)land_jaop) + "jao (ランドマーク計算無: " + ((int)BasicLandCalculation(blocki, distance)) + "jao)");
				Method.SendMessage(sender, cmd, "土地登録日付: " + res.getString("createdate"));
				Method.SendMessage(sender, cmd, "土地取得日付: " + res.getString("date"));
			}else{
				int blocki = 0;
				for(int i1=res.getInt("x2"); i1<=res.getInt("x1"); i1++){
					for(int k=res.getInt("z2"); k<=res.getInt("z1"); k++){
						blocki += 1;
					}
				}
				Location spawncenterloc = new Location(Bukkit.getWorld("ReJao_Afa"), 0, 0, 0);
				Location landcenter = new Location(Bukkit.getWorld("ReJao_Afa"), (res.getInt("x1") + res.getInt("x2")) / 2, 68, (res.getInt("z1") + res.getInt("z2")) / 2);
				double distance = spawncenterloc.distance(landcenter);

				double land_jaop = BasicLandCalculation(blocki, distance);

				land_jaop = LandMarkjaoAdd(sender, cmd, commandLabel, args, land_jaop, landcenter);

				Method.SendMessage(sender, cmd, "指定された土地「" + i + "」は所有されていない土地です。");
				Method.SendMessage(sender, cmd, "XYZ1: " + res.getInt("x1") + " " + res.getInt("y1") + " " + res.getInt("z1"));
				Method.SendMessage(sender, cmd, "XYZ2: " + res.getInt("x2") + " " + res.getInt("y2") + " " + res.getInt("z2"));
				Method.SendMessage(sender, cmd, "値段: " + ((int)land_jaop) + "jao (ランドマーク計算無: " + ((int)BasicLandCalculation(blocki, distance)) + "jao)");
				Method.SendMessage(sender, cmd, "土地登録日付: " + res.getString("createdate"));
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			e.printStackTrace();
			return;
		}
	}

	private void Command_LandNewMark(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		double i;
		try{
			i = Double.parseDouble(args[1]);
		}catch (NumberFormatException e){
			Method.SendMessage(sender, cmd, "ランドマーク価値は浮動小数点数で指定してください。");
			return;
		}
		if(i < 0){
			Method.SendMessage(sender, cmd, "ランドマーク価値は0以上で指定してください。");
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		Location loc = player.getLocation();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			statement.execute("INSERT INTO land_mark (value, x, y, z, date) VALUES (" + i + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", '" + sdf.format(new Date()) + "');");
			Method.SendMessage(sender, cmd, "ランドマークを追加しました。");
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return;
		}
	}


	// ---------------------------------------- //
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			return;
		}
		if(PermissionsEx.getUser(player).inGroup("Admin")){
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため設置行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}else{
					player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため設置行為をすることはできません。");
					event.setCancelled(true);
					return;
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため設置行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			return;
		}
		if(PermissionsEx.getUser(player).inGroup("Admin")){
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため破壊行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}else{
					player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため破壊行為をすることはできません。");
					event.setCancelled(true);
					return;
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため破壊行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			return;
		}
		if(PermissionsEx.getUser(player).inGroup("Admin")){
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため着火等行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}else{
					player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため着火等行為をすることはできません。");
					event.setCancelled(true);
					return;
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため着火等行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			return;
		}
		if(PermissionsEx.getUser(player).inGroup("Admin")){
			return;
		}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlockClicked().getLocation().getBlockX() + " AND y1 >= " + event.getBlockClicked().getLocation().getBlockY() + " AND z1 >= " + event.getBlockClicked().getLocation().getBlockZ() + " AND x2 <= " + event.getBlockClicked().getLocation().getBlockX() + " AND y2 <= " + event.getBlockClicked().getLocation().getBlockY() + " AND z2 <= " + event.getBlockClicked().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため着火等行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}else{
					player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため着火等行為をすることはできません。");
					event.setCancelled(true);
					return;
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため着火等行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent event){
		for(Block block : event.blockList()){
			if(!block.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
				return;
			}
			event.setCancelled(true);
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockFromToEvent(BlockFromToEvent event){
		if(!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			return;
		}
		Statement statement;
		Statement statement2;
		try {
			statement = MyMaid.c.createStatement();
			statement2 = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
				statement2 = MyMaid.c.createStatement();
				event.setCancelled(true);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}

		statement = MySQL.check(statement);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			boolean whileboo = true;
			while(res.next()){
				int id = res.getInt("id");
				ResultSet res_to = statement2.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getToBlock().getLocation().getBlockX() + " AND y1 >= " + event.getToBlock().getLocation().getBlockY() + " AND z1 >= " + event.getToBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getToBlock().getLocation().getBlockX() + " AND y2 <= " + event.getToBlock().getLocation().getBlockY() + " AND z2 <= " + event.getToBlock().getLocation().getBlockZ() + ";");

				if(res_to.next()){
					if(id != res_to.getInt("id")){
						event.setCancelled(true);
					}
				}else{
					event.setCancelled(true);
				}
				whileboo = false;
			}
			if(whileboo){
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event){
		for(Block block : event.getBlocks()){
			if(!block.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
				return;
			}
			Statement statement;
			Statement statement2;
			try {
				statement = MyMaid.c.createStatement();
				statement2 = MyMaid.c.createStatement();
			} catch (NullPointerException e) {
				MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
				try {
					MyMaid.c = MySQL.openConnection();
					statement = MyMaid.c.createStatement();
					statement2 = MyMaid.c.createStatement();
					event.setCancelled(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					event.setCancelled(true);
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return;
			}

			statement = MySQL.check(statement);
			statement2 = MySQL.check(statement2);

			try {
				ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
				boolean whileboo = true;
				while(res.next()){
					int id = res.getInt("id");
					ResultSet res_to = statement2.executeQuery("SELECT * FROM land WHERE x1 >= " + block.getLocation().getBlockX() + " AND y1 >= " + block.getLocation().getBlockY() + " AND z1 >= " + block.getLocation().getBlockZ() + " AND x2 <= " + block.getLocation().getBlockX() + " AND y2 <= " + block.getLocation().getBlockY() + " AND z2 <= " + block.getLocation().getBlockZ() + ";");

					if(res_to.next()){
						if(id != res_to.getInt("id")){
							event.setCancelled(true);
						}
					}else{
						event.setCancelled(true);
					}
					whileboo = false;
				}
				if(whileboo){
					event.setCancelled(true);
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				event.setCancelled(true);
				return;
			}
		}
	}

	// WorldEdit対策(?)
	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldEditInteract(PlayerInteractEvent event){
		if(event.getAction() == Action.LEFT_CLICK_BLOCK){
			if(!(event.getPlayer().getItemInHand().getType() == Material.WOOD_AXE)){
				return;
			}
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();

			if(!block.getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
				return;
			}

			if(PermissionsEx.getUser(player).inGroup("Admin")){
				return;
			}
			Statement statement;
			try {
				statement = MyMaid.c.createStatement();
			} catch (NullPointerException e) {
				MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
				try {
					MyMaid.c = MySQL.openConnection();
					statement = MyMaid.c.createStatement();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
					player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}

			statement = MySQL.check(statement);

			try {
				ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + block.getLocation().getBlockX() + " AND y1 >= " + block.getLocation().getBlockY() + " AND z1 >= " + block.getLocation().getBlockZ() + " AND x2 <= " + block.getLocation().getBlockX() + " AND y2 <= " + block.getLocation().getBlockY() + " AND z2 <= " + block.getLocation().getBlockZ() + ";");
				if(res.next()){
					if(res.getBoolean("isplayerland")){
						if(!res.getString("uuid").equalsIgnoreCase("" + player.getUniqueId())){
							player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いためWorldEditで選択できません。");
							event.setCancelled(true);
							return;
						}
					}else{
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いためWorldEditで選択できません。");
						event.setCancelled(true);
						return;
					}
				}else{
					player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いためWorldEditで選択できません。");
					event.setCancelled(true);
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				event.setCancelled(true);
				return;
			}
		}else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(!(event.getPlayer().getItemInHand().getType() == Material.WOOD_AXE)){
				return;
			}
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();

			if(!block.getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
				return;
			}

			if(PermissionsEx.getUser(player).inGroup("Admin")){
				return;
			}
			Statement statement;
			try {
				statement = MyMaid.c.createStatement();
			} catch (NullPointerException e) {
				MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
				try {
					MyMaid.c = MySQL.openConnection();
					statement = MyMaid.c.createStatement();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
					player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}

			statement = MySQL.check(statement);

			try {
				ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + block.getLocation().getBlockX() + " AND y1 >= " + block.getLocation().getBlockY() + " AND z1 >= " + block.getLocation().getBlockZ() + " AND x2 <= " + block.getLocation().getBlockX() + " AND y2 <= " + block.getLocation().getBlockY() + " AND z2 <= " + block.getLocation().getBlockZ() + ";");
				if(res.next()){
					if(res.getBoolean("isplayerland")){
						if(!res.getString("uuid").equalsIgnoreCase("" + player.getUniqueId())){
							player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いためWorldEditで選択できません。");
							event.setCancelled(true);
							return;
						}
					}else{
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いためWorldEditで選択できません。");
						event.setCancelled(true);
						return;
					}
				}else{
					player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いためWorldEditで選択できません。");
					event.setCancelled(true);
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				event.setCancelled(true);
				return;
			}
		}
	}
	/**
	 * 指定した地点の地面の高さを返す
	 *
	 * @param loc
	 *            地面を探したい場所の座標
	 * @return 地面の高さ（Y座標）
	 *
	 * http://www.jias.jp/blog/?57
	 */
	private int getGroundPos(Location loc) {

		// 最も高い位置にある非空気ブロックを取得
		loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

		// 最後に見つかった地上の高さ
		int ground = loc.getBlockY();

		// 下に向かって探索
		for (int y = loc.getBlockY(); y != 0; y--) {
			// 座標をセット
			loc.setY(y);

			// そこは太陽光が一定以上届く場所で、非固体ブロックで、ひとつ上も非固体ブロックか
			if (loc.getBlock().getLightFromSky() >= 8
					&& !loc.getBlock().getType().isSolid()
					&& !loc.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
				// 地上の高さとして記憶しておく
				ground = y;
			}
		}

		// 地上の高さを返す
		return ground;
	}
}
