package com.itson.Scoreboard.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public final class Placeholders {

  private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

  private final boolean external;

  public Placeholders(boolean external) {
    this.external = external;
  }

  public boolean hasExternalPlaceholders() {
    return external;
  }

  public Component render(String rawLine, Player player) {
    return MINI_MESSAGE.deserialize(apply(rawLine, player));
  }

  private String apply(String rawLine, Player player) {
    String result = expandExternally(rawLine, player);

    result = set(result, "player_name", player.getName());
    result = set(result, "uuid", player.getUniqueId().toString());
    result = set(result, "world", player.getWorld().getName());
    result = set(result, "kills", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
    result = set(result, "deaths", String.valueOf(player.getStatistic(Statistic.DEATHS)));
    result = set(result, "ping", String.valueOf(player.getPing()));
    result = set(result, "online", String.valueOf(Bukkit.getOnlinePlayers().size()));
    result = set(result, "health", String.format("%.1f", player.getHealth()));
    result = set(result, "food", String.valueOf(player.getFoodLevel()));

    return result;
  }

  private String expandExternally(String input, Player player) {
    if (!external) {
      return input;
    }
    try {
      return PlaceholderBridge.apply(player, input);
    } catch (Throwable throwable) {
      Bukkit.getLogger().warning("PlaceholderAPI threw while expanding a line: " + throwable);
      return input;
    }
  }

  private static String set(String input, String key, String value) {
    return input.replace("%" + key + "%", value);
  }
}
