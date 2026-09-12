package org.vaelow233.botloom.extensions.whitelist;

import org.jdbi.v3.core.Jdbi;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.config.DefaultConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.BotLoomExtension;
import org.vaelow233.botloom.core.game.Subscription;
import org.vaelow233.botloom.core.game.event.LoginEvent;
import org.vaelow233.botloom.extensions.whitelist.config.WhitelistConfig;
import org.vaelow233.botloom.extensions.whitelist.config.WhitelistMessageConfig;
import org.vaelow233.botloom.extensions.whitelist.event.WhitelistEvent;
import org.vaelow233.botweave.api.event.MessageReceivedEvent;

import java.nio.file.Files;
import java.nio.file.Path;

@Extension
public class WhitelistExtension implements BotLoomExtension {
    private WhitelistConfig config;
    private WhitelistMessageConfig messageConfig;
    private WhitelistEvent event;
    private Subscription joinSubscription;
    private org.vaelow233.botweave.api.eventbus.Subscription subscription;
    private Jdbi jdbi;
    private Logger logger;

    @Override
    public void enable(BotLoomContext context) {
        try {
            Path configDir = context.dataDirectory().resolve("extensions").resolve("whitelist");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Path configFile = configDir.resolve("config.yml");
            ConfigProvider<WhitelistConfig> provider = new DefaultConfigProvider<>(configFile,
                    WhitelistConfig.class, WhitelistExtension.class);
            context.registerConfig("whitelist:config.yml", provider);
            this.config = provider.config();
            Path messageConfig = configDir.resolve("messages.yml");
            ConfigProvider<WhitelistMessageConfig> messageProvider = new DefaultConfigProvider<>(messageConfig,
                    WhitelistMessageConfig.class, WhitelistExtension.class);
            context.registerConfig("whitelist:messages.yml", messageProvider);
            this.messageConfig = messageProvider.config();
            context.storage().migrate("whitelist", getClass().getClassLoader(),
                    "classpath:migration/" + context.storage().type().toString());
            this.jdbi = context.storage().jdbi();
            this.logger = context.logger();
            this.event = new WhitelistEvent(this);
            this.joinSubscription = context.addListener(LoginEvent.class, event::onLogin);
            this.subscription = context.botWeave().events().subscribe(MessageReceivedEvent.class, event::onUserMessage);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize whitelist extension", e);
        }
    }

    @Override
    public void disable() {
        if (subscription != null) {
            subscription.close();
            subscription = null;
        }
        if (joinSubscription != null) {
            joinSubscription.close();
            joinSubscription = null;
        }
        if (event != null) {
            event.close();
            event = null;
        }
    }

    @Override
    public String name() {
        return "whitelist";
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    public WhitelistConfig config() {
        return config;
    }

    public WhitelistMessageConfig message() {
        return messageConfig;
    }

    public Jdbi jdbi() {
        return jdbi;
    }

    public Logger logger() {
        return logger;
    }
}
