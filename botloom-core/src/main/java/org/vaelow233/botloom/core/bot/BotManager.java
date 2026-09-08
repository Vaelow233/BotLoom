package org.vaelow233.botloom.core.bot;

import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botweave.core.BotWeave;

import java.util.concurrent.CompletionStage;

public interface BotManager {
    CompletionStage<Void> load(BotLoomConfig.BotConfig config);
    void unload();
    BotWeave botWeave();
}
