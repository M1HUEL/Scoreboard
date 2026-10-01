package com.itson.Scoreboard.expansion;

import com.itson.Scoreboard.ScoreboardPlugin;
import java.util.List;
import java.util.Locale;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ScoreboardExpansion extends PlaceholderExpansion {

  private static final String LINE_PREFIX = "line_";

  private final ScoreboardPlugin plugin;

  public ScoreboardExpansion(ScoreboardPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NotNull
  String getIdentifier() {
    return "scoreboard";
  }

  @Override
  public @NotNull
  String getAuthor() {
    return "M1HUEL";
  }

  @Override
  public @NotNull
  String getVersion() {
    return plugin.getPluginMeta().getVersion();
  }

  @Override
  public @Nullable
  String onRequest(OfflinePlayer player, @NotNull String params) {
    return switch (params.toLowerCase(Locale.ROOT)) {
      case "title" ->
        plugin.getScoreboardConfig().getTitle();
      case "line_count" ->
        String.valueOf(plugin.getScoreboardConfig().getLines().size());
      case "update_interval" ->
        String.valueOf(plugin.getScoreboardConfig().getUpdateIntervalTicks());
      default ->
        line(params);
    };
  }

  private String line(String params) {
    String lowerCase = params.toLowerCase(Locale.ROOT);
    if (!lowerCase.startsWith(LINE_PREFIX)) {
      return null;
    }
    int index;
    try {
      index = Integer.parseInt(lowerCase.substring(LINE_PREFIX.length())) - 1;
    } catch (NumberFormatException exception) {
      return null;
    }
    List<String> lines = plugin.getScoreboardConfig().getLines();
    if (index < 0 || index >= lines.size()) {
      return "";
    }
    return lines.get(index);
  }
}
