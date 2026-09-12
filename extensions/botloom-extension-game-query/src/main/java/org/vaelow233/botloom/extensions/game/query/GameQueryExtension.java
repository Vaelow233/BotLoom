package org.vaelow233.botloom.extensions.game.query;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.config.DefaultConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.BotLoomExtension;
import org.vaelow233.botloom.extensions.game.query.config.GameQueryConfig;
import org.vaelow233.botloom.extensions.game.query.handler.GameQueryHandler;
import org.vaelow233.botweave.api.event.MessageReceivedEvent;
import org.vaelow233.botweave.api.eventbus.Subscription;
import org.vaelow233.botweave.core.BotWeave;

import java.nio.file.Files;
import java.nio.file.Path;

@Extension
public class GameQueryExtension implements BotLoomExtension {
    private GameQueryConfig config;
    private BotWeave botWeave;
    private GameQueryHandler handler;
    private Subscription subscription;
    private Logger logger;

    @Override
    public void enable(BotLoomContext context) {
        try {
            Class.forName("me.lucko.spark.api.Spark");
        } catch (ClassNotFoundException e) {
            context.logger().warn("The game-query extension requires spark to run!");
            return;
        }
        try {
            Path configDir = context.dataDirectory().resolve("extensions").resolve("game-query");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Path configFile = configDir.resolve("config.yml");
            ConfigProvider<GameQueryConfig> provider = new DefaultConfigProvider<>(configFile,
                    GameQueryConfig.class, GameQueryExtension.class);
            context.registerConfig("game-query:config.yml", provider);
            this.config = provider.config();
            this.botWeave = context.botWeave();
            this.logger = context.logger();
            this.handler = new GameQueryHandler(this);
            this.subscription = botWeave.events().subscribe(MessageReceivedEvent.class, handler::handle);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize game-query extension", e);
        }
    }

    @Override
    public void disable() {
        if (subscription != null) {
            subscription
                    .close();
            subscription = null;
        }
    }

    @Override
    public String name() {
        return "game-query";
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    public GameQueryConfig config() {
        return config;
    }

    public BotWeave botWeave() {
        return botWeave;
    }

    public Logger logger() {
        return logger;
    }
}
