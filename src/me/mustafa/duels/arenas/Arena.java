package me.mustafa.duels.arenas;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Arena {
	
	private String name;
	private Location a;
	private Location b;
	private boolean isWhitelisted = false;
	
	

}
