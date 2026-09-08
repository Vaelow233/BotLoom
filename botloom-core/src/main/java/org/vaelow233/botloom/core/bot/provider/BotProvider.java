package org.vaelow233.botloom.core.bot.provider;

import java.util.concurrent.CompletionStage;

public interface BotProvider {
    CompletionStage<Void> load();
}
