package inv;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import tw.TMGame;
import tw.TWPlayer;

public abstract class TWInventory implements Listener{

	Plugin plugin;
	TMGame tmg;
	Player player;
	TWPlayer playerdata;
	int openindex;
	World world;
	
	public TWInventory(Plugin plugin,Player player,TMGame tmg,int openindex) {
		this.plugin = plugin;
		this.tmg = tmg;
		this.player = player;
		this.openindex = openindex;
		this.world = player.getWorld();
		this.playerdata = tmg.getPlayerData(player);
	}
	
	public void openInventory(Inventory inv) {
		player.openInventory(inv);
		registerEvents();
	}
	
	public void cancelEvents() {
		HandlerList.unregisterAll(this);
	}
	
	public void registerEvents() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		if(e.getPlayer() == player) {
			cancelEvents();	
		}
	}
	
	@EventHandler
	public abstract void clickInventory(InventoryClickEvent e);
}
