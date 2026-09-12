package org.vaelow233.botloom.extensions.game.notifications;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.config.DefaultConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.BotLoomExtension;
import org.vaelow233.botloom.core.game.Subscription;
import org.vaelow233.botloom.core.game.event.JoinEvent;
import org.vaelow233.botloom.core.game.event.QuitEvent;
import org.vaelow233.botloom.core.game.event.ServerStartedEvent;
import org.vaelow233.botloom.core.game.event.ServerStoppingEvent;
import org.vaelow233.botloom.extensions.game.notifications.config.GameNotificationsConfig;
import org.vaelow233.botloom.extensions.game.notifications.event.GameEvents;
import org.vaelow233.botweave.core.BotWeave;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Extension
public class GameNotificationsExtension implements BotLoomExtension {
    private GameNotificationsConfig config;
    private GameEvents events;
    private final List<Subscription> subscriptions = new ArrayList<>();
    private BotWeave botWeave;
    private Logger logger;

    @Override
    public void enable(BotLoomContext context) {
        try {
            Path configDir = context.dataDirectory().resolve("extensions").resolve("game-notifications");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Path configFile = configDir.resolve("config.yml");
            ConfigProvider<GameNotificationsConfig> provider = new DefaultConfigProvider<>(configFile,
                    GameNotificationsConfig.class, GameNotificationsExtension.class);
            context.registerConfig("game-notifications:config.yml", provider);
            this.config = provider.config();
            this.botWeave = context.botWeave();
            this.logger = context.logger();
            this.events = new GameEvents(this, context.botsReady());
            subscriptions.add(context.addListener(ServerStartedEvent.class, events::onServerStarted));
            subscriptions.add(context.addListener(ServerStoppingEvent.class, events::onServerStopping));
            subscriptions.add(context.addListener(JoinEvent.class, events::onJoin));
            subscriptions.add(context.addListener(QuitEvent.class, events::onQuit));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize game notification extension", e);
        }
    }

    @Override
    public void disable() {
        if (events != null) {
            events.close();
        }
        subscriptions.forEach(s -> {
            if (s != null) {
                s.close();
            }
        });
    }

    @Override
    public String name() {
        return "game-notifications";
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    public GameNotificationsConfig config() {
        return config;
    }

    public BotWeave botWeave() {
        return botWeave;
    }

    public Logger logger() {
        return logger;
    }
}
