package com.itson.Scoreboard;

import com.itson.Scoreboard.command.ScoreboardCommand;
import com.itson.Scoreboard.config.ScoreboardConfig;
import com.itson.Scoreboard.listener.PlayerListener;
import com.itson.Scoreboard.scoreboard.ScoreboardService;
import com.itson.Scoreboard.util.PlaceholderBridge;
import com.itson.Scoreboard.util.Placeholders;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreboardPlugin extends JavaPlugin {

  private ScoreboardConfig scoreboardConfig;
  private ScoreboardService scoreboardService;

  @Override
  public void onEnable() {
    saveDefaultConfig();

    scoreboardConfig = new ScoreboardConfig(this);
    PlaceholderBridge.init(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null);
    scoreboardService = new ScoreboardService(this, new Placeholders(PlaceholderBridge.isEnabled()));
    if (PlaceholderBridge.isEnabled()) {
      PlaceholderBridge.registerExpansion(this);
      getLogger().info("Hooked into PlaceholderAPI.");
    }

    ScoreboardCommand command = new ScoreboardCommand(this);
    getCommand("scoreboard").setExecutor(command);
    getCommand("scoreboard").setTabCompleter(command);
    getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    scoreboardService.start();
    getLogger().info("Scoreboard v" + getPluginMeta().getVersion() + " enabled.");
  }

  @Override
  public void onDisable() {
    if (scoreboardService != null) {
      scoreboardService.stop();
    }
    PlaceholderBridge.unregisterExpansion();
    getLogger().info("Scoreboard has been disabled.");
  }

  public void reloadPlugin() {
    scoreboardService.reload();
  }

  public ScoreboardConfig getScoreboardConfig() {
    return scoreboardConfig;
  }

  public ScoreboardService getScoreboardService() {
    return scoreboardService;
  }
}
