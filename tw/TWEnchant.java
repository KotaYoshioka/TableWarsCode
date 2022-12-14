package tw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import inv.TWReview;
import net.md_5.bungee.api.ChatColor;

public class TWEnchant {

	Player user;
	LivingEntity target;
	Plugin plugin;
	TMGame tmg;
	HashMap<String,Integer> enchants = new HashMap<String,Integer>();
	
	public TWEnchant(Player user,LivingEntity target,Plugin plugin,TMGame tmg) {
		this.user = user;
		this.target = target;
		this.plugin = plugin;
		this.tmg = tmg;
		confirmEnchants();
	}
	
	public TWEnchant(Player user,Plugin plugin,TMGame tmg) {
		this.user = user;
		this.plugin = plugin;
		this.tmg = tmg;
		confirmEnchants();
	}
	
	void confirmEnchants() {
		if(user.getInventory().getItemInMainHand() != null) {
			if(user.getInventory().getItemInMainHand().getType() != Material.AIR) {
				getEnchants(user.getInventory().getItemInMainHand());
			}
		}
		if(user.getInventory().getHelmet() != null) {
			if(user.getInventory().getHelmet().getType() != Material.AIR) {
				getEnchants(user.getInventory().getHelmet());
			}
		}
		if(user.getInventory().getChestplate() != null) {
			if(user.getInventory().getChestplate().getType() != Material.AIR) {
				getEnchants(user.getInventory().getChestplate());
			}
		}
		if(user.getInventory().getLeggings() != null) {
			if(user.getInventory().getLeggings().getType() != Material.AIR) {
				getEnchants(user.getInventory().getLeggings());
			}
		}
		if(user.getInventory().getBoots() != null) {
			if(user.getInventory().getBoots().getType() != Material.AIR) {
				getEnchants(user.getInventory().getBoots());
			}
		}
	}
	
	void getEnchants(ItemStack item) {
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if(meta.hasLore()) {
				List<String> lore = meta.getLore();
				Pattern p = Pattern.compile(".*Lv\\.\\d");
				for(String s:lore) {
					if(p.matcher(s).find()) {
						Pattern lp = Pattern.compile("Lv\\.\\d");
						Matcher lm = lp.matcher(s);
						if(lm.find()) {
							String slevel = lm.group();
							slevel = ChatColor.stripColor(slevel.replace("Lv.",""));
							Pattern ep = Pattern.compile("Lv\\.\\d");
							Matcher em = ep.matcher(s);
							String ename = ChatColor.stripColor(em.replaceAll(""));
							enchants.put(ename, Integer.parseInt(slevel));
						}
					}
				}
			}
		}
	}
	
	public void onAttack() {
		//?????????????????????
		if(enchants.containsKey("??????")) {
			Bukkit.getLogger().info("yeah");
			double newhp = user.getHealth() + enchants.get("??????");
			if(newhp > user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				newhp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			}
			user.setHealth(newhp);
		}
		if(enchants.containsKey("??????")) {
			user.setVelocity(user.getLocation().getDirection().multiply(-1 * enchants.get("??????")));
		}
		if(enchants.containsKey("?????????????????????")) {
			int rndm = new Random().nextInt(60);
			if(rndm == 0) {
				user.getInventory().addItem(TWData.getTreasureBox());
				user.sendMessage(ChatColor.GOLD + "???????????????????????????");
			}
		}
		//?????????????????????
		if(target != null) {
			if(enchants.containsKey("??????")) {
				int level = enchants.get("??????");
				if(target instanceof Player) {
					tmg.getPlayerData((Player)target).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,(20*level),2));
				}else {
					target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,(20*level),2));
				}
			}
			if(enchants.containsKey("????????????")) {
				int level = enchants.get("????????????");
				target.damage(level);
			}
			if(enchants.containsKey("??????")) {
				int level = enchants.get("??????");
				new BukkitRunnable() {
					public void run() {
						target.damage(level);
					}
				}.runTaskLater(plugin, 50);
			}
			if(enchants.containsKey("??????") && target instanceof Player) {
				int level = enchants.get("??????");
				Player t = (Player)target;
				int tlevel = t.getLevel() - level;
				if(tlevel < 0) {
					tlevel = 0;
				}
				t.setLevel(tlevel);
			}
			if(enchants.containsKey("??????") && target instanceof Player) {
				int level = enchants.get("??????");
				int rndm = new Random().nextInt(23 - (level*2));
				if(rndm == 0) {
					user.setLevel(user.getLevel() + 1);
				}
			}
			if(enchants.containsKey("?????????") && target instanceof Player) {
				Player t = (Player)target;
				boolean[] attached = {false,false,false,false};
				PlayerInventory ti = t.getInventory();
				if(ti.getHelmet() != null) {
					if(ti.getHelmet().getType() != Material.AIR) {
						if(canChange(ti.getHelmet().getType())) {
							attached[0] = true;
						}
					}
				}
				if(ti.getChestplate() != null) {
					if(ti.getChestplate().getType() != Material.AIR) {
						if(canChange(ti.getChestplate().getType())) {
							attached[1] = true;
						}
					}
				}
				if(ti.getLeggings() != null) {
					if(ti.getLeggings().getType() != Material.AIR) {
						if(canChange(ti.getLeggings().getType())) {
							attached[2] = true;
						}
					}
				}
				if(ti.getBoots() != null) {
					if(ti.getBoots().getType() != Material.AIR) {
						if(canChange(ti.getBoots().getType())) {
							attached[3] = true;
						}
					}
				}
				List<Integer> indexs = new ArrayList<Integer>(Arrays.asList(0,1,2,3));
				Collections.shuffle(indexs);
				for(int i:indexs) {
					if(attached[i]) {
						switch(i) {
						case 0:
							t.getInventory().setHelmet(new ItemStack(Material.AIR));
							user.getInventory().setItem(user.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
							break;
						case 1:
							t.getInventory().setChestplate(new ItemStack(Material.AIR));
							user.getInventory().setItem(user.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
							break;
						case 2:
							t.getInventory().setLeggings(new ItemStack(Material.AIR));
							user.getInventory().setItem(user.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
							break;
						case 3:
							t.getInventory().setBoots(new ItemStack(Material.AIR));
							user.getInventory().setItem(user.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
							break;
						}
						break;
					}
				}
			}
			if(enchants.containsKey("??????") && target instanceof Player) {
				Player t = (Player)target;
				if(t.getInventory().getItemInMainHand() != null) {
					consume();
					ItemStack item = t.getInventory().getItemInMainHand().clone();
					user.getInventory().addItem(item);
				}
			}
			if(enchants.containsKey("??????")) {
				user.damage(enchants.get("??????"));
				target.damage(enchants.get("??????") * 2);
			}
			if(enchants.containsKey("??????")) {
				int level = user.getLevel();
				if(level > 0) {
					tmg.getPlayerData(user).reduceLevel(1);
					target.damage(5);
				}
			}
			if(enchants.containsKey("??????")) {
				target.setVelocity(new Vector(0,-8,0));
			}
			if(enchants.containsKey("???????????????") && target instanceof Player) {
				Player t = (Player)target;
				int rndm;
				int count = 0;
				do{
					rndm = new Random().nextInt(36);
					count++;
					if(count >= 500) {
						break;
					}
				}while(t.getInventory().getItem(rndm) != null && t.getInventory().getItem(rndm).getType() == Material.APPLE);
				t.getInventory().setItem(rndm, new ItemStack(Material.APPLE));
			}
		}
	}
	
	public void onAttacked() {
		if(enchants.containsKey("??????") && tmg.entities.containsKey(target)) {
			tmg.entities.get(target).addNotAttack(user);
		}
	}
	
	public int onDig() {
		if(enchants.containsKey("?????????")) {
			return enchants.get("?????????");
		}
		return 0;
	}
	
	//??????????????????
	public void onShoot(Player shooter,Arrow projectile) {
		if(enchants.containsKey("??????")) {
			final Location s = projectile.getLocation().clone();
			for(int i = 0 ;  i < enchants.get("??????") ; i++) {
				new BukkitRunnable() {
					public void run() {
						Arrow newArrow = projectile.getWorld().spawnArrow(s, projectile.getLocation().getDirection(), 0.6f, 12.0f);
						newArrow.setShooter(shooter);
						newArrow.setVelocity(projectile.getVelocity());
						newArrow.setDamage(projectile.getDamage());
						newArrow.setPickupStatus(PickupStatus.DISALLOWED);
					}
				}.runTaskLater(plugin, 4 + (4*i));
			}
		}
		if(enchants.containsKey("??????")) {
			projectile.addPassenger(shooter);
		}
		if(enchants.containsKey("?????????")) {
			if(user.getWorld().isClearWeather()) {
				projectile.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER,200,2), true);
			}else if(user.getWorld().isThundering()) {
				projectile.addCustomEffect(new PotionEffect(PotionEffectType.SLOW,100,2), true);
			}else {
				projectile.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING,400,2),true);
			}
		}
	}
	
	//????????????????????????????????????
	public boolean onEffect() {
		if(enchants.containsKey("??????")) {
			return true;
		}
		return false;
	}
	
	//??????????????????
	public void onRightClick() {
		if(enchants.containsKey("??????")) {
			double maxh = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2;
			if(maxh <= 0) {
				user.sendMessage(ChatColor.RED + "???????????????????????????????????????????????????????????????????????????");
			}else {
				consume();
				user.teleport(tmg.locations[tmg.getPlayerIndex(user)][0].toLocation(tmg.world));
				user.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxh);
			}
		}
		if(enchants.containsKey("????????????")) {
			consume();
			user.setVelocity(user.getLocation().getDirection().multiply(2 * enchants.get("????????????")));
		}
		if(enchants.containsKey("????????????")) {
			consume();
			tmg.getPlayerData(user).openHoldInventory();
		}
		if(enchants.containsKey("???????????????")) {
			consume();
			user.getWorld().setClearWeatherDuration(0);
		}
		if(enchants.containsKey("????????????")) {
			consume();
			user.getWorld().setStorm(true);
		}
		if(enchants.containsKey("????????????")) {
			consume();
			user.getWorld().setThundering(true);
		}
		if(enchants.containsKey("??????")) {
			if(user.getLevel() >= 13) {
				tmg.getPlayerData(user).reduceLevel(13);
				user.teleport(tmg.locations[tmg.getPlayerIndex(user)][0].toLocation(user.getWorld()));
			}
		}
		if(enchants.containsKey("??????")) {
			consume();
			List<Player> players = new ArrayList<Player>();
			players.remove(user);
			players.addAll(tmg.livings);
			Collections.shuffle(players);
			new TWReview(plugin,user,tmg,0,players.get(0));
		}
		if(enchants.containsKey("?????????")) {
			consume();
			for(Player p:tmg.livings) {
				if(p != user) {
					tmg.getPlayerData(p).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1));
				}
			}
		}
		if(enchants.containsKey("????????????")) {
			consume();
			for(Player p:tmg.livings) {
				if(p.getInventory().getBoots() != null && !canChange(p.getInventory().getBoots().getType())) {
					continue;
				}
				p.getInventory().setBoots(null);
			}
		}
		if(enchants.containsKey("??????")) {
			consume();
			int range = 3 * enchants.get("??????");
			for(Entity ent:user.getNearbyEntities(range, range, range)) {
				if(tmg.containsLivings(ent)) {
					Player target = (Player)ent;
					Location tlo = target.getLocation();
					Location ulo = user.getLocation();
					Vector v = new Vector(tlo.getX() - ulo.getX(),tlo.getY()-ulo.getY(),tlo.getZ()-ulo.getZ());
					target.setVelocity(v);
				}
			}
		}
		if(enchants.containsKey("??????")) {
			consume();
			List<Player> randoms = new ArrayList<Player>();
			randoms.addAll(tmg.livings);
			randoms.remove(user);
			Player t = randoms.get(new Random().nextInt(randoms.size()));
			t.sendMessage(ChatColor.RED + "10?????????" + user.getDisplayName() + "?????????????????????????????????");
			new BukkitRunnable() {
				public void run() {
					user.teleport(t.getLocation());
				}
			}.runTaskTimer(plugin, 0, 200);
		}
	}
	
	//???????????????????????????
	public void onRightClickAtEntity() {
		if(enchants.containsKey("????????????") && tmg.entities.containsKey(target)) {
			consume();
			double health = target.getHealth() +  (5 * enchants.get("????????????"));
			if(health > target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				health = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			}
			target.setHealth(health);
		}
		if(enchants.containsKey("??????") && !(target instanceof Player) && target instanceof LivingEntity) {
			double maxh = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2;
			if(maxh <= 0) {
				user.sendMessage(ChatColor.RED + "???????????????????????????????????????????????????????????????????????????");
			}else {
				consume();
				target.remove();
				user.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxh);
			}
		}
	}
	
	//???????????????
	public void onSneak() {
		if(enchants.containsKey("?????????????????????")) {
			int level = enchants.get("?????????????????????");
			user.damage(2);
			for(Entity ent:user.getNearbyEntities(level * 5, level * 5 , level * 5)) {
				if(ent instanceof LivingEntity) {
					if(ent instanceof Player) {
						if(!tmg.livings.contains(ent)) {
							continue;
						}
					}
					LivingEntity target = (LivingEntity)ent;
					Location tl = target.getLocation();
					Location ul = user.getLocation();
					Vector v = new Vector(ul.getX()-tl.getX(),ul.getY()-tl.getY(),ul.getZ()-tl.getZ()).normalize();
					target.setVelocity(v.multiply(2));
				}
			}
		}
	}
	
	//??????????????????
	public int onEXP(int exp) {
		int newexp = exp;
		if(enchants.containsKey("??????")) {
			int level = enchants.get("??????");
			newexp = newexp + level + 3;
		}
		return newexp;
	}
	
	//??????????????????
	public boolean onReduceLevel() {
		boolean reduce = true;
		if(enchants.containsKey("??????")) {
			int level = enchants.get("??????");
			Random rnd = new Random();
			int rndm = rnd.nextInt(23 - (4 * level));
			if(rndm == 0) {
				reduce = false;
			}
		}
		return reduce;
	}
	
	//??????????????????????????????
	public void onDamage(double damage) {
		if(enchants.containsKey("?????????")) {
			int level = enchants.get("?????????");
			int delay = 300;
			double amount = damage * (0.25 * level);
			new BukkitRunnable() {
				public void run() {
					if(!tmg.getPlayerData(user).death) {
						double newhealth = user.getHealth() + amount;
						if(newhealth >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
							newhealth = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
						}
						user.setHealth(newhealth);
					}
				}
			}.runTaskLater(plugin, delay);
		}
		if(enchants.containsKey("??????") && target != null && target instanceof Player) {
			Player t = (Player)target;
			if(t.getInventory().getItemInMainHand() != null) {
				if(t.getInventory().getItemInMainHand().getType() != Material.AIR) {
					t.getInventory().setItem(t.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
					user.getInventory().setChestplate(new ItemStack(Material.AIR));
				}
			}
		}
		if(enchants.containsKey("??????????????????")) {
			int rndm = new Random().nextInt(70);
			if(rndm == 0) {
				user.getInventory().addItem(TWData.getTreasureBox());
				user.sendMessage(ChatColor.GOLD + "???????????????????????????");
			}
		}
		if(enchants.containsKey("??????") && target != null && target instanceof Player) {
			Player t = (Player)target;
			ItemStack titem = t.getInventory().getItemInMainHand().clone();
			t.getInventory().setItem(t.getInventory().getHeldItemSlot(),new ItemStack(Material.AIR));
			Item item = t.getWorld().dropItem(t.getLocation(), titem);
			item.setOwner(t.getUniqueId());
			item.setPickupDelay(5);
		}
		if(enchants.containsKey("?????????") && target != null && target instanceof Player) {
			Player t = (Player)target;
			if(user.getInventory().getHelmet() != null && user.getInventory().getHelmet().getType() != Material.AIR) {
				if(canChange(t.getInventory().getHelmet().getType())){
					t.getInventory().setHelmet(user.getInventory().getHelmet());
					user.getInventory().setHelmet(new ItemStack(Material.AIR));
				}
			}
			if(user.getInventory().getChestplate() != null && user.getInventory().getChestplate().getType() != Material.AIR) {
				if(canChange(t.getInventory().getChestplate().getType())){
					t.getInventory().setChestplate(user.getInventory().getChestplate());
					user.getInventory().setChestplate(new ItemStack(Material.AIR));
				}
			}
			if(user.getInventory().getLeggings() != null && user.getInventory().getLeggings().getType() != Material.AIR) {
				if(canChange(t.getInventory().getLeggings().getType())){
					t.getInventory().setLeggings(user.getInventory().getLeggings());
					user.getInventory().setLeggings(new ItemStack(Material.AIR));
				}
			}
			if(user.getInventory().getBoots() != null && user.getInventory().getBoots().getType() != Material.AIR) {
				if(canChange(t.getInventory().getBoots().getType())){
					t.getInventory().setBoots(user.getInventory().getBoots());
					user.getInventory().setBoots(new ItemStack(Material.AIR));
				}
			}
			user.getInventory().setItem(user.getInventory().getHeldItemSlot(),new ItemStack(Material.AIR));
		}
		if(enchants.containsKey("????????????")) {
			tmg.getPlayerData(user).addSpeedTemp(-1, 80);
		}
		if(enchants.containsKey("????????????")) {
			new TWSummon(plugin,tmg).summonNormal(EntityType.ZOMBIE, user.getLocation());
		}
	}
	
	//???????????????
	public boolean onTarget() {
		boolean result = false;
		if(enchants.containsKey("??????")) {
			result = true;
		}
		return result;
	}
	
	//??????????????????????????????
	public void onDeathNearly() {
		if(enchants.containsKey("?????????")) {
			double health = user.getHealth() + (enchants.get("?????????") * 2);
			if(health >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
				health = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			}
			user.setHealth(health);
		}
	}
	
	//????????????
	public void onLoop() {
		if(enchants.containsKey("??????")) {
			tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,40,1));
		}
		if(enchants.containsKey("?????????")) {
			if(!tmg.getPlayerData(user).containsPromise("?????????")) {
				tmg.getPlayerData(user).promiseSpeed("?????????", enchants.get("?????????") + 1);
			}
		}else {
			if(tmg.getPlayerData(user).containsPromise("?????????")) {
				tmg.getPlayerData(user).promiseBurnSpeed("?????????");
			}
		}
		if(enchants.containsKey("??????")) {
			if(user.getLocation().getBlock().getType() == Material.WATER) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(19 - (4 * enchants.get("??????")));
				if(rndm == 0) {
					double hp = user.getHealth() + 1;
					if(hp >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					}
					user.setHealth(hp);
				}
			}
		}
		if(enchants.containsKey("??????")) {
			int level = enchants.get("??????");
			int radius = level * 7;
			for(Entity ent:user.getNearbyEntities(radius, radius, radius)) {
				if(tmg.containsLivings(ent)) {
					Player target = (Player)ent;
					tmg.getPlayerData(target).setKanazuti(100);
				}
			}
		}
		if(enchants.containsKey("???") && (user.getWorld().hasStorm() || user.getWorld().isThundering())) {
			if(!tmg.getPlayerData(user).containsPromise("???")) {
				tmg.getPlayerData(user).promiseSpeed("???", 4);
			}
		}else {
			if(tmg.getPlayerData(user).containsPromise("???")) {
				tmg.getPlayerData(user).promiseBurnSpeed("???");
			}
		}
		if(enchants.containsKey("?????????")) {
			if(user.getWorld().isClearWeather()) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(15);
				if(rndm == 0) {
					double hp = user.getHealth() + 1;
					if(hp >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					}
					user.setHealth(hp);
				}
			}
		}
		if(enchants.containsKey("?????????")) {
			Random rnd = new Random();
			int rndm = rnd.nextInt(15);
			if(rndm == 0) {
				user.damage(2);
			}
		}
		if(enchants.containsKey("?????????")) {
			Random rnd = new Random();
			int rndm = rnd.nextInt(15);
			if(rndm == 0) {
				int newfood = user.getFoodLevel() - 2;
				if(newfood < 0) {
					newfood = 0;
				}
				user.setFoodLevel(newfood);
			}
		}
		if(enchants.containsKey("????????????")) {
			if(user.isSprinting()) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(30 - (enchants.get("????????????") * 5));
				if(rndm == 0) {
					double hp = user.getHealth() + 1;
					if(hp >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					}
					user.setHealth(hp);
				}
			}
		}
		if(enchants.containsKey("?????????")) {
			Location l = user.getLocation().clone();
			l.setY(l.getY() - 1);
			if(l.getBlock().getType() == Material.DIRT || l.getBlock().getType() == Material.GRASS_BLOCK) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(30 - (enchants.get("?????????") * 5));
				if(rndm == 0) {
					double hp = user.getHealth() + 1;
					if(hp >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					}
					user.setHealth(hp);
				}
			}
		}
	}
	
	public boolean containsEnchant(String enchantname) {
		for(String s:enchants.keySet()) {
			if(s.equals(enchantname)) {
				return true;
			}
		}
		return false;
	}
	
	public int getEnchantLevel(String enchantname) {
		return enchants.get(enchantname);
	}
	
	/**
	 * ????????????????????????????????????????????????
	 */
	void consume() {
		ItemStack item = user.getInventory().getItemInMainHand().clone();
		if(item.getAmount() == 1) {
			user.getInventory().setItem(user.getInventory().getHeldItemSlot(), null);
		}else {
			item.setAmount(item.getAmount() - 1);
			user.getInventory().setItem(user.getInventory().getHeldItemSlot(), item);
		}
		//???????????????????????????
		if(item.getType() == Material.IRON_INGOT) {
			tmg.getPlayerData(user).addSpeedTemp(2, 300);
		}else if(item.getType() == Material.GOLD_INGOT) {
			tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,300,2));
		}else if(item.getType() == Material.REDSTONE) {
			tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,300,2));
		}else if(item.getType() == Material.DIAMOND) {
			tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,300,2));
		}else if(item.getType() == Material.EMERALD) {
			tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,300,2));
		}else if(item.getType() == Material.AMETHYST_SHARD) {
			Random rnd = new Random();
			switch(rnd.nextInt(5)) {
			case 0:
				tmg.getPlayerData(user).addSpeedTemp(2, 200);
				break;
			case 1:
				tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,300,2));
				break;
			case 2:
				tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,300,2));
				break;
			case 3:
				tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,300,2));
				break;
			default:
				tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,300,2));
				break;
			}
		}else if(item.getType() == Material.NETHERITE_INGOT) {
			double hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			user.setHealth(hp);
		}
	}
	
	boolean canChange(Material m) {
		switch(m) {
		case NETHERITE_HELMET:
		case NETHERITE_CHESTPLATE:
		case NETHERITE_LEGGINGS:
		case NETHERITE_BOOTS:
			return false;
		default:
			return true;
		}
	}
}
