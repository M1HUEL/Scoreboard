package com.itson.Scoreboard.command;

import com.itson.Scoreboard.ScoreboardPlugin;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public final class ScoreboardCommand implements CommandExecutor, TabCompleter {

  private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
  private static final List<String> SUBCOMMANDS = List.of("reload");

  private final ScoreboardPlugin plugin;

  public ScoreboardCommand(ScoreboardPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
      sender.sendMessage(MINI_MESSAGE.deserialize("<gray>Usage: /scoreboard reload"));
      return true;
    }
    if (!sender.hasPermission("scoreboard.reload")) {
      sender.sendMessage(MINI_MESSAGE.deserialize("<red>You do not have permission."));
      return true;
    }

    plugin.reloadPlugin();
    sender.sendMessage(MINI_MESSAGE.deserialize("<green>Scoreboard configuration reloaded."));
    return true;
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String alias, @NotNull String[] args) {
    if (args.length == 1) {
      return SUBCOMMANDS;
    }
    return Collections.emptyList();
  }
}
