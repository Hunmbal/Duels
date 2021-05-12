package me.mustafa.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

@Getter
public class DuelEndEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	
	private Player loser;
	private Player winner;

	
	
	public DuelEndEvent(Player p, Player p2) {
		loser = p;
		winner = p2;
		
	}

}
