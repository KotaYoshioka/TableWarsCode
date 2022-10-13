package inv;

import java.util.ArrayList;
import java.util.Arrays;

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

public class TWLiza extends TWInventory{

	int category = 0;
	int index;
	Inventory inv;
	
	public TWLiza(Plugin plugin, Player player, TMGame tmg, int openindex,int index) {
		super(plugin, player, tmg, openindex);
		inv = Bukkit.createInventory(null, 54,"リザ");
		this.index = index;
		openInventory(inv);
		fill();
	}
	
	void fill() {
		Inventory inv = player.getOpenInventory().getTopInventory();
		inv.clear();
		Material[] ms = {Material.NETHERITE_CHESTPLATE,Material.BOOK};
		String[] names = {"一般","開放本"};
		for(int i = 0 ; i < ms.length ; i++) {
			ItemStack categories = new ItemStack(ms[i]);
			ItemMeta categoriesm = categories.getItemMeta();
			categoriesm.setDisplayName(names[i]);
			if(category == i) {
				categoriesm.addEnchant(Enchantment.LUCK, 1,true);
				categoriesm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			if(i == 1) {
				categoriesm.setLore(new ArrayList<String>(Arrays.asList(ChatColor.GOLD + "一律" + 18 + "レベル")));
			}
			categories.setItemMeta(categoriesm);
			inv.setItem(i, categories);
		}
		int gray = 9;
		for(int i = 0 ; i < gray ; i++) {
			ItemStack none = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta nonem = none.getItemMeta();
			nonem.setDisplayName(" ");
			none.setItemMeta(nonem);
			inv.setItem(i + 9, none);
		}
		for(int i = 0 ; i < (category==0?tmg.getLizaItems(index):tmg.getLizaBooks(index)).size() ; i++) {
			inv.setItem(i + 18, category==0?tmg.getLizaItems(index).get(i):tmg.getLizaBooks(index).get(i));
		}
	}
	

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
				if(e.getRawSlot() < 54 && e.getRawSlot() > 17) {
					if(category==0?tmg.getLizaItems(index).contains(e.getCurrentItem()):tmg.getLizaBooks(index).contains(e.getCurrentItem())) {
						ItemStack prise = e.getCurrentItem().clone();
						int price = category==0?TWData.getShopItemPriceLiza(prise.getType()):18;
						if(player.getLevel() >= price) {
							playerdata.reduceLevel(price);
							if(prise instanceof Damageable) {
								ItemMeta dam = prise.getItemMeta();
								Damageable da = (Damageable)dam;
								da.damage(10);
								prise.setItemMeta(dam);
							}
							player.getInventory().addItem(prise);
							if(category == 0) {
								tmg.removeLizaItems(index, e.getCurrentItem());
							}else {
								tmg.removeLizaBooks(index, e.getCurrentItem());
							}
							fill();
						}else {
							player.sendMessage(ChatColor.RED + "レベルが足りません！");
							player.closeInventory();
						}	
					}else {
						player.sendMessage(ChatColor.RED + "すみません！もう品切れです！");
						player.closeInventory();
					}
				}else if(e.getRawSlot() < 9) {
					category = e.getRawSlot();
					fill();
				}
			}
		}
	}

}
