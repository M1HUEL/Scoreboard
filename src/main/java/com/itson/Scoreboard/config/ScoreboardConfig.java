package com.itson.Scoreboard.config;

import com.itson.Scoreboard.ScoreboardPlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public final class ScoreboardConfig {

  private final ScoreboardPlugin plugin;
  private boolean enabled;
  private int updateIntervalTicks;
  private String title;
  private List<String> lines;

  public ScoreboardConfig(ScoreboardPlugin plugin) {
    this.plugin = plugin;
    reload();
  }

  public void reload() {
    plugin.saveDefaultConfig();
    plugin.reloadConfig();

    FileConfiguration config = plugin.getConfig();
    enabled = config.getBoolean("enabled", true);
    updateIntervalTicks = Math.max(1, config.getInt("update-interval-ticks", 20));
    title = config.getString("title", "<gradient:#00c6ff:#0072ff>Scoreboard</gradient>");
    lines = new ArrayList<>(config.getStringList("lines"));
  }

  public boolean isEnabled() {
    return enabled;
  }

  public int getUpdateIntervalTicks() {
    return updateIntervalTicks;
  }

  public String getTitle() {
    return title;
  }

  public List<String> getLines() {
    return lines;
  }
}
