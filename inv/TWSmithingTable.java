package inv;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tw.TMGame;
import tw.TWData;

public class TWSmithingTable extends TWInventory{

	int category = 0;
	boolean confirm = false;
	ItemStack confirmItem;
	int confirmPrice;
	
	
	public TWSmithingTable(Plugin plugin, Player player, TMGame tmg, int openindex) {
		super(plugin, player, tmg, openindex);
		Inventory inv = Bukkit.createInventory(null, 54,"鍛冶台");
		openInventory(inv);
		fill();
	}
	
	void fill() {
		Inventory inv = player.getOpenInventory().getTopInventory();
		inv.clear();
		Material[] ms = {Material.STONE_SWORD,Material.IRON_CHESTPLATE,Material.COOKED_BEEF,Material.GOLD_INGOT,Material.FISHING_ROD,Material.PAPER};
		String[] names = {"武器","装備","食事","媒体","その他","セルフ"};
		for(int i = 0 ; i < ms.length ; i++) {
			ItemStack categories = new ItemStack(ms[i]);
			ItemMeta categoriesm = categories.getItemMeta();
			categoriesm.setDisplayName(names[i]);
			if(category == i) {
				categoriesm.addEnchant(Enchantment.LUCK, 1,true);
				categoriesm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			categories.setItemMeta(categoriesm);
			inv.setItem(i, categories);
		}
		int gray = 9;
		if(category == 5) {
			gray = 45;
		}
		for(int i = 0 ; i < gray ; i++) {
			ItemStack none = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta nonem = none.getItemMeta();
			nonem.setDisplayName(" ");
			none.setItemMeta(nonem);
			inv.setItem(i + 9, none);
		}
		if(category != 5) {
			List<ItemStack> items = new ArrayList<ItemStack>();
			items.addAll(TWData.getShopItems(tmg.getTableLevel(openindex, 4), category));
			for(int i = 0 ; i < items.size() ; i++) {
				ItemStack item = items.get(i);
				inv.setItem(i+18, item);
			}	
		}else {
			int[] selfindex = {29,31,33};
			for(int i = 0 ; i < selfindex.length ; i++) {
				if(tmg.getSelf(openindex, i)[0] != null) {
					ItemStack item = ((ItemStack)tmg.getSelf(openindex, i)[0]).clone();
					List<String> lore = new ArrayList<String>();
					lore.add(ChatColor.GOLD + "" + (int)tmg.getSelf(openindex, i)[1] + "レベル");
					if(item.getItemMeta().hasLore()) {
						for(String s:item.getItemMeta().getLore()) {
							lore.add(s);
						}
					}
					ItemMeta itemm = item.getItemMeta();
					itemm.setLore(lore);
					item.setItemMeta(itemm);
					inv.setItem(selfindex[i], item);
				}else {
					inv.setItem(selfindex[i], new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	void confirm() {
		confirm = true;
		Inventory confirmInv = Bukkit.createInventory(null, 9,"本当に商品にしますか？");
		ItemStack yes = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
		ItemMeta yesm = yes.getItemMeta();
		yesm.setDisplayName("はい");
		yes.setItemMeta(yesm);
		confirmInv.setItem(6, yes);
		ItemStack no = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta nom = no.getItemMeta();
		nom.setDisplayName("いいえ");
		no.setItemMeta(nom);
		confirmInv.setItem(2, no);
		openInventory(confirmInv);
		ItemStack item = confirmItem.clone();
		ItemMeta itemm = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "" + confirmPrice + "レベル");
		if(itemm.hasLore()) {
			for(String s:itemm.getLore()) {
				lore.add(s);
			}
		}
		itemm.setLore(lore);
		item.setItemMeta(itemm);
		confirmInv.setItem(4, item);
	}

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().getType() != Material.AIR) {
					if(!confirm) {
						if(e.getRawSlot() < 54) {
							if(e.getRawSlot() > 17) {
								ItemStack prise = e.getCurrentItem().clone();
								ItemMeta prisem = prise.getItemMeta();
								List<String> lore = new ArrayList<String>();
								prisem.setLore(lore);
								prise.setItemMeta(prisem);
								int price = TWData.getShopItemPrice(prise.getType());
								if(player.getLevel() >= price) {
									playerdata.reduceLevel(price);
									if(prise instanceof Damageable) {
										ItemMeta dam = prise.getItemMeta();
										Damageable da = (Damageable)dam;
										da.damage(10);
										prise.setItemMeta(dam);
									}
									player.getInventory().addItem(prise);
								}else {
									player.closeInventory();
									player.sendMessage(ChatColor.RED + "レベルが足りません！");
								}
							}else if(e.getRawSlot() < 9){
								category = e.getRawSlot();
								fill();
							}
						}else if(category == 5) {
							if(e.getRawSlot() > 53) {
								int unusedIndex = tmg.getUnusedSelfIndex(openindex);
								if(unusedIndex != -1 ) {
									if(checkSelf(e.getCurrentItem())) {
										confirm();	
									}else {
										player.closeInventory();
										player.sendMessage(ChatColor.RED + "このアイテムは商品にすることができません！");
									}
								}else {
									player.closeInventory();
									player.sendMessage(ChatColor.RED + "セルフショップにもう商品を並べることはできません！");
								}
							}else if(e.getRawSlot() > 28 && e.getRawSlot() < 34 && e.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE) {
								int clickIndex = e.getRawSlot();
								clickIndex = clickIndex==29?0:(clickIndex==31?1:2);
								ItemStack prise = (ItemStack)tmg.getSelf(openindex, clickIndex)[0];
								int price = (int)tmg.getSelf(openindex, clickIndex)[1];
								if(player.getLevel() - price >= 0) {
									playerdata.reduceLevel(price);
									player.getInventory().addItem(prise);
								}else {
									player.closeInventory();
									player.sendMessage(ChatColor.RED + "レベルが足りません！");
								}
							}
						}
					}else {
						if(e.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
							tmg.setSelf(openindex, tmg.getUnusedSelfIndex(openindex), confirmItem, confirmPrice);
							Inventory inv = Bukkit.createInventory(null, 54,"鍛冶台");
							openInventory(inv);
							fill();
							confirm = false;
						}else if(e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
							Inventory inv = Bukkit.createInventory(null, 54,"鍛冶台");
							openInventory(inv);
							fill();
							confirm = false;
						}
					}
				}
			}
		}
	}

	boolean checkSelf(ItemStack checkitem) {
		ItemStack item = checkitem.clone();
		item.setAmount(1);
		if(item.getType() == Material.BOOK) {
			return false;
		}
		int estimate = TWData.getShopItemPrice(item.getType());
		if(estimate == 999) {
			estimate = 25;
		}
		if(item.hasItemMeta()) {
			if(item.getItemMeta().hasLore()) {
				estimate += item.getItemMeta().getLore().size() * 4;
			}
		}
		confirmItem = item;
		confirmPrice = estimate;
		return true;
	}
}
