package tw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tw.TWData.mateTypes;

public class TWSummon {

	Plugin plugin;
	TMGame tmg;
	int openindex;
	
	public TWSummon(Plugin plugin,TMGame tmg) {
		this.plugin = plugin;
		this.tmg = tmg;
	}
	
	public TWSummon(Plugin plugin,TMGame tmg,int openindex) {
		this.plugin = plugin;
		this.tmg = tmg;
		this.openindex = openindex;
	}
	
	public void summonShipCrafter(Player master,int index) {
		Location base = tmg.locations[openindex][0].toLocation(master.getWorld());
		switch(index) {
		case 0:
			LivingEntity zombie = (LivingEntity)master.getWorld().spawnEntity(base, EntityType.ZOMBIE);
			TWEntity twez = new TWEntity(plugin,zombie,tmg);
			twez.setMaster(master);
			twez.setMateTypes(mateTypes.Active);
			tmg.entities.put(zombie, twez);
			break;
		case 1:
			LivingEntity skeleton = (LivingEntity)master.getWorld().spawnEntity(base, EntityType.SKELETON);
			TWEntity twes = new TWEntity(plugin,skeleton,tmg);
			twes.setMateTypes(mateTypes.Stay);
			twes.setMaster(master);
			tmg.entities.put(skeleton, twes);
			break;
		case 2:
			LivingEntity husk = (LivingEntity)master.getWorld().spawnEntity(base, EntityType.HUSK);
			TWEntity tweh = new TWEntity(plugin,husk,tmg);
			tweh.setMaster(master);
			tweh.setMateTypes(mateTypes.Active);
			tmg.entities.put(husk, tweh);
			break;
		case 3:
			LivingEntity stray = (LivingEntity)master.getWorld().spawnEntity(base, EntityType.STRAY);
			TWEntity twest = new TWEntity(plugin,stray,tmg);
			twest.setMaster(master);
			twest.setMateTypes(mateTypes.Stay);
			tmg.entities.put(stray, twest);
			break;
		case 4:
			LivingEntity ender = (LivingEntity)master.getWorld().spawnEntity(base, EntityType.ENDERMAN);
			TWEntity tween = new TWEntity(plugin,ender,tmg);
			tween.setMaster(master);
			tween.setMateTypes(mateTypes.Free);
			tmg.entities.put(ender, tween);
			break;
		case 5:
			Slime slime = (Slime)master.getWorld().spawnEntity(base, EntityType.SLIME);
			slime.setSize(new Random().nextInt(3));
			TWEntity twesl = new TWEntity(plugin,slime,tmg);
			twesl.setMaster(master);
			twesl.setMateTypes(mateTypes.Stay);
			tmg.entities.put(slime, twesl);
			//TODO 分裂スライムの敵対
			break;
		case 6:
			List<Integer> indxs = new ArrayList<Integer>(Arrays.asList(0,1,2,3));
			indxs.remove(openindex);
			MagmaCube mgm = (MagmaCube)master.getWorld().spawnEntity(base, EntityType.SLIME);
			mgm.setSize(new Random().nextInt(3));
			TWEntity twemg = new TWEntity(plugin,mgm,tmg);
			twemg.setMaster(master);
			twemg.setMateTypes(mateTypes.Invader);
			tmg.entities.put(mgm, twemg);
			//TODO 分裂マグマクリームの敵対
			break;
		}
	}
	
	public LivingEntity summonGhost(EntityType et,Location l) {
		LivingEntity ghost = (LivingEntity)l.getWorld().spawnEntity(l, et);
		ghost.setRemoveWhenFarAway(false);
		ghost.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,9999999,1));
		ghost.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,99999999,1));
		TWEntity tweghost = new TWEntity(plugin,ghost,tmg);
		tmg.entities.put(ghost, tweghost);
		return ghost;
	}
	
	public void summonNormal(EntityType et,Location l) {
		LivingEntity monster = (LivingEntity)l.getWorld().spawnEntity(l, et);
		TWEntity twemons = new TWEntity(plugin,monster,tmg);
		tmg.entities.put(monster, twemons);
	}
}
