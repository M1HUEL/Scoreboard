package com.itson.Scoreboard.util;

import com.itson.Scoreboard.ScoreboardPlugin;
import org.bukkit.entity.Player;

public final class PlaceholderBridge {

  private static boolean enabled;

  private PlaceholderBridge() {
  }

  public static void init(boolean papiPresent) {
    enabled = papiPresent;
  }

  public static boolean isEnabled() {
    return enabled;
  }

  public static String apply(Player player, String text) {
    if (!enabled) {
      return text;
    }
    return Hook.apply(player, text);
  }

  public static void registerExpansion(ScoreboardPlugin plugin) {
    if (enabled) {
      Hook.register(plugin);
    }
  }

  public static void unregisterExpansion() {
    if (enabled) {
      Hook.unregister();
    }
  }

  private static final class Hook {

    private static com.itson.Scoreboard.expansion.ScoreboardExpansion expansion;

    private Hook() {
    }

    static String apply(Player player, String text) {
      return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
    }

    static void register(ScoreboardPlugin plugin) {
      expansion = new com.itson.Scoreboard.expansion.ScoreboardExpansion(plugin);
      me.clip.placeholderapi.PlaceholderAPI.registerExpansion(expansion);
    }

    static void unregister() {
      if (expansion != null) {
        me.clip.placeholderapi.PlaceholderAPI.unregisterExpansion(expansion);
        expansion = null;
      }
    }
  }
}
