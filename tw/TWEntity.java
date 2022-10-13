package tw;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import tw.TWData.mateTypes;

public class TWEntity implements Listener{

	Plugin plugin;
	TMGame tmg;
	LivingEntity body;
	//マスター
	Player master;
	//攻撃しない対象
	List<LivingEntity> notAttack = new ArrayList<LivingEntity>();
	//生きているか
	boolean live = true;
	//行動タイプ
	mateTypes mt = null;
	
	public TWEntity(Plugin plugin,LivingEntity body,TMGame tmg) {
		this.plugin = plugin;
		this.tmg = tmg;
		this.body = body;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void setMaster(Player player) {
		this.master = player;
		addNotAttack(player);
	}
	
	public void setMateTypes(mateTypes mt) {
		this.mt = mt;
	}
	
	public void addNotAttack(LivingEntity le) {
		if(!notAttack.contains(le)) {
			notAttack.add(le);
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity() == body) {
			death();
		}
	}
	
	public void death() {
		live = false;
		if(!body.isDead()) {
			body.remove();
		}
		if(master != null) {
			tmg.getPlayerData(master).allies.remove(this);
		}
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(e.getEntity() == body) {
			if(notAttack.contains(e.getTarget())) {
				e.setCancelled(true);
			}else if(e.getTarget() instanceof LivingEntity && tmg.containsLivingEntity((LivingEntity)e.getTarget())) {
				if(tmg.entities.get((LivingEntity)e.getTarget()).getMaster() != null && tmg.entities.get((LivingEntity)e.getTarget()).getMaster() == master) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	//TODO 索敵の仕組みを詳しく
	public void search() {
		new BukkitRunnable() {
			public void run() {
				if(!live) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 40);
	}
	
	public Player getMaster() {
		return master;
	}
}
