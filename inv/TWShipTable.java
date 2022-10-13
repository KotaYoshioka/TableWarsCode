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
import tw.TWSummon;

public class TWShipTable extends TWInventory{

	int phase = 0;
	
	public TWShipTable(Plugin plugin, Player player, TMGame tmg, int openindex) {
		super(plugin, player, tmg, openindex);
		openMainScreen();
	}

	void openMainScreen() {
		phase = 0;
		Inventory inv = Bukkit.createInventory(null, 9,"船の細工");
		fillMain(inv);
		openInventory(inv);
	}
	void fillMain(Inventory inv) {
		Material[] ms = {Material.OAK_BOAT,Material.ZOMBIE_HEAD,Material.GLASS,Material.ARMOR_STAND,Material.GRINDSTONE};
		String[] ss = {"造船","船員","ボトルシップ","トラップ","砲撃"};
		for(int i = 0 ; i < ms.length ; i++) {
			ItemStack menus = new ItemStack(ms[i]);
			ItemMeta menusm = menus.getItemMeta();
			menusm.setDisplayName(ss[i]);
			menus.setItemMeta(menusm);
			inv.setItem(i, menus);
		}
	}
	
	void openShipCraft() {
		phase = 1;
		Inventory inv = Bukkit.createInventory(null, 36,"造船");
		fillShipCraft(inv);
		openInventory(inv);
	}
	void fillShipCraft(Inventory inv) {
		ItemStack makeShip = new ItemStack(Material.OAK_BOAT);
		ItemMeta makeShipm = makeShip.getItemMeta();
		makeShipm.setDisplayName("造船する");
		makeShipm.setLore(new ArrayList<String>(Arrays.asList(ChatColor.GOLD + "" + (2 + (playerdata.getMakeShipTime() * 2)) + "レベル")));
		makeShip.setItemMeta(makeShipm);
		inv.setItem(31, makeShip);
	}
	
	void openShipMate() {
		phase = 2;
		Inventory inv = Bukkit.createInventory(null, 36,"船員");
		fillShipMate(inv);
		openInventory(inv);
	}
	void fillShipMate(Inventory inv) {
		for(int i = 0 ; i < TWData.shipmates.length ; i++) {
			ItemStack mobs = new ItemStack((Material)TWData.shipmates[i][0]);
			ItemMeta mobsm = mobs.getItemMeta();
			mobsm.setDisplayName((String)TWData.shipmates[i][1]);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + "" + ((int)TWData.shipmates[i][3]) + "レベル");
			lore.add(ChatColor.GREEN + "タイプ：" + (TWData.getMateTypesName((TWData.mateTypes)TWData.shipmates[i][2])));
			lore.add(ChatColor.WHITE + ((String)TWData.shipmates[i][4]));
			mobsm.setLore(lore);
			mobs.setItemMeta(mobsm);
			inv.setItem(i, mobs);
		}
	}
	
	void openTrap() {
		phase = 4;
		Inventory inv = Bukkit.createInventory(null, 36,"トラップ");
		fillTrap(inv);
		openInventory(inv);
	}
	void fillTrap(Inventory inv) {
		for(int i = 0 ; i < 3 ; i ++) {
			int basicIndex = i * 9;
			ItemStack title = new ItemStack(Material.PAPER);
			ItemMeta titlem = title.getItemMeta();
			titlem.setDisplayName(i == 0?"近距離":(i==1?"中距離":"遠距離"));
			title.setItemMeta(titlem);
			inv.setItem(basicIndex, title);
			for(int k = 0 ; k < TWData.traps.length ; k++) {
				ItemStack trap = new ItemStack((Material)TWData.traps[k][0]);
				ItemMeta trapm = trap.getItemMeta();
				trapm.setDisplayName((String)TWData.traps[k][1]);
				List<String> lore = new ArrayList<String>();
				if(tmg.getTrap(openindex, i,k)) {
					lore.add(ChatColor.RED + "セット済");
				}
				lore.add(ChatColor.GOLD + "" + (int)TWData.traps[k][2] + "レベル");
				lore.add(ChatColor.WHITE + (String)TWData.traps[k][3]);
				trapm.setLore(lore);
				trap.setItemMeta(trapm);
				inv.setItem(basicIndex + (k+1), trap);
			}
		}
	}
	
	public void fill() {
		Inventory inv = player.getOpenInventory().getTopInventory();
		switch(phase) {
		case 0:
			fillMain(inv);
			break;
		case 1:
			fillShipCraft(inv);
			break;
		case 2:
			fillShipMate(inv);
			break;
		case 4:
			fillTrap(inv);
			break;
		}
	}
	
	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() == player) {
			e.setCancelled(true);
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
				ItemStack item = e.getCurrentItem().clone();
				int slot = e.getRawSlot();
				switch(phase) {
				//メイン画面
				case 0:
					switch(slot) {
					case 0:
						openShipCraft();
						break;
					case 1:
						openShipMate();
						break;
					case 3:
						openTrap();
						break;
					}
					break;
				//造船
				case 1:
					if(slot == 31) {
						//TODO オプション追加
						//TODO 値段変更に対応
						if(player.getLevel() - (2 + (playerdata.getMakeShipTime() * 2)) >= 0) {
							playerdata.reduceLevel((2 + (playerdata.getMakeShipTime() * 2)));
							Location l = tmg.getSettingLocation(openindex, 7);
							l.setDirection(tmg.getShipRotate(openindex));
							player.getWorld().spawnEntity(l,EntityType.BOAT);
							player.closeInventory();
							player.sendMessage(ChatColor.GOLD + "造船しました！");
						}else {
							player.sendMessage(ChatColor.RED + "レベルが足りません！");
						}
					}
					break;
				//船員
				case 2:
					if(slot < TWData.shipmates.length) {
						if(player.getLevel() - (int)TWData.shipmates[e.getRawSlot()][3] >= 0) {
							playerdata.reduceLevel((int)TWData.shipmates[e.getRawSlot()][3]);
							new TWSummon(plugin,tmg,openindex).summonShipCrafter(player, e.getRawSlot());
							player.sendMessage(ChatColor.GOLD + "一匹、仲間に加わりました！");
						}else {
							player.closeInventory();
							player.sendMessage(ChatColor.RED + "レベルが足りません！");
						}
					}
					break;
				//トラップ
				case 4:
					if(e.getRawSlot() < 35) {
						int buying = e.getRawSlot() % 9;
						int buyLevel = e.getRawSlot() / 9;
						if(!tmg.getTrap(openindex, buyLevel, buying - 1)) {
							if(player.getLevel() >= (int)TWData.traps[buying-1][2]) {
								tmg.setTrap(openindex, buyLevel, buying-1, true);
								playerdata.reduceLevel((int)TWData.traps[buying-1][2]);
							}else {
								player.closeInventory();
								player.sendMessage(ChatColor.RED + "レベルが足りません！");
							}
						}
						fill();
					}
					break;
				}
			}
		}
	}

}
