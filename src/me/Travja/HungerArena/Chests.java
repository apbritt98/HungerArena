package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Chests implements Listener {
	public Main plugin;
	public Chests(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void ChestSaves(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		Player p = event.getPlayer();
		//currently crashes the server when refilling....
		//Kinda glitchy through all here...
		if(p.getItemInHand().getType()== Material.BLAZE_ROD && event.getAction() == Action.LEFT_CLICK_BLOCK){
			if(block.getState() instanceof Chest){
				ItemStack[] itemsinchest = ((Chest) block.getState()).getInventory().getContents();
				int blockx = block.getX();
				int blocky = block.getY();
				int blockz = block.getZ();
				String blockw = block.getWorld().getName().toString();
				if(!plugin.getConfig().contains("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X")){
					plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X", blockx);
					plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Y", blocky);
					plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Z",blockz);
					plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.W", blockw);
					plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".ItemsInStorage", itemsinchest);
				}else{
					plugin.getConfig().set("Storage." + blockx + "," + blocky+ "," + blockz + ".Location.X",blockx);
					plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Y", blocky);
					plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Z", blockz);
					plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.W", blockw);
					plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".ItemsInStorage", itemsinchest);
				}
				List<String> list2 = plugin.getConfig().getStringList("StorageXYZ");
				list2.add(blockx + "," + blocky + "," + blockz);
				plugin.getConfig().set("StorageXYZ", list2);
				plugin.getConfig().options().copyDefaults(true);
				plugin.saveConfig();
			}
		}
	}
}