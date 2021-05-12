package me.mustafa.duels.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.mustafa.duels.Main;

@Getter
public class MatchManager {

	private Main plugin = Main.getInstance();
	private MatchStats matchStats = new MatchStats();
	
	private HashMap<Player, Player> selectingkit = new HashMap<Player, Player>(); 
	private HashMap<Player, String> duelkit = new HashMap<Player, String>();

	private HashMap<Player, Player> pending = new HashMap<Player, Player>();
	private HashMap<UUID, List<Player>> matchPlayers = new HashMap<UUID, List<Player>>();
	
	private HashMap<Player, UUID> matchID = new HashMap<Player, UUID>();
	
	private List<Player> waiting = new ArrayList<>();
	private List<Player> playing = new ArrayList<>();
	
	private BukkitTask expiry;	
	
	public void addPending(String kit, Player p, Player target) {
		
		this.duelkit.put(p, kit);
		this.pending.put(p, target);
		
		expiry = (new BukkitRunnable() {
	          public void run() {
	        	  
	        	  duelkit.remove(p);
	        	  selectingkit.remove(p);
	        	  pending.remove(p);
		            
		          }
		        }).runTaskLater(this.plugin, 60*20); 
		
	}
	
	
	public void startMatch(Player p1, Player p2) {
		this.waiting.remove(p1);
		this.waiting.remove(p2);
		
		this.playing.add(p1);
		this.playing.add(p2);
		
		
	}
	
	public void removeMatch(Player p1, Player p2) {
		this.playing.remove(p1);
		this.playing.remove(p2);
		
		this.waiting.remove(p1);
		this.waiting.remove(p2);
		
		this.matchPlayers.remove(p1.getUniqueId());
		
		this.matchID.remove(p1);
		this.matchID.remove(p2);
	}
	
	List<Player> pList = new ArrayList<>();
	public void createMatch(Player p1, Player p2) {

		this.matchID.put(p1, p1.getUniqueId());
		this.matchID.put(p2, p1.getUniqueId());
		
		pList.add(p1);
		pList.add(p2);
		
		this.matchPlayers.put(p1.getUniqueId(), pList);
		pList.clear();
		
	}
	
}
