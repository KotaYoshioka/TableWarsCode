package tw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;


public class TMGame implements Listener{

	Plugin plugin;
	World world;
	TMGame tmg;
	
	//????????????????????????
	boolean nowgame = false;
	//?????????????????????????????????
	List<Player> players = new ArrayList<Player>();
	//?????????????????????????????????
	List<Player> livings = new ArrayList<Player>();
	//???????????????????????????
	HashMap<Player,TWPlayer> playerdata = new HashMap<Player,TWPlayer>();
	//???????????????
	HashMap<Location,ItemStack> treasures = new HashMap<Location,ItemStack>();
	//?????????????????????????????????
	HashMap<LivingEntity,TWEntity> entities = new HashMap<LivingEntity,TWEntity>();
	//??????????????????
	boolean morning = false;
	//????????????????????? 0:?????? 1:???1?????? 2:????????????
	int phase = 0;
	//??????????????????
	int minutes = 25;
	int seconds = 0;
	double secondCount = 0;
	double[] tickLong = {30000d,12000d};
	BossBar bb;
	
	//????????????
	////[0]?????????
	////[1]??????????????????
	////[2]?????????
	////[3]????????????
	////[4]?????????
	////[5]?????????????????????
	////[6]?????????
	////[7]??????(?????????)
	////[8]????????????
	//?????????????????????
	Vector[][] locations = {
			{new Vector(0.5,-26,-216.5),new Vector(0.5,-17.2,-222.5),new Vector(0.5,-22,-223.5),new Vector(0.5,-26,-185.5),new Vector(-3.5,-26,-199.5),new Vector(0.5,-21,-181.5),new Vector(4.5,-26,-199.5),new Vector(-8.5,-34.5,-199.5),new Vector(0.5,-26,-199.5)},
			{new Vector(0.5,-26,217.5),new Vector(0.5,-17.2,223.5),new Vector(0.5,-22,224.5),new Vector(0.5,-26,186.5),new Vector(4.5,-26,200.5),new Vector(0.5,-21,182.5),new Vector(-3.5,-26,200.5),new Vector(8.5,-34.5,200.5),new Vector(0.5,-26,199.5)},
			{new Vector(217.5,-26,0.5),new Vector(223.5,-17.2,0.5),new Vector(224.5,-22,0.5),new Vector(186.5,-26,0.5),new Vector(200.5,-26,-3.5),new Vector(182.5,-21,0.5),new Vector(200.5,-26,4.5),new Vector(200.5,-34.5,-7.5),new Vector(201.5,-26,0.5)},
			{new Vector(-216.5,-26,0.5),new Vector(-222.5,-17.2,0.5),new Vector(-223.5,-22,0.5),new Vector(-185.5,-26,0.5),new Vector(-199.5,-26,4.5),new Vector(-181.5,-21,0.5),new Vector(-199.5,-26,-3.5),new Vector(-199.5,-34.5,8.5),new Vector(-200.5,-26,0.5)}
	};
	//????????????
	Vector[] shipRotate = {
		new Vector(0,0,1),new Vector(0,0,-1),new Vector(-1,0,0),new Vector(1,0,0)	
	};
	
	//???????????????
	Vector[] lizes = {
			new Vector(26.1,2,-29.5),new Vector(23.5,2,-32.1),new Vector(20.8,2,-29.5),new Vector(23.5,2,-26.7)
	};
	
	Vector[] lizeRotate = {
			new Vector(1,0,0),new Vector(0,0,-1),new Vector(-1,0,0),new Vector(0,0,1)
	};
	
	//??????????????????
	Vector[] ghostship = {
			new Vector(202.5,-27,200.5),new Vector(-202.5,-27,-200)
	};
	
	List<LivingEntity> ghosts = new ArrayList<LivingEntity>();
	
	HashMap<Villager,Integer> lizas = new HashMap<Villager,Integer>();
	
	HashMap<Integer,List<ItemStack>> lizaitems = new HashMap<Integer,List<ItemStack>>();
	HashMap<Integer,List<ItemStack>> lizabooks = new HashMap<Integer,List<ItemStack>>();
	
	//?????????????????????????????????
	////[0]?????????
	////[1]????????????
	////[2]?????????
	////[3]?????????????????????
	////[4]?????????
	int[][] levels = {
			{0,-1,-1,-1,-1},
			{0,-1,-1,-1,-1},
			{0,-1,-1,-1,-1},
			{0,-1,-1,-1,-1}
	};
	
	//???????????????????????????
	//??????????????????
	//?????????7????????????34????????????62????????????
	////[0]????????????????????????
	////[1]????????????????????????
	////[2]???????????????????????????
	boolean[][][] trapActive = {
			{
				{false,false,false,false,false},
				{false,false,false,false,false},
				{false,false,false,false,false}
			},
			{
				{false,false,false,false,false},
				{false,false,false,false,false},
				{false,false,false,false,false}
			},
			{
				{false,false,false,false,false},
				{false,false,false,false,false},
				{false,false,false,false,false}
			},
			{
				{false,false,false,false,false},
				{false,false,false,false,false},
				{false,false,false,false,false}
			}
	};
	
	//??????????????????????????????
	Object[][][] selfshop = {
			{
				{null,null},
				{null,null},
				{null,null},
			},
			{
				{null,null},
				{null,null},
				{null,null},
			},
			{
				{null,null},
				{null,null},
				{null,null},
			},
			{
				{null,null},
				{null,null},
				{null,null},
			}
	};
	
	public TMGame(Plugin plugin) {
		this.tmg = this;
		this.plugin = plugin;
		createNewWorld(Bukkit.getWorld("TableWarsAlter"));
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		for(int i = 0 ; i < 4 ; i++) {
			List<ItemStack> lizas = new ArrayList<ItemStack>();
			lizas.addAll(TWData.getShopItemsLiza());
			lizaitems.put(i, lizas);
			List<ItemStack> books = new ArrayList<ItemStack>();
			books.addAll(TWData.getBookLiza());
			lizabooks.put(i, books);
		}
	}
	
	public void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat","session.lock"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	void createNewWorld(World world) {
		File worldDir = world.getWorldFolder();
		String newName = world.getName() + UUID.randomUUID().toString();
		try {
			FileUtils.copyDirectory(worldDir, new File(worldDir.getParent(),newName));
		} catch (IOException e) {
			// TODO ????????????????????? catch ????????????
			e.printStackTrace();
		}
		WorldCreator creator = new WorldCreator(newName);
		this.world = Bukkit.createWorld(creator);
		world.setGameRule(GameRule.DO_TILE_DROPS, false);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
	}
	
	public void start() {
		//?????????
		nowgame = true;
		morning = new Random().nextBoolean();
		for(int i = 0 ; i < 4 ; i++) {
			for(int j = 3 ; j <=6 ; j++) {
				locations[i][j].toLocation(world).getBlock().setType(Material.AIR);
			}
		}
		bb = Bukkit.createBossBar(ChatColor.GREEN + "?????????????????????" + minutes + "???" + seconds + "???",BarColor.GREEN,BarStyle.SOLID);
		//???????????????????????????
		for(Player p:players) {
			livings.add(p);
			int index = getPlayerIndex(p);
			p.teleport(locations[index][0].toLocation(world));
			locations[index][2].toLocation(world).getBlock().setType(Material.CRAFTING_TABLE);
			playerdata.put(p, new TWPlayer(plugin,this,p));
			p.sendTitle(ChatColor.YELLOW + "????????????",(!morning?ChatColor.RED:ChatColor.BLUE) + "?????????" + (!morning?"???":"???"),10,100,10);
			spawnexp(index);
			bb.addPlayer(p);
		}
		//?????????????????????
		for(int i = 0 ; i < 50 ; i ++) {
			Random rnd = new Random();
			Location l = new Vector(15 + ((rnd.nextBoolean()?-1:1) * rnd.nextInt(70)),56,-19 + ((rnd.nextBoolean()?-1:1) * rnd.nextInt(70))).toLocation(world);
			ItemStack treasure = TWData.getTreasure();
			treasures.put(l, treasure);
		}
		//???????????????
		for(int i = 0 ; i < 4 ; i++) {
			Location l = lizes[i].toLocation(world);
			l.setDirection(lizeRotate[i]);
			Villager v = (Villager)world.spawnEntity(l, EntityType.VILLAGER);
			v.setAI(false);
			v.setNoDamageTicks(9999999);
			lizas.put(v,i);
		}
		WorldBorder wb  = world.getWorldBorder();
		wb.setCenter(new Vector(0,0,0).toLocation(world));
		wb.setSize(500);
		countdown(wb);
		morningTimer();
		trap();
	}
	
	void morningTimer() {
		new BukkitRunnable() {
			public void run() {
				if(!nowgame) {
					this.cancel();
					return;
				}
				morning = !morning;
				world.setTime(morning?1000:18000);
				if(morning) {
					for(LivingEntity ghost:ghosts) {
						entities.get(ghost).death();
					}
					ghosts.clear();
				}else {
					TWSummon tws = new TWSummon(plugin,tmg);
					for(int i = 0 ; i < 25 ; i++) {
						if(i < 15) {
							ghosts.add(tws.summonGhost(EntityType.ZOMBIE, ghostship[0].toLocation(world)));
							ghosts.add(tws.summonGhost(EntityType.ZOMBIE, ghostship[1].toLocation(world)));
						}else if(i < 24) {
							ghosts.add(tws.summonGhost(EntityType.SKELETON, ghostship[0].toLocation(world)));
							ghosts.add(tws.summonGhost(EntityType.SKELETON, ghostship[1].toLocation(world)));
						}else {
							ghosts.add(tws.summonGhost(EntityType.WITHER_SKELETON, ghostship[0].toLocation(world)));
							ghosts.add(tws.summonGhost(EntityType.WITHER_SKELETON, ghostship[1].toLocation(world)));
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0, 12000);
	}
	
	public void countdown(WorldBorder wb) {
		new BukkitRunnable() {
			public void run() {
				if(seconds == 0) {
					if(minutes != 0) {
						minutes--;
						seconds = 59;
					}else {
						phase++;
						if(phase == 1) {
							bb.setColor(BarColor.YELLOW);
							minutes = 10;
							seconds = 0;
							wb.setSize(250,20);
						}else {
							bb.setTitle(ChatColor.RED + "????????????");
							bb.setProgress(1);
							bb.setColor(BarColor.RED);
							wb.setSize(50,20);
							return;
						}
					}
				}else {
					seconds--;
				}
				String title = (phase==0?ChatColor.GREEN:ChatColor.YELLOW) + "?????????????????????" + minutes + "???" + seconds + "???";
				bb.setTitle(title);
				secondCount++;
				bb.setProgress((double)secondCount/tickLong[phase]);
				countdown(wb);
			}
		}.runTaskLater(plugin, 20);
	}
	
	/*
	public void timer(WorldBorder wb) {
		new BukkitRunnable() {
			public void run() {
				if(!nowgame) {
					return;
				}
				wb.setSize(250, 20);
				lastTimer(wb);
			}
		}.runTaskLater(plugin, 30000);
		new BukkitRunnable() {
			public void run() {
				for(Player p:players) {
					p.sendMessage(ChatColor.RED + "??????2????????????");
				}
			}
		}.runTaskLater(plugin, 27600);
		new BukkitRunnable() {
			public void run() {
				for(Player p:players) {
					p.sendMessage(ChatColor.RED + "??????1????????????");
				}
			}
		}.runTaskLater(plugin, 28800);
	}
	
	public void lastTimer(WorldBorder wb) {
		new BukkitRunnable() {
			public void run() {
				if(!nowgame) {
					return;
				}
				wb.setSize(50,20);
			}
		}.runTaskLater(plugin, 5000);
	}
	*/
	
	public void dig(Player player) {
		int level = new TWEnchant(player,plugin,this).onDig();
		for(Location l:treasures.keySet()) {
			Location pl = player.getLocation();
			double px = pl.getX();
			double pz = pl.getZ();
			double lx = l.getX();
			double lz = l.getZ();
			double error = 0.8d + (1 * level);
			if(px < lx + error && px > lx - error) {
				if(pz < lz + error && pz > lz - error) {
					player.getInventory().addItem(treasures.get(l));
					treasures.remove(l);
					player.sendMessage(ChatColor.GOLD + "??????????????????????????????");
					return;
				}
			}
		}
		player.sendMessage(ChatColor.RED + "???????????????????????????????????????...");
	}
	
	/**
	 * ??????????????????????????????????????????????????????
	 */
	void spawnexp(int id) {
		new BukkitRunnable() {
			public void run() {
				if(!nowgame) {
					return;
				}
				ExperienceOrb orb = world.spawn(locations[id][1].toLocation(world), ExperienceOrb.class);
				orb.setExperience(10);
				spawnexp(id);
			}
		}.runTaskLater(plugin, 100 - (levels[id][0] * 20));
	}
	
	void trap() {
		new BukkitRunnable() {
			public void run() {
				if(!nowgame) {
					this.cancel();
					return;
				}
				for(int i = 0 ; i < 4 ; i++) {
					Location center = locations[i][8].toLocation(world);
					int[] radius = {7,34,62};
					for(int k = 0 ; k < 3 ; k++) {
						boolean b = false;
						for(int j = 0 ; j < 3 ; j++) {
							if(trapActive[i][k][j]) {
								b = true;
								break;
							}
						}
						if(b) {
							List<Player> targets = new ArrayList<Player>();
							for(Entity ent : world.getNearbyEntities(center, radius[k], 10, radius[k])) {
								if(livings.contains(ent)) {
									Player target = (Player)ent;
									if(i != getPlayerIndex(target)) {
										targets.add(target);
									}
								}
							}
							if(targets.size() > 0) {
								for(int j = 0 ; j < 3 ; j++) {
									if(trapActive[i][k][j]) {
										for(Player p:targets) {
											switch(j) {
											case 0:
												players.get(i).sendTitle(ChatColor.RED + "???????????????", ChatColor.RED + "??????????????????????????????",20,100,20);
												break;
											case 1:
												p.setFireTicks(400);
												break;
											case 2:
												playerdata.get(p).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,400,1));
												break;
											case 3:
												playerdata.get(p).addPotionEffect(new PotionEffect(PotionEffectType.POISON,200,3));
												break;
											case 4:
												p.getInventory().setHelmet(new ItemStack(Material.AIR));
												p.getInventory().setChestplate(new ItemStack(Material.AIR));
												p.getInventory().setLeggings(new ItemStack(Material.AIR));
												p.getInventory().setBoots(new ItemStack(Material.AIR));
												break;
											}
										}
										trapActive[i][k][j] = false;
									}
								}
							}
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0, 5);
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if(livings.contains(e.getDamager())) {
			Player attacker = (Player)e.getDamager();
			if(e.getEntity() instanceof LivingEntity) {
				LivingEntity le = (LivingEntity)e.getEntity();
				new TWEnchant(attacker,le,plugin,this).onAttack();
			}
		}else if(livings.contains(e.getEntity())) {
			if(e.getDamager() instanceof LivingEntity) {
				Player target = (Player)e.getEntity();
				LivingEntity attacker = (LivingEntity)e.getDamager();
				new TWEnchant(target,attacker,plugin,this).onAttacked();
			}
		}
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if(livings.contains(e.getEntity())) {
			Player attacker = (Player)e.getEntity();
			if(e.getProjectile() instanceof Arrow) {
				new TWEnchant(attacker,plugin,this).onShoot(attacker,(Arrow)e.getProjectile());
			}
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(livings.contains(e.getPlayer())) {
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player clicker = e.getPlayer();
				new TWEnchant(clicker,plugin,this).onRightClick();
			}
		}
	}
	
	@EventHandler
	public void onRightClickAtEntity(PlayerInteractAtEntityEvent e) {
		if(livings.contains(e.getPlayer())) {
			Player clicker = e.getPlayer();
			new TWEnchant(clicker,plugin,this).onRightClickAtEntity();
		}
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		if(livings.contains(e.getPlayer())) {
			Player sneaker = e.getPlayer();
			new TWEnchant(sneaker,plugin,this).onSneak();
		}
	}
	
	@EventHandler
	public void onEXP(PlayerExpChangeEvent e) {
		if(livings.contains(e.getPlayer())) {
			Player exper = e.getPlayer();
			int newexp = new TWEnchant(exper,plugin,this).onEXP(e.getAmount());
			e.setAmount(newexp);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPut(BlockPlaceEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlock(BlockCanBuildEvent e) {
		e.setBuildable(false);
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent e) {
		if(livings.contains(e.getEntity())) {
			Player player = (Player)e.getEntity();
			if(player.getHealth() - e.getDamage() <= 0) {
				if(player.getInventory().contains(Material.TOTEM_OF_UNDYING)) {
					return;
				}
				if(new TWEnchant(player,plugin,this).containsEnchant("?????????")) {
					e.setCancelled(true);
					player.getInventory().clear();
					player.setVelocity(new Vector(0,10,0));
					player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					return;
				}
				e.setCancelled(true);
				livings.remove(player);
				player.setGameMode(GameMode.SPECTATOR);
				if(livings.size() == 1) {
					win();
				}else {
					for(Player p:players) {
						p.sendTitle(player.getDisplayName() + "???????????????", "??????" + livings.size() + "???",20,100,20);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDeathGhost(EntityDeathEvent e) {
		//??????????????????????????????
		if(ghosts.contains(e.getEntity())) {
			ghosts.remove(e.getEntity());
			int exp;
			switch(e.getEntity().getType()) {
			case SKELETON:
				exp = 150;
				break;
			case WITHER_SKELETON:
				exp = 1000;
				world.dropItem(e.getEntity().getLocation(), TWData.getTreasureBox());
				break;
			default:
				exp = 100;
				break;
			}
			e.setDroppedExp(exp);
			Bukkit.getLogger().info("????????????");
		}
		if(entities.containsKey(e.getEntity())) {
			for(Entity ent:e.getEntity().getNearbyEntities(5, 5, 5)) {
				if(livings.contains(ent)) {
					Player player = (Player)ent;
					new TWEnchant(player,plugin,this).onDeathNearly();
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(livings.contains(e.getEntity())) {
			Player player = (Player)e.getEntity();
			new TWEnchant(player,plugin,this).onDamage(e.getDamage());
		}
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(livings.contains(e.getTarget())) {
			Player player = (Player)e.getTarget();
			e.setCancelled(new TWEnchant(player,plugin,this).onTarget());
		}
	}
	
	void win() {
		for(Player p:players) {
			p.sendTitle(livings.get(0).getDisplayName() + "????????????", "",20,100,20);
		}
		new BukkitRunnable() {
			public void run() {
				end();
			}
		}.runTaskLater(plugin, 140);
	}
	
	public void join(Player player) {
		if(!players.contains(player)) {
			if(players.size() < 4) {
				players.add(player);	
			}
		}
	}
	
	public void end() {
		nowgame = false;
		bb.removeAll();
		HandlerList.unregisterAll(this);
		world.getWorldBorder().reset();
		for(Player p:players) {
			playerdata.get(p).cancelEvents();
		}
		for(LivingEntity ent: entities.keySet()) {
			ent.damage(1000);
		}
		for(Villager v:lizas.keySet()) {
			v.remove();
		}
		for(Entity ent:world.getEntities()) {
			if(ent instanceof Boat || ent instanceof ExperienceOrb) {
				ent.remove();
			}
		}
	}
	
	/**
	 * ??????????????????????????????????????????????????????????????????????????????????????????-1?????????????????????
	 * @param player
	 * @return
	 */
	public int getPlayerIndex(Player player) {
		for(int i = 0 ; i < players.size() ; i++) {
			if(players.get(i) == player)return i;
		}
		return -1;
	}
	
	public int getTableIndex(Block table) {
		Location l = table.getLocation();
		for(int i = 0 ; i < locations.length ; i++) {
			for(int k = 2 ; k < locations[i].length ; k++) {
				Location cl = locations[i][k].toLocation(world).getBlock().getLocation();
				if(cl.getX() == l.getX() && cl.getY() == l.getY() && cl.getZ() == l.getZ()) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public TWPlayer getPlayerData(Player player) {
		return playerdata.get(player);
	}
	
	public boolean containsLivings(Entity ent) {
		return livings.contains(ent);
	}
	
	public boolean containsLivingEntity(LivingEntity ent) {
		return entities.containsKey(ent);
	}
	public void addLivingEntity(LivingEntity le,TWEntity twe) {
		entities.put(le, twe);
	}
	
	public int getTableLevel(int shipIndex, int tableIndex) {
		return levels[shipIndex][tableIndex];
	}
	public void setTableLevel(int shipIndex,int tableIndex, int newlevel) {
		levels[shipIndex][tableIndex] = newlevel;
	}
	
	public Location getSettingLocation(int shipIndex, int index) {
		return locations[shipIndex][index].toLocation(world);
	}
	
	public Vector getShipRotate(int shipIndex) {
		return shipRotate[shipIndex];
	}
	
	public boolean getTrap(int shipIndex,int trapLevel, int trapIndex) {
		return trapActive[shipIndex][trapLevel][trapIndex];
	}
	public void setTrap(int shipIndex,int trapLevel,int trapIndex,boolean set) {
		trapActive[shipIndex][trapLevel][trapIndex] = set;
	}
	
	public int getTreasureSize() {
		return treasures.size();
	}
	public HashMap<Location,ItemStack> getTreasures(){
		return treasures;
	}
	
	public int getUnusedSelfIndex(int playerindex) {
		for(int i = 0 ; i < 3 ; i++) {
			if(selfshop[playerindex][i][0] == null) {
				return i;
			}
		}
		return -1;
	}
	public Object[] getSelf(int playerindex,int index) {
		return selfshop[playerindex][index];
	}
	public void setSelf(int playerindex,int index,ItemStack setItem,int price) {
		selfshop[playerindex][index][0] = setItem;
		selfshop[playerindex][index][1] = price;
	}
	
	public List<ItemStack> getLizaItems(int index){
		return lizaitems.get(index);
	}
	public void removeLizaItems(int index,ItemStack item) {
		lizaitems.get(index).remove(item);
	}
	public List<ItemStack> getLizaBooks(int index){
		return lizabooks.get(index);
	}
	public void removeLizaBooks(int index,ItemStack item) {
		lizabooks.get(index).remove(item);
	}
}
