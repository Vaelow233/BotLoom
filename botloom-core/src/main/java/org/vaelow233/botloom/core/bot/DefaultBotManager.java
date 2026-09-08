package org.vaelow233.botloom.core.bot;

import org.vaelow233.botloom.core.bot.provider.BotProvider;
import org.vaelow233.botloom.core.bot.provider.QQOB11Provider;
import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botweave.core.BotWeave;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

public class DefaultBotManager implements BotManager {
    private final BotWeave botWeave = new BotWeave();
    private final Map<String, BotProvider> botProviderMap = new HashMap<>();

    @Override
    public CompletionStage<Void> load(BotLoomConfig.BotConfig config) {
        Map<String, BotProvider> prepared = new LinkedHashMap<>();
        for (String rawType : config.types) {
            String type = rawType.toLowerCase();
            if (prepared.containsKey(type)) {
                throw new IllegalArgumentException("Duplicate bot type: " + type);
            }
            if (!"qq-ob11".equals(type)) {
                throw new IllegalArgumentException("Invalid bot type: " + type);
            }
            prepared.put(type, new QQOB11Provider(botWeave, config.qqOb11));
        }
        botProviderMap.putAll(prepared);
        CompletableFuture<?>[] starts = prepared.values().stream()
                .map(provider -> CompletableFuture.<Void>completedFuture(null)
                        .thenCompose(unused -> provider.load())
                        .toCompletableFuture())
                .toArray(CompletableFuture<?>[]::new);

        return CompletableFuture.allOf(starts);
    }

    @Override
    public void unload() {
        try {
            botWeave.stop().toCompletableFuture().get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while stopping bots", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new IllegalStateException("Failed to stop bots", e);
        }
    }

    @Override
    public BotWeave botWeave() {
        return botWeave;
    }

    public Map<String, BotProvider> botProviderMap() {
        return botProviderMap;
    }
}
