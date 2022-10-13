package tw;

import org.bukkit.plugin.java.JavaPlugin;


public class TWMain extends JavaPlugin{
	
	//TODO 釣りの掘り下げ
	
	@Override
	public void onEnable() {
		super.onEnable();
		getCommand("tw").setExecutor(new TMCommand(this));
		getCommand("r").setExecutor(new RenameCommand(this));
	}
}
