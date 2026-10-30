package top.sctserver.sct.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;

public final class MigrateScoresUtil {
    private MigrateScoresUtil() {
    }

    public static void migrateScores(MinecraftServer server, String source, String target) {
        Scoreboard scoreboard = server.getScoreboard();
        ScoreHolder sourceHolder = ScoreHolder.forNameOnly(source);
        ScoreHolder targetHolder = ScoreHolder.forNameOnly(target);

        Object2IntMap<Objective> scores = scoreboard.listPlayerScores(sourceHolder);
        scores.forEach((objective, value) -> {
            ReadOnlyScoreInfo oldScore = scoreboard.getPlayerScoreInfo(sourceHolder, objective);
            ScoreAccess targetScore = scoreboard.getOrCreatePlayerScore(targetHolder, objective, true);
            if (oldScore instanceof Score oldScoreValue) {
                targetScore.display(oldScoreValue.display());
                targetScore.numberFormatOverride(oldScore.numberFormat());
            }
            targetScore.set(value);
        });

        scoreboard.resetAllPlayerScores(sourceHolder);
    }
}
