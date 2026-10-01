package com.itson.Scoreboard;

import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreboardPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    getLogger().info("Scoreboard v" + getPluginMeta().getVersion() + " enabled.");
  }

  @Override
  public void onDisable() {
    getLogger().info("Scoreboard has been disabled.");
  }
}
