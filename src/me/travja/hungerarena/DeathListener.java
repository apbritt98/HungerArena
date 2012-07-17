package me.Travja.HungerArena;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener{
	public Main plugin;
	public DeathListener(Main m){
		this.plugin = m;
	}
	public FileConfiguration config;
	int i = 0;
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Dead.contains(pname)){
			String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
			World spawnw = plugin.getServer().getWorld(Spawncoords[3]);
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			event.setRespawnLocation(Spawn);
		}
	}
	@EventHandler
    @SuppressWarnings("SizeReplaceableByIsEmpty")
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		Server s = p.getServer();
		String pname = p.getName();
		String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
		World spawnw = plugin.getServer().getWorld(Spawncoords[3]);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(plugin.Playing.contains(pname)){
			if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
				double y = p.getLocation().getY();
				double newy = y+200;
				double x = p.getLocation().getX();
				double z = p.getLocation().getZ();
				Location strike = new Location(p.getWorld(), x, newy, z);
				p.getWorld().strikeLightning(strike);
			}
			plugin.Dead.add(pname);
			plugin.Playing.remove(pname);
			String leftmsg = ChatColor.BLUE + "There are now " + plugin.Playing.size() + " tributes left!";
			if(p.getKiller() instanceof Player){
				if(p.getKiller().getItemInHand().getType().getId()== 0){
					Player killer = p.getKiller();
					String killername = killer.getName();
					event.setDeathMessage("");
					s.broadcastMessage(ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with their FIST!");
					s.broadcastMessage(leftmsg);
					if(plugin.Playing.size()== 1 && plugin.canjoin== true){
						//Announce winner
						String winnername = plugin.Playing.get(i++);
						Player winner = plugin.getServer().getPlayerExact(winnername);
						String winnername2 = winner.getName();
						plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
						winner.getInventory().clear();
						winner.teleport(Spawn);
						winner.getInventory().setBoots(null);
						winner.getInventory().setChestplate(null);
						winner.getInventory().setHelmet(null);
						winner.getInventory().setLeggings(null);
						winner.getInventory().addItem(plugin.Reward);
						plugin.Playing.clear();
						//Show spectators
						if(!plugin.Watching.isEmpty()){
							String s1 = plugin.Watching.get(i++);
							Player spectator = plugin.getServer().getPlayerExact(s1);
							spectator.setAllowFlight(false);
							spectator.teleport(Spawn);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.showPlayer(spectator);
							}
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");
						}
					}
				}else{
					Player killer = p.getKiller();
					String killername = killer.getName();
					Material weapon = killer.getItemInHand().getType();
					String msg = ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with a(n) " + weapon;
					event.setDeathMessage("");
					s.broadcastMessage(msg);
					s.broadcastMessage(leftmsg);
					if(plugin.Playing.size()== 1 && plugin.canjoin== true){
						//Announce winner
						String winnername = plugin.Playing.get(i++);
						Player winner = plugin.getServer().getPlayerExact(winnername);
						String winnername2 = winner.getName();
						plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
						winner.getInventory().clear();
						winner.teleport(Spawn);
						winner.getInventory().setBoots(null);
						winner.getInventory().setChestplate(null);
						winner.getInventory().setHelmet(null);
						winner.getInventory().setLeggings(null);
						winner.getInventory().addItem(plugin.Reward);
						plugin.Playing.clear();
						//Show spectators
						if(plugin.Watching.size() != 0){
							String s1 = plugin.Watching.get(i++);
							Player spectator = plugin.getServer().getPlayerExact(s1);
							spectator.setAllowFlight(false);
							spectator.teleport(Spawn);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.showPlayer(spectator);
							}
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");
						}
					}
				}
			}else{
				event.setDeathMessage("");
				s.broadcastMessage(ChatColor.LIGHT_PURPLE + pname + " died of natural causes!");
				s.broadcastMessage(leftmsg);
				if(plugin.Playing.size()== 1 && plugin.canjoin== true){
					//Announce winner
					String winnername = plugin.Playing.get(i++);
					Player winner = plugin.getServer().getPlayerExact(winnername);
					String winnername2 = winner.getName();
					plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
					winner.getInventory().clear();
					winner.teleport(Spawn);
					winner.getInventory().setBoots(null);
					winner.getInventory().setChestplate(null);
					winner.getInventory().setHelmet(null);
					winner.getInventory().setLeggings(null);
					winner.getInventory().addItem(plugin.Reward);
                                        // Create the event here
                                        PlayerWinGamesEvent winevent = new PlayerWinGamesEvent(winner);
                                        // Call the event
                                        Bukkit.getServer().getPluginManager().callEvent(winevent);
					plugin.Playing.clear();
					//Show spectators
					if(!plugin.Watching.isEmpty()){
						String s1 = plugin.Watching.get(i++);
						Player spectator = plugin.getServer().getPlayerExact(s1);
						spectator.setAllowFlight(false);
						spectator.teleport(Spawn);
						for(Player online:plugin.getServer().getOnlinePlayers()){
							online.showPlayer(spectator);
						}
					}
					if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");
					}
				}
			}
		}
	}
}