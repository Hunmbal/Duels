package me.mustafa.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

@Getter
public class DuelStartEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	
	private Player player1;
	private Player player2;
	private String kit;
	
	
	public DuelStartEvent(Player p1, Player p2, String k) {
		player1 = p1;
		player2 = p2;
		kit = k;
	}
	
	
	
	
	
	
	

}
