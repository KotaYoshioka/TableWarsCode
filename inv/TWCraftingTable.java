package inv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tw.TMGame;
import tw.TWData;

public class TWCraftingTable extends TWInventory{

	public TWCraftingTable(Plugin plugin, Player player, TMGame tmg,int openindex) {
		super(plugin, player, tmg,openindex);
		Inventory inv = Bukkit.createInventory(null, 45,"作業台");	
		openInventory(inv);
		setItems();
	}
	
	void setItems() {
		Inventory inv = player.getOpenInventory().getTopInventory();
		inv.clear();
		Material[] ms = {Material.CRAFTING_TABLE,Material.FLETCHING_TABLE,Material.CARTOGRAPHY_TABLE,Material.ENCHANTING_TABLE,Material.SMITHING_TABLE};
		String[] ss = {"経験値の産出速度が上昇","船の強化ができる","宝の地図を製図できる","エンチャントができる","アイテムを作成できる"};
		for(int i = 0 ; i < ms.length ; i++) {
			int firstindex = i * 9;
			ItemStack it = new ItemStack(ms[i]);
			ItemMeta itm = it.getItemMeta();
			itm.setLore(new ArrayList<String>(Arrays.asList(ss[i])));
			it.setItemMeta(itm);
			inv.setItem(firstindex + 2,it);
			for(int k = 0 ; k < 4 ; k++) {
				Material m = Material.GRAY_STAINED_GLASS_PANE;
				if(tmg.getTableLevel(openindex,i) >= k) {
					m = Material.GREEN_STAINED_GLASS_PANE;
				}else if(tmg.getTableLevel(openindex,i) + 1 == k) {
					if(player.getLevel() < TWData.requireLevels[i][k]) {
						m = Material.RED_STAINED_GLASS_PANE;
					}else {
						m = Material.CYAN_STAINED_GLASS_PANE;
					}
				}
				ItemStack glasspane = new ItemStack(m);
				ItemMeta glasspanem = glasspane.getItemMeta();
				glasspanem.setDisplayName(k + "");
				List<String> lore = new ArrayList<String>();
				lore.add(glasspane.getType()!=Material.GREEN_STAINED_GLASS_PANE?(ChatColor.GOLD + "" + TWData.requireLevels[i][k] + "レベル必要です"):(ChatColor.GREEN + "アップグレード済みです"));
				glasspanem.setLore(lore);
				glasspane.setItemMeta(glasspanem);
				inv.setItem(firstindex + 3 + k,glasspane); 
			}
		}
	}

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(e.getCurrentItem().getType() == Material.CYAN_STAINED_GLASS_PANE) {
				int tableindex = (int)e.getSlot() / 9;
				int cost = TWData.requireLevels[tableindex][tmg.getTableLevel(openindex,tableindex) + 1];
				playerdata.reduceLevel(cost);
				tmg.setTableLevel(openindex, tableindex, tmg.getTableLevel(openindex,tableindex) + 1);
				if(tmg.getTableLevel(openindex, tableindex) == 0) {
					Material[] ms = {Material.FLETCHING_TABLE,Material.CARTOGRAPHY_TABLE,Material.ENCHANTING_TABLE,Material.SMITHING_TABLE};
					tmg.getSettingLocation(openindex, tableindex+2).getBlock().setType(ms[tableindex-1]);
					//矢細工台解放時、船の出現と、アラームトラップの開放をする
					if(tableindex - 1 == 0) {
						//船の出現
						Location l = tmg.getSettingLocation(openindex, 7);
						l.setDirection(tmg.getShipRotate(openindex));
						world.spawnEntity(l,EntityType.BOAT);
						//アラームトラップの開放
						tmg.setTrap(openindex, 2, 0, true);
					}
				}
				setItems();
			}
		}
	}

}
