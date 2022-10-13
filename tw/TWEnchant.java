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
		//ターゲット不要
		if(enchants.containsKey("吸血")) {
			Bukkit.getLogger().info("yeah");
			double newhp = user.getHealth() + enchants.get("吸血");
			if(newhp > user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				newhp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			}
			user.setHealth(newhp);
		}
		if(enchants.containsKey("反動")) {
			user.setVelocity(user.getLocation().getDirection().multiply(-1 * enchants.get("反動")));
		}
		if(enchants.containsKey("ラッキーパンチ")) {
			int rndm = new Random().nextInt(60);
			if(rndm == 0) {
				user.getInventory().addItem(TWData.getTreasureBox());
				user.sendMessage(ChatColor.GOLD + "宝箱を手に入れた！");
			}
		}
		//ターゲット必要
		if(target != null) {
			if(enchants.containsKey("浮遊")) {
				int level = enchants.get("浮遊");
				if(target instanceof Player) {
					tmg.getPlayerData((Player)target).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,(20*level),2));
				}else {
					target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,(20*level),2));
				}
			}
			if(enchants.containsKey("固定火力")) {
				int level = enchants.get("固定火力");
				target.damage(level);
			}
			if(enchants.containsKey("爆竹")) {
				int level = enchants.get("爆竹");
				new BukkitRunnable() {
					public void run() {
						target.damage(level);
					}
				}.runTaskLater(plugin, 50);
			}
			if(enchants.containsKey("流出") && target instanceof Player) {
				int level = enchants.get("流出");
				Player t = (Player)target;
				int tlevel = t.getLevel() - level;
				if(tlevel < 0) {
					tlevel = 0;
				}
				t.setLevel(tlevel);
			}
			if(enchants.containsKey("学習") && target instanceof Player) {
				int level = enchants.get("学習");
				int rndm = new Random().nextInt(23 - (level*2));
				if(rndm == 0) {
					user.setLevel(user.getLevel() + 1);
				}
			}
			if(enchants.containsKey("瓦割り") && target instanceof Player) {
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
			if(enchants.containsKey("模造") && target instanceof Player) {
				Player t = (Player)target;
				if(t.getInventory().getItemInMainHand() != null) {
					consume();
					ItemStack item = t.getInventory().getItemInMainHand().clone();
					user.getInventory().addItem(item);
				}
			}
			if(enchants.containsKey("崩壊")) {
				user.damage(enchants.get("崩壊"));
				target.damage(enchants.get("崩壊") * 2);
			}
			if(enchants.containsKey("禁忌")) {
				int level = user.getLevel();
				if(level > 0) {
					tmg.getPlayerData(user).reduceLevel(1);
					target.damage(5);
				}
			}
			if(enchants.containsKey("重り")) {
				target.setVelocity(new Vector(0,-8,0));
			}
			if(enchants.containsKey("プレゼント") && target instanceof Player) {
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
		if(enchants.containsKey("洗脳") && tmg.entities.containsKey(target)) {
			tmg.entities.get(target).addNotAttack(user);
		}
	}
	
	public int onDig() {
		if(enchants.containsKey("炭鉱夫")) {
			return enchants.get("炭鉱夫");
		}
		return 0;
	}
	
	//矢を打った時
	public void onShoot(Player shooter,Arrow projectile) {
		if(enchants.containsKey("連撃")) {
			final Location s = projectile.getLocation().clone();
			for(int i = 0 ;  i < enchants.get("連撃") ; i++) {
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
		if(enchants.containsKey("便乗")) {
			projectile.addPassenger(shooter);
		}
		if(enchants.containsKey("適応性")) {
			if(user.getWorld().isClearWeather()) {
				projectile.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER,200,2), true);
			}else if(user.getWorld().isThundering()) {
				projectile.addCustomEffect(new PotionEffect(PotionEffectType.SLOW,100,2), true);
			}else {
				projectile.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING,400,2),true);
			}
		}
	}
	
	//ポーション効果を受けた時
	public boolean onEffect() {
		if(enchants.containsKey("免疫")) {
			return true;
		}
		return false;
	}
	
	//右クリック時
	public void onRightClick() {
		if(enchants.containsKey("帰還")) {
			double maxh = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2;
			if(maxh <= 0) {
				user.sendMessage(ChatColor.RED + "もう代償を払えないため、「帰還」を使用できません！");
			}else {
				consume();
				user.teleport(tmg.locations[tmg.getPlayerIndex(user)][0].toLocation(tmg.world));
				user.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxh);
			}
		}
		if(enchants.containsKey("ロケット")) {
			consume();
			user.setVelocity(user.getLocation().getDirection().multiply(2 * enchants.get("ロケット")));
		}
		if(enchants.containsKey("保管空間")) {
			consume();
			tmg.getPlayerData(user).openHoldInventory();
		}
		if(enchants.containsKey("晴れの祈り")) {
			consume();
			user.getWorld().setClearWeatherDuration(0);
		}
		if(enchants.containsKey("雨の祈り")) {
			consume();
			user.getWorld().setStorm(true);
		}
		if(enchants.containsKey("雷の祈り")) {
			consume();
			user.getWorld().setThundering(true);
		}
		if(enchants.containsKey("生還")) {
			if(user.getLevel() >= 13) {
				tmg.getPlayerData(user).reduceLevel(13);
				user.teleport(tmg.locations[tmg.getPlayerIndex(user)][0].toLocation(user.getWorld()));
			}
		}
		if(enchants.containsKey("検閲")) {
			consume();
			List<Player> players = new ArrayList<Player>();
			players.remove(user);
			players.addAll(tmg.livings);
			Collections.shuffle(players);
			new TWReview(plugin,user,tmg,0,players.get(0));
		}
		if(enchants.containsKey("千里眼")) {
			consume();
			for(Player p:tmg.livings) {
				if(p != user) {
					tmg.getPlayerData(p).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1));
				}
			}
		}
		if(enchants.containsKey("足すくい")) {
			consume();
			for(Player p:tmg.livings) {
				if(p.getInventory().getBoots() != null && !canChange(p.getInventory().getBoots().getType())) {
					continue;
				}
				p.getInventory().setBoots(null);
			}
		}
		if(enchants.containsKey("息吹")) {
			consume();
			int range = 3 * enchants.get("息吹");
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
		if(enchants.containsKey("降臨")) {
			consume();
			List<Player> randoms = new ArrayList<Player>();
			randoms.addAll(tmg.livings);
			randoms.remove(user);
			Player t = randoms.get(new Random().nextInt(randoms.size()));
			t.sendMessage(ChatColor.RED + "10秒後、" + user.getDisplayName() + "が、船に乗り込みます！");
			new BukkitRunnable() {
				public void run() {
					user.teleport(t.getLocation());
				}
			}.runTaskTimer(plugin, 0, 200);
		}
	}
	
	//生物を右クリック時
	public void onRightClickAtEntity() {
		if(enchants.containsKey("生物薬品") && tmg.entities.containsKey(target)) {
			consume();
			double health = target.getHealth() +  (5 * enchants.get("生物薬品"));
			if(health > target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
				health = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			}
			target.setHealth(health);
		}
		if(enchants.containsKey("末梢") && !(target instanceof Player) && target instanceof LivingEntity) {
			double maxh = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - 2;
			if(maxh <= 0) {
				user.sendMessage(ChatColor.RED + "もう代償を払えないため、「末梢」を使用できません！");
			}else {
				consume();
				target.remove();
				user.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxh);
			}
		}
	}
	
	//スニーク時
	public void onSneak() {
		if(enchants.containsKey("ブラックホール")) {
			int level = enchants.get("ブラックホール");
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
	
	//経験値取得時
	public int onEXP(int exp) {
		int newexp = exp;
		if(enchants.containsKey("利口")) {
			int level = enchants.get("利口");
			newexp = newexp + level + 3;
		}
		return newexp;
	}
	
	//レベル消費時
	public boolean onReduceLevel() {
		boolean reduce = true;
		if(enchants.containsKey("節約")) {
			int level = enchants.get("節約");
			Random rnd = new Random();
			int rndm = rnd.nextInt(23 - (4 * level));
			if(rndm == 0) {
				reduce = false;
			}
		}
		return reduce;
	}
	
	//ダメージを食らった時
	public void onDamage(double damage) {
		if(enchants.containsKey("再生体")) {
			int level = enchants.get("再生体");
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
		if(enchants.containsKey("消失") && target != null && target instanceof Player) {
			Player t = (Player)target;
			if(t.getInventory().getItemInMainHand() != null) {
				if(t.getInventory().getItemInMainHand().getType() != Material.AIR) {
					t.getInventory().setItem(t.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
					user.getInventory().setChestplate(new ItemStack(Material.AIR));
				}
			}
		}
		if(enchants.containsKey("不幸中の幸い")) {
			int rndm = new Random().nextInt(70);
			if(rndm == 0) {
				user.getInventory().addItem(TWData.getTreasureBox());
				user.sendMessage(ChatColor.GOLD + "宝箱を手に入れた！");
			}
		}
		if(enchants.containsKey("銅鑼") && target != null && target instanceof Player) {
			Player t = (Player)target;
			ItemStack titem = t.getInventory().getItemInMainHand().clone();
			t.getInventory().setItem(t.getInventory().getHeldItemSlot(),new ItemStack(Material.AIR));
			Item item = t.getWorld().dropItem(t.getLocation(), titem);
			item.setOwner(t.getUniqueId());
			item.setPickupDelay(5);
		}
		if(enchants.containsKey("着付け") && target != null && target instanceof Player) {
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
		if(enchants.containsKey("びびり腰")) {
			tmg.getPlayerData(user).addSpeedTemp(-1, 80);
		}
		if(enchants.containsKey("百鬼夜行")) {
			new TWSummon(plugin,tmg).summonNormal(EntityType.ZOMBIE, user.getLocation());
		}
	}
	
	//ターゲット
	public boolean onTarget() {
		boolean result = false;
		if(enchants.containsKey("王冠")) {
			result = true;
		}
		return result;
	}
	
	//近くで生物が倒れた時
	public void onDeathNearly() {
		if(enchants.containsKey("黒魔術")) {
			double health = user.getHealth() + (enchants.get("黒魔術") * 2);
			if(health >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
				health = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			}
			user.setHealth(health);
		}
	}
	
	//常時発動
	public void onLoop() {
		if(enchants.containsKey("透明")) {
			tmg.getPlayerData(user).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,40,1));
		}
		if(enchants.containsKey("軽量化")) {
			if(!tmg.getPlayerData(user).containsPromise("軽量化")) {
				tmg.getPlayerData(user).promiseSpeed("軽量化", enchants.get("軽量化") + 1);
			}
		}else {
			if(tmg.getPlayerData(user).containsPromise("軽量化")) {
				tmg.getPlayerData(user).promiseBurnSpeed("軽量化");
			}
		}
		if(enchants.containsKey("水性")) {
			if(user.getLocation().getBlock().getType() == Material.WATER) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(19 - (4 * enchants.get("水性")));
				if(rndm == 0) {
					double hp = user.getHealth() + 1;
					if(hp >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					}
					user.setHealth(hp);
				}
			}
		}
		if(enchants.containsKey("金槌")) {
			int level = enchants.get("金槌");
			int radius = level * 7;
			for(Entity ent:user.getNearbyEntities(radius, radius, radius)) {
				if(tmg.containsLivings(ent)) {
					Player target = (Player)ent;
					tmg.getPlayerData(target).setKanazuti(100);
				}
			}
		}
		if(enchants.containsKey("皿") && (user.getWorld().hasStorm() || user.getWorld().isThundering())) {
			if(!tmg.getPlayerData(user).containsPromise("皿")) {
				tmg.getPlayerData(user).promiseSpeed("皿", 4);
			}
		}else {
			if(tmg.getPlayerData(user).containsPromise("皿")) {
				tmg.getPlayerData(user).promiseBurnSpeed("皿");
			}
		}
		if(enchants.containsKey("光合成")) {
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
		if(enchants.containsKey("偏頭痛")) {
			Random rnd = new Random();
			int rndm = rnd.nextInt(15);
			if(rndm == 0) {
				user.damage(2);
			}
		}
		if(enchants.containsKey("乾燥肌")) {
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
		if(enchants.containsKey("ハミング")) {
			if(user.isSprinting()) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(30 - (enchants.get("ハミング") * 5));
				if(rndm == 0) {
					double hp = user.getHealth() + 1;
					if(hp >= user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
						hp = user.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
					}
					user.setHealth(hp);
				}
			}
		}
		if(enchants.containsKey("宿り木")) {
			Location l = user.getLocation().clone();
			l.setY(l.getY() - 1);
			if(l.getBlock().getType() == Material.DIRT || l.getBlock().getType() == Material.GRASS_BLOCK) {
				Random rnd = new Random();
				int rndm = rnd.nextInt(30 - (enchants.get("宿り木") * 5));
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
	 * 手に持っているアイテムを消費する
	 */
	void consume() {
		ItemStack item = user.getInventory().getItemInMainHand().clone();
		if(item.getAmount() == 1) {
			user.getInventory().setItem(user.getInventory().getHeldItemSlot(), null);
		}else {
			item.setAmount(item.getAmount() - 1);
			user.getInventory().setItem(user.getInventory().getHeldItemSlot(), item);
		}
		//消費アイテムの効果
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
