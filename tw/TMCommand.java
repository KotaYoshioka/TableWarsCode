package tw;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class TMCommand implements CommandExecutor {

	Plugin plugin;
	TMGame tmg;
	
	public TMCommand(Plugin plugin) {
		this.plugin  = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(args[0].equals("newgame")) {
				tmg = new TMGame(plugin);
			}else if(args[0].equals("join")) {
				if(args[1].equals("@a")) {
					for(Player p:Bukkit.getOnlinePlayers()) {
						tmg.join(p);
					}
				}else {
					tmg.join(player);
				}
			}else if(args[0].equals("start")) {
				tmg.start();
			}else if(args[0].equals("end")) {
				tmg.end();
			}else if(args[0].equals("rotate")){
				Vector v = player.getLocation().getDirection().clone();
				player.sendMessage("x:" + v.getX() + "   y:" + v.getY() + "    z:" + v.getZ());
			}
		}
		return true;
	}

}
