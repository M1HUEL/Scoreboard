package com.itson.Scoreboard.listener;

import com.itson.Scoreboard.ScoreboardPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {

  private final ScoreboardPlugin plugin;

  public PlayerListener(ScoreboardPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onJoin(PlayerJoinEvent event) {
    plugin.getScoreboardService().create(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onQuit(PlayerQuitEvent event) {
    plugin.getScoreboardService().remove(event.getPlayer());
  }
}
