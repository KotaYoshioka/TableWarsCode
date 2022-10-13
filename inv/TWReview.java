package inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import tw.TMGame;

public class TWReview extends TWInventory{

	public TWReview(Plugin plugin, Player player, TMGame tmg, int openindex, Player target) {
		super(plugin, player, tmg, openindex);
		Inventory inv = Bukkit.createInventory(null, 9,target.getDisplayName() + "の校閲結果");
		if(target.getInventory().getItemInMainHand() != null) {
			if(target.getInventory().getItemInMainHand().getType() != Material.AIR) {
				inv.setItem(4, target.getInventory().getItemInMainHand().clone());
			}
		}
		openInventory(inv);
	}

	@Override
	public void clickInventory(InventoryClickEvent e) {
		e.setCancelled(true);
	}

}
