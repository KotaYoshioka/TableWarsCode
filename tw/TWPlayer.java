package tw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import inv.TWCraftingTable;
import inv.TWEnchantTable;
import inv.TWLiza;
import inv.TWMapTable;
import inv.TWShipTable;
import inv.TWSmithingTable;
import inv.TWTreasure;

public class TWPlayer implements Listener{
	
	Plugin plugin;
	TMGame tmg;
	Player player;
	//開いているインベントリの種類の特定
	int opentype = 0;
	int opendetail = 0;
	int openindex = 0;
	
	//造船した回数
	int makeShipTime = 1;
	
	//連続使用防止
	boolean right = true;
	//敗退しているかどうか
	boolean death = false;
	//宝で手に入れたエンチャント一覧
	List<String> perm = new ArrayList<String>();
	//保管空間のインベントリ
	Inventory hinv;
	////金槌
	//自分が現在金槌かどうか
	boolean kanazuti = false;
	//金槌に関する遅延タスク
	BukkitTask kanazutiTask = null;
	
	//////ステータス
	////移動速度
	int speed = 0;
	//約束速度
	HashMap<String,Integer> promiseSpeed = new HashMap<String,Integer>();
	////ジャンプ力
	int jump = 0;
	
	////攻撃力
	int attack = 0;
	
	
	//自分の使役している生物達
	List<TWEntity> allies = new ArrayList<TWEntity>();
	
	public TWPlayer(Plugin plugin, TMGame tmg, Player player) {
		//初期要素
		this.plugin = plugin;
		this.tmg = tmg;
		this.player = player;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		//ステータスの初期化
		player.setHealth(20);
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.getInventory().addItem(TWData.getTreasureBox());
		player.setExp(0);
		player.setLevel(0);
		//ポーション効果消す
		for(PotionEffect pe:player.getActivePotionEffects()) {
			player.removePotionEffect(pe.getType());
		}
		//保管空間の作成
		hinv = Bukkit.createInventory(null, 18,player.getDisplayName() + "の保管空間");
		passive();
	}
	
	public void cancelEvents() {
		death = true;
		HandlerList.unregisterAll(this);
	}
	
	void passive() {
		new BukkitRunnable() {
			public void run() {
				if(!tmg.nowgame) {
					this.cancel();
					return;
				}
				new TWEnchant(player,plugin,tmg).onLoop();
			}
		}.runTaskTimer(plugin, 0, 10);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(e.getPlayer() == player) {
			if(!right) {
				e.setCancelled(true);
				return;
			}
			right = false;
			new BukkitRunnable() {
				public void run() {
					right = true;
				}
			}.runTaskLater(plugin, 5);
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				//作業台をクリックした場合	
				if(e.getClickedBlock().getType() == Material.CRAFTING_TABLE) {
					e.setCancelled(true);
					openindex = tmg.getTableIndex(e.getClickedBlock());
					new TWCraftingTable(plugin,player,tmg,openindex);
					return;
				}
				//矢細工台をクリックした場合
				else if(e.getClickedBlock().getType() == Material.FLETCHING_TABLE) {
					e.setCancelled(true);
					openindex = tmg.getTableIndex(e.getClickedBlock());
					new TWShipTable(plugin,player,tmg,openindex);
					return;
				}
				//製図台をクリックした場合
				else if(e.getClickedBlock().getType() == Material.CARTOGRAPHY_TABLE) {
					e.setCancelled(true);
					openindex = tmg.getTableIndex(e.getClickedBlock());
					new TWMapTable(plugin,player,tmg,openindex);
					return;
				}
				//エンチャント台をクリックした場合
				else if(e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
					e.setCancelled(true);
					openindex = tmg.getTableIndex(e.getClickedBlock());
					new TWEnchantTable(plugin,player,tmg,openindex);
					return;
				}
				//鍛冶台をクリックした場合
				else if(e.getClickedBlock().getType() == Material.SMITHING_TABLE) {
					e.setCancelled(true);
					openindex = tmg.getTableIndex(e.getClickedBlock());
					new TWSmithingTable(plugin,player,tmg,openindex);
					return;
				}
			}
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(player.getInventory().getItemInMainHand() != null) {
					if(player.getInventory().getItemInMainHand().getType() != Material.AIR) {
						ItemStack item = player.getInventory().getItemInMainHand().clone();
						//鉄ピッケルによる宝探し
						if(item.getType() == Material.IRON_PICKAXE) {
							e.setCancelled(true);
							tmg.dig(player);
							player.getInventory().setItem(player.getInventory().getHeldItemSlot(),null);
						}
						//エンチャント記録書による新たなエンチャントの開放
						else if(item.getType() == Material.BOOK) {
							e.setCancelled(true);
							Bukkit.getLogger().info("a");
							String enchantname = item.getItemMeta().getDisplayName();
							Bukkit.getLogger().info("b");
							String levels = item.getItemMeta().getLore().get(0);
							Bukkit.getLogger().info("c");
							perm.add(enchantname);
							Bukkit.getLogger().info("d");
							player.getInventory().setItemInMainHand(null);
							Bukkit.getLogger().info("e");
							player.sendMessage(ChatColor.GOLD + enchantname + levels + ChatColor.GOLD + "を開放しました！");
						}
						//宝箱の開封
						else if(item.getType() == Material.TRAPPED_CHEST) {
							e.setCancelled(true);
							if(item.getAmount() == 1) {
								player.getInventory().setItem(player.getInventory().getHeldItemSlot(),null);
							}else {
								item.setAmount(item.getAmount() - 1);
							}
							if(new TWEnchant(player,plugin,tmg).containsEnchant("鑑定士")) {
								new TWTreasure(plugin,player,tmg,0);
							}else {
								player.getInventory().addItem(TWData.getTreasure());
								player.sendMessage(ChatColor.GOLD + "宝箱を開封しました！");
							}
						}
						//レベルボトルの仕様
						else if(item.getType() == Material.EXPERIENCE_BOTTLE) {
							e.setCancelled(true);
							if(item.getAmount() == 1) {
								player.getInventory().setItemInMainHand(null);
							}else {
								item.setAmount(item.getAmount() - 1);
							}
							addLevel(1);
						}
						//TNT
						else if(item.getType() == Material.TNT) {
							e.setCancelled(true);
							if(item.getAmount() == 1) {
								player.getInventory().setItemInMainHand(null);
							}else {
								item.setAmount(item.getAmount() - 1);
							}
							player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onRightClickEntity(PlayerInteractAtEntityEvent e) {
		if(e.getPlayer() == player) {
			if(e.getRightClicked().getType() == EntityType.VILLAGER) {
				e.setCancelled(true);
				int index = tmg.lizas.get(e.getRightClicked());
				new TWLiza(plugin,player,tmg,openindex,index);
			}
		}
	}
	
	@EventHandler
	public void onSwim(EntityToggleSwimEvent e) {
		if(e.getEntity() == player) {
			if(kanazuti && !e.isSwimming()) {
				e.setCancelled(true);
			}
		}
	}

	
	public void addPotionEffect(PotionEffect pe) {
		if(!new TWEnchant(player,plugin,tmg).onEffect()) {
			player.addPotionEffect(pe);
		}
	}
	
	public void addLevel(int level) {
		player.setLevel(player.getLevel() + level);
	}
	public void reduceLevel(int level) {
		if(new TWEnchant(player,plugin,tmg).onReduceLevel()) {
			int newlevel = player.getLevel() - level;
			if(newlevel < 0) {
				newlevel = 0;
			}
			player.setLevel(newlevel);
		}
	}
	
	public void openHoldInventory() {
		player.openInventory(hinv);
	}
	
	public void setKanazuti(int delayForTicks) {
		if(kanazutiTask != null) {
			kanazutiTask.cancel();
		}
		if(player.isSwimming()) {
			player.setSwimming(false);
		}
		kanazuti = true;
		kanazutiTask = new BukkitRunnable() {
			public void run() {
				kanazuti = false;
				kanazutiTask = null;
			}
		}.runTaskLater(plugin, delayForTicks);
	}
	
	public int getSpeed() {
		return speed;
	}
	public void addSpeedTemp(int add,int delayForTicks) {
		speed += add;
		changeSpeed();
		new BukkitRunnable() {
			public void run() {
				speed -= add;
				changeSpeed();
			}
		}.runTaskLater(plugin, delayForTicks);
	}
	public void promiseSpeed(String promise,int add) {
		speed += add;
		promiseSpeed.put(promise, add);
		changeSpeed();
	}
	public void promiseBurnSpeed(String promise) {
		speed -= promiseSpeed.get(promise);
		promiseSpeed.remove(promise);
		changeSpeed();
	}
	void changeSpeed() {
		if(player.hasPotionEffect(PotionEffectType.SPEED)) {
			player.removePotionEffect(PotionEffectType.SPEED);
		}
		if(player.hasPotionEffect(PotionEffectType.SLOW)) {
			player.removePotionEffect(PotionEffectType.SLOW);
		}
		if(speed > 0) {
			addPotionEffect(new PotionEffect(PotionEffectType.SPEED,9999999,speed - 1));
		}else if(speed < 0) {
			addPotionEffect(new PotionEffect(PotionEffectType.SLOW,9999999,-speed - 1));
		}
	}
	
	public boolean containsPromise(String promise) {
		return promiseSpeed.containsKey(promise);
	}
	
	public void addAllies(TWEntity twe) {
		allies.add(twe);
	}
	
	public int getMakeShipTime() {
		return makeShipTime;
	}
	
	public List<String> getPerm(){
		return perm;
	}
}
