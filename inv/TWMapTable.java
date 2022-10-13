package inv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import tw.TMGame;

public class TWMapTable extends TWInventory{

	public TWMapTable(Plugin plugin, Player player, TMGame tmg, int openindex) {
		super(plugin, player, tmg, openindex);
		Inventory inv = Bukkit.createInventory(null, 9,"製図台");
		openInventory(inv);
		fill();
	}
	
	void fill() {
		Inventory inv = player.getOpenInventory().getTopInventory();
		ItemStack confirm = new ItemStack(Material.FEATHER);
		ItemMeta confirmm = confirm.getItemMeta();
		confirmm.setDisplayName("製図する");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "1回：" + (8 - (tmg.getTableLevel(openindex, 2) * 2)) + "レベル");
		confirmm.setLore(lore);
		confirm.setItemMeta(confirmm);
		inv.setItem(4, confirm);
	}

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(e.getRawSlot() < 9) {
				if(e.getCurrentItem() != null) {
					if(e.getCurrentItem().getType() != Material.AIR) {
						int level = player.getLevel() - (8 - (tmg.getTableLevel(tmg.getPlayerIndex(player), 2) * 2));
						if(level < 0) {
							player.sendMessage(ChatColor.RED + "レベルが足りません！");
						}else {
							if(tmg.getTreasureSize() == 0) {
								player.sendMessage(ChatColor.RED + "どうやら全ての宝はもう取られてしまったようだ...");
							}else {
								playerdata.reduceLevel((8 - (tmg.getTableLevel(tmg.getPlayerIndex(player), 2) * 2)));
								ItemStack paper = new ItemStack(Material.PAPER);
								ItemMeta paperm = paper.getItemMeta();
								paperm.setDisplayName("宝の地図");
								List<String> lore = new ArrayList<String>();
								List<Location> ts = new ArrayList<Location>();
								ts.addAll(tmg.getTreasures().keySet());
								int rndm = new Random().nextInt(ts.size());
								lore.add("x:" + (int)ts.get(rndm).getX());
								lore.add("z:" + (int)ts.get(rndm).getZ());
								paperm.setLore(lore);
								paper.setItemMeta(paperm);
								player.getInventory().addItem(paper);
								player.sendMessage(ChatColor.GOLD + "宝の地図を製図した！");
							}
						}
						player.closeInventory();
					}
				}
			}
		}
	}

}
