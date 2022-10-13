package inv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import tw.TMGame;
import tw.TWData;
import tw.TWEnchant;

public class TWEnchantTable extends TWInventory{

	boolean clickguard = false;
	
	public TWEnchantTable(Plugin plugin, Player player, TMGame tmg, int openindex) {
		super(plugin, player, tmg, openindex);
		Inventory inv = Bukkit.createInventory(null,27,"エンチャント台");
		List<Integer> ban = new ArrayList<Integer>(Arrays.asList(10,12,13,14,16));
		for(int i = 0 ; i < 27 ; i++) {
			if(ban.contains(i)) {
				continue;
			}
			inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
		}
		openInventory(inv);
	}

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(clickguard) {
				return;
			}
			clickguard = true;
			new BukkitRunnable() {
				public void run() {
					clickguard = false;
				}
			}.runTaskLater(plugin, 9);
			if(e.getRawSlot() < 27) {
				if(e.getCurrentItem() != null) {
					if(e.getCurrentItem().getType() != Material.AIR) {
						if(e.getRawSlot() == 10) {
							ItemStack exitem = e.getCurrentItem().clone();
							e.getInventory().setItem(10, new ItemStack(Material.AIR));
							player.getInventory().addItem(exitem);
							for(int i = 12 ; i < 15 ; i++) {
								e.getInventory().setItem(i,new ItemStack(Material.AIR));
							}
						}else if(e.getRawSlot() <= 14 && e.getRawSlot() >= 12) {
							if(player.getLevel() >= 3) {
								if(e.getInventory().getItem(10).hasItemMeta()) {
									if(e.getInventory().getItem(10).getItemMeta().hasLore()) {
										if(e.getInventory().getItem(10).getItemMeta().getLore().size() >= 3) {
											player.closeInventory();
											player.sendMessage(ChatColor.RED + "一つのアイテムに付けられるエンチャントは3つまでです！");
											return;
										}
									}
								}
								playerdata.reduceLevel(3);
								ItemStack click = e.getCurrentItem().clone();
								//String level = click.getItemMeta().getLore().get(0).replaceAll("Lv.", "");
								String enchantname = click.getItemMeta().getDisplayName();
								ItemStack tar = e.getInventory().getItem(10).clone();
								ItemMeta tarm = tar.getItemMeta();
								List<String> lore = tarm.getLore();
								if(lore == null) {
									lore = new ArrayList<String>();
								}
								lore.add(ChatColor.WHITE + enchantname + click.getItemMeta().getLore().get(0));
								tarm.setLore(lore);
								if(!tarm.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
									tarm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
								}
								tar.setItemMeta(tarm);
								e.getInventory().setItem(10, new ItemStack(Material.AIR));
								for(int i = 12 ; i < 15 ; i++) {
									e.getInventory().setItem(i,new ItemStack(Material.AIR));
								}
								String level = click.getItemMeta().getLore().get(0).replaceAll("Lv.", "");
								int truelevel = Integer.parseInt(ChatColor.stripColor(level));
								e.getInventory().setItem(16, addEnchantment(tar,enchantname,truelevel));
							}else {
								player.closeInventory();
								player.sendMessage(ChatColor.RED + "エンチャントには" + ChatColor.YELLOW + "3レベル" + ChatColor.RED + "が必要です！");
							}
						}else if(e.getRawSlot() == 16) {
							ItemStack exitem = e.getCurrentItem().clone();
							e.getInventory().setItem(16, new ItemStack(Material.AIR));
							player.getInventory().addItem(exitem);
						}
					}
				}
			}else {
				if(e.getInventory().getItem(10) != null) {
					if(e.getInventory().getItem(10).getType() != Material.AIR) {
						ItemStack exitem = e.getInventory().getItem(10).clone();
						player.getInventory().addItem(exitem);
					}
				}
				if(e.getCurrentItem() != null) {
					if(e.getCurrentItem().getType() != Material.AIR) {
						ItemStack clickitem = e.getCurrentItem().clone();
						e.getInventory().setItem(10, clickitem);
						player.getInventory().setItem(e.getSlot(),new ItemStack(Material.AIR));
						for(int i = 12 ; i < 15 ; i++) {
							e.getInventory().setItem(i,new ItemStack(Material.AIR));
						}
						setSuggest(e.getInventory());
					}
				}
			}
		}
	}

	
	ItemStack addEnchantment(ItemStack tar,String enchantname,int level) {
		ItemStack t = tar.clone();
		ItemMeta tm = t.getItemMeta();
		Enchantment e = TWData.getRealEnchantment(enchantname);
		if(e != null) {
			tm.addEnchant(TWData.getRealEnchantment(enchantname),level, true);
		}
		t.setItemMeta(tm);
		return t;
	}
	
	
	
	void setSuggest(Inventory inv) {
		ItemStack base = inv.getItem(10);
		//[0]名前
		//[1]レベル
		//[2]必要グレード
		List<Object[]> sugenchants = new ArrayList<Object[]>();
		for(int i = 0 ; i < TWData.enchant.length ; i++) {
			String enchantname = (String)TWData.enchant[i][0];
			int level = (int)TWData.enchant[i][1];
			int grade = (int)TWData.enchant[i][2];
			String target = Integer.toBinaryString((int)TWData.enchant[i][3]);
			StringBuilder plus = new StringBuilder();
			for(int k = target.length() ; k < 18 ; k++) {
				plus.append("0");
			}
			target = plus.toString() + target;
			Object[] koho = {enchantname,level,grade};
			//一度ついたエンチャントを排除する
			boolean out = false;
			if(base.getItemMeta().hasLore()) {
				for(String s:base.getItemMeta().getLore()) {
					s = s.replaceAll("Lv.+", "");
					String perfect = ChatColor.stripColor(s);
					if(perfect.equals(enchantname)) {
						out = true;
						break;
					}
				}
			}
			if(out) {
				continue;
			}
			//グレードがしっかり満ちているか調べる
			if(tmg.getTableLevel(openindex, 3) < grade) {
				if(grade >= 4) {
					if(!playerdata.getPerm().contains(enchantname)) {
						continue;
					}
				}else {
					continue;
				}
			}
			//こだわりがないか調べる
			int kdwr = -1;
			if(new TWEnchant(player,plugin,tmg).containsEnchant("こだわり")) {
				kdwr = new TWEnchant(player,plugin,tmg).getEnchantLevel("こだわり");
			}
			if(kdwr != -1) {
				if(level != kdwr) {
					continue;
				}
			}
			//対象範囲にあるかを調べる
			if(base.getType() == Material.CHAINMAIL_HELMET || base.getType() == Material.DIAMOND_HELMET || base.getType() == Material.GOLDEN_HELMET
					|| base.getType() == Material.IRON_HELMET || base.getType() == Material.LEATHER_HELMET || base.getType() == Material.NETHERITE_HELMET) {
				if(target.charAt(target.length() - 1) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.CHAINMAIL_CHESTPLATE || base.getType() == Material.DIAMOND_CHESTPLATE || base.getType() == Material.GOLDEN_CHESTPLATE
					|| base.getType() == Material.IRON_CHESTPLATE || base.getType() == Material.LEATHER_CHESTPLATE || base.getType() == Material.NETHERITE_CHESTPLATE) {
				if(target.charAt(target.length() - 2) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.CHAINMAIL_LEGGINGS || base.getType() == Material.DIAMOND_LEGGINGS || base.getType() == Material.GOLDEN_LEGGINGS
					|| base.getType() == Material.IRON_LEGGINGS || base.getType() == Material.LEATHER_LEGGINGS || base.getType() == Material.NETHERITE_LEGGINGS) {
				if(target.charAt(target.length() - 3) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.CHAINMAIL_BOOTS || base.getType() == Material.DIAMOND_BOOTS || base.getType() == Material.GOLDEN_BOOTS
					|| base.getType() == Material.IRON_BOOTS || base.getType() == Material.LEATHER_BOOTS || base.getType() == Material.NETHERITE_BOOTS) {
				if(target.charAt(target.length() - 4) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.DIAMOND_SWORD || base.getType() == Material.GOLDEN_SWORD || base.getType() == Material.IRON_SWORD
					|| base.getType() == Material.NETHERITE_SWORD || base.getType() == Material.STONE_SWORD || base.getType() == Material.WOODEN_SWORD) {
				if(target.charAt(target.length() - 5) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.BOW) {
				if(target.charAt(target.length() - 6) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.CROSSBOW) {
				if(target.charAt(target.length() - 7) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.TRIDENT) {
				if(target.charAt(target.length() - 8) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.FISHING_ROD) {
				if(target.charAt(target.length() - 9) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.SHIELD) {
				if(target.charAt(target.length() - 10) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.COOKED_BEEF || base.getType() == Material.BREAD || base.getType() == Material.APPLE ||
					base.getType() == Material.GOLDEN_APPLE || base.getType() == Material.ENCHANTED_GOLDEN_APPLE) {
				if(target.charAt(target.length() - 11) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.IRON_INGOT || base.getType() == Material.GOLD_INGOT || base.getType() == Material.DIAMOND ||
					base.getType() == Material.REDSTONE || base.getType() == Material.EMERALD || base.getType() == Material.AMETHYST_SHARD ||
					base.getType() == Material.NETHERITE_INGOT) {
				if(target.charAt(target.length() - 12) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.WOODEN_PICKAXE || base.getType() == Material.STONE_PICKAXE || base.getType() == Material.IRON_PICKAXE ||
					base.getType() == Material.GOLDEN_PICKAXE || base.getType() == Material.DIAMOND_PICKAXE || base.getType() == Material.NETHERITE_PICKAXE) {
				if(target.charAt(target.length() - 13) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.WOODEN_AXE || base.getType() == Material.STONE_AXE || base.getType() == Material.IRON_AXE ||
					base.getType() == Material.GOLDEN_AXE || base.getType() == Material.DIAMOND_AXE || base.getType() == Material.NETHERITE_AXE) {
				if(target.charAt(target.length() - 14) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.WOODEN_HOE || base.getType() == Material.STONE_HOE || base.getType() == Material.IRON_HOE ||
					base.getType() == Material.GOLDEN_HOE || base.getType() == Material.DIAMOND_HOE || base.getType() == Material.NETHERITE_HOE) {
				if(target.charAt(target.length() - 15) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.NETHER_STAR) {
				if(target.charAt(target.length() - 16) == '1') {
					sugenchants.add(koho);
				}
			}else if(base.getType() == Material.EGG) {
				if(target.charAt(target.length() - 17) == '1') {
					sugenchants.add(koho);
				}
			}
			else {
				if(target.charAt(target.length() - 18) == '1') {
					sugenchants.add(koho);
				}
			}
		}
		if(sugenchants.size() >= 1) {
			Collections.shuffle(sugenchants);
			int max = sugenchants.size()>2?3:sugenchants.size();
			for(int i = 0 ; i < max ; i++) {
				Object[] sug = sugenchants.get(i);
				ItemStack enchant = new ItemStack(Material.PAPER);
				ItemMeta enchantm = enchant.getItemMeta();
				enchantm.setDisplayName((String)sug[0]);
				List<String> lore = new ArrayList<String>();
				lore.add((TWData.getMaxLevel((String)sug[0])==(int)sug[1]?ChatColor.RED:ChatColor.GOLD) + "Lv." + (int)sug[1]);
				lore.add(ChatColor.WHITE + TWData.getDiscription((String)sug[0]));
				enchantm.setLore(lore);
				enchant.setItemMeta(enchantm);
				inv.setItem(i + 12, enchant);
			}
		}
	}
	
	@Override
	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		if(e.getPlayer() == player) {
			Inventory inv = e.getInventory();
			if(inv.getItem(10) != null) {
				if(inv.getItem(10).getType() != Material.AIR) {
					ItemStack item = inv.getItem(10).clone();
					player.getInventory().addItem(item);
				}
			}
			if(inv.getItem(16) != null) {
				if(inv.getItem(16).getType() != Material.AIR) {
					ItemStack item = inv.getItem(16).clone();
					player.getInventory().addItem(item);
				}
			}
			cancelEvents();
		}
	}
}
