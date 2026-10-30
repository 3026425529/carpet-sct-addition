package top.sctserver.sct.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.server.MinecraftServer;

public final class MigrateScoresUtil {
    private MigrateScoresUtil() {
    }

    public static void migrateScores(MinecraftServer server, String source, String target) {
        Scoreboard scoreboard = server.getScoreboard();
        ScoreHolder sourceHolder = ScoreHolder.fromName(source);
        ScoreHolder targetHolder = ScoreHolder.fromName(target);

        Object2IntMap<ScoreboardObjective> scores = scoreboard.getScoreHolderObjectives(sourceHolder);
        scores.forEach((objective, value) -> {
            ReadableScoreboardScore oldScore = scoreboard.getScore(sourceHolder, objective);
            ScoreAccess targetScore = scoreboard.getOrCreateScore(targetHolder, objective, true);
            if (oldScore instanceof ScoreboardScore oldScoreValue) {
                targetScore.setDisplayText(oldScoreValue.getDisplayText());
            }
            targetScore.setNumberFormat(oldScore.getNumberFormat());
            targetScore.setScore(value);
        });

        scoreboard.removeScores(sourceHolder);
    }
}
