package inv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import tw.TMGame;
import tw.TWData;

public class TWTreasure extends TWInventory{

	boolean selectbool = false;
	List<ItemStack> list = new ArrayList<ItemStack>();
	
	public TWTreasure(Plugin plugin, Player player, TMGame tmg, int openindex) {
		super(plugin, player, tmg, openindex);
		Inventory inv = Bukkit.createInventory(null, 9,"宝箱の鑑定結果");
		for(int i = 0 ; i < 3 ; i++) {
			list.add(TWData.getTreasure());
			inv.setItem(i + 3, list.get(i));
		}
		openInventory(inv);
	}

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().getType() != Material.AIR) {
					selectbool = true;
					ItemStack select = e.getCurrentItem().clone();
					player.getInventory().addItem(select);
					player.closeInventory();
				}
			}
		}
	}
	
	@Override
	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		if(e.getPlayer() == player) {
			HandlerList.unregisterAll(this);
			if(!selectbool) {
				selectbool = true;
				ItemStack random = list.get(new Random().nextInt(list.size()));
				player.getInventory().addItem(random);
			}
		}
	}

}
