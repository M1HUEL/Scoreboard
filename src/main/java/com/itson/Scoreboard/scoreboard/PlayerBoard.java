package com.itson.Scoreboard.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public final class PlayerBoard {

  public static final int MAX_LINES = 15;

  private static final char[] ENTRY_ALPHABET = "0123456789abcdef".toCharArray();
  private static final String OBJECTIVE_NAME = "scoreboard";

  private final Scoreboard scoreboard;
  private final Objective objective;
  private final List<String> entries = new ArrayList<>(MAX_LINES);

  private Component title;
  private List<Component> lastLines = List.of();

  private PlayerBoard(Component title) {
    this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    this.objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY, title, RenderType.INTEGER);
    this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    this.objective.numberFormat(NumberFormat.blank());
    this.title = title;
  }

  public static PlayerBoard attach(Player player, Component title) {
    PlayerBoard board = new PlayerBoard(title);
    player.setScoreboard(board.scoreboard);
    return board;
  }

  public void update(Component newTitle, List<Component> lines) {
    if (Objects.equals(newTitle, title) && lines.equals(lastLines)) {
      return;
    }

    int size = lines.size();

    title = newTitle;
    objective.displayName(newTitle);

    while (entries.size() > size) {
      scoreboard.resetScores(entries.remove(entries.size() - 1));
    }

    for (int index = 0; index < size; index++) {
      String entry = index < entries.size() ? entries.get(index) : createEntry(index);
      Score score = objective.getScore(entry);
      score.setScore(size - 1 - index);
      score.customName(lines.get(index));
    }

    lastLines = List.copyOf(lines);
  }

  public void detach(Player player) {
    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
  }

  private String createEntry(int index) {
    String entry = invisibleEntry(index);
    entries.add(entry);
    return entry;
  }

  private static String invisibleEntry(int index) {
    StringBuilder builder = new StringBuilder(16);
    for (int digit = 0; digit < 8; digit++) {
      builder.append('§').append(ENTRY_ALPHABET[(index + digit) & 0xF]);
    }
    return builder.toString();
  }
}
