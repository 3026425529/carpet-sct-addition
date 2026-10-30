package top.sctserver.sct;

import carpet.api.settings.Rule;

public class SCTSettings {
    public static final String SCT = "SCT";

    @Rule(categories = SCT, options = {"false", "true"})
    public static boolean migrateScores = true;

    @Rule(categories = SCT, options = {"false", "true"})
    public static boolean migrateScoresCommand = false;

    @Rule(categories = SCT, options = {"false", "true"})
    public static boolean sendAllRecipes = false;
}
