package tw;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;



public class RenameCommand implements CommandExecutor {

	Plugin plugin;
	
	public RenameCommand(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(args.length == 0) {
				player.sendMessage(ChatColor.RED + "名前は最低でも1文字必要です！");
			}else {
				if(player.getInventory().getItemInMainHand() != null) {
					if(player.getInventory().getItemInMainHand().getType() != Material.AIR) {
						switch(player.getInventory().getItemInMainHand().getType()) {
						case BOOK:
							player.sendMessage(ChatColor.RED + "エンチャント記録書は名前を変えられません！");
							break;
						case TRAPPED_CHEST:
							player.sendMessage(ChatColor.RED + "宝箱は名前を変えられません！");
							break;
						default:
							ItemStack item = player.getInventory().getItemInMainHand();
							ItemMeta itemm = item.getItemMeta();
							itemm.setDisplayName(String.join(" ",args));
							item.setItemMeta(itemm);
							break;
						}
					}
				}
			}
		}
		return true;
	}

}
