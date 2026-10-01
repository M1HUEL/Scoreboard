package com.itson.Scoreboard.scoreboard;

import com.itson.Scoreboard.ScoreboardPlugin;
import com.itson.Scoreboard.util.Placeholders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class ScoreboardService {

  private final ScoreboardPlugin plugin;
  private final Map<UUID, PlayerBoard> boards = new HashMap<>();
  private final Placeholders placeholders;

  private BukkitTask updateTask;

  public ScoreboardService(ScoreboardPlugin plugin, Placeholders placeholders) {
    this.plugin = plugin;
    this.placeholders = placeholders;
  }

  public void start() {
    stop();

    if (!plugin.getScoreboardConfig().isEnabled()) {
      plugin.getLogger().info("Scoreboard is disabled in config.yml.");
      return;
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      create(player);
    }

    long period = plugin.getScoreboardConfig().getUpdateIntervalTicks();
    updateTask = Bukkit.getScheduler().runTaskTimer(plugin, this::updateAll, period, period);
  }

  public void stop() {
    if (updateTask != null) {
      updateTask.cancel();
      updateTask = null;
    }
    for (Player player : Bukkit.getOnlinePlayers()) {
      remove(player);
    }
    boards.clear();
  }

  public void create(Player player) {
    if (!plugin.getScoreboardConfig().isEnabled()) {
      return;
    }

    remove(player);

    PlayerBoard board = PlayerBoard.attach(player,
      placeholders.render(plugin.getScoreboardConfig().getTitle(), player));
    boards.put(player.getUniqueId(), board);

    update(player);
  }

  public void remove(Player player) {
    PlayerBoard board = boards.remove(player.getUniqueId());
    if (board != null) {
      board.detach(player);
    }
  }

  public void update(Player player) {
    PlayerBoard board = boards.get(player.getUniqueId());
    if (board == null) {
      create(player);
      return;
    }
    board.update(placeholders.render(plugin.getScoreboardConfig().getTitle(), player), lines(player));
  }

  public void reload() {
    plugin.getScoreboardConfig().reload();
    start();
  }

  private void updateAll() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      update(player);
    }
  }

  private List<Component> lines(Player player) {
    List<String> rawLines = plugin.getScoreboardConfig().getLines();
    int size = Math.min(rawLines.size(), PlayerBoard.MAX_LINES);

    List<Component> rendered = new ArrayList<>(size);
    for (int index = 0; index < size; index++) {
      rendered.add(placeholders.render(rawLines.get(index), player));
    }
    return rendered;
  }
}
