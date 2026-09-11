package org.vaelow233.botloom.extensions.message.forwarding;

import org.pf4j.Extension;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.config.DefaultConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.BotLoomExtension;
import org.vaelow233.botloom.core.game.event.ChatEvent;
import org.vaelow233.botloom.extensions.message.forwarding.config.MessageForwardingConfig;
import org.vaelow233.botweave.api.capability.Messaging;
import org.vaelow233.botweave.api.conversation.ConversationId;
import org.vaelow233.botweave.api.conversation.ConversationKind;
import org.vaelow233.botweave.api.conversation.ConversationRef;
import org.vaelow233.botweave.api.event.MessageReceivedEvent;
import org.vaelow233.botweave.api.eventbus.Subscription;
import org.vaelow233.botweave.api.message.MessageContent;
import org.vaelow233.botweave.api.message.element.TextElement;
import org.vaelow233.botweave.connector.qq.ob11.impl.OneBotConversation;
import org.vaelow233.botweave.core.BotWeave;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@Extension
public class MessageForwardingExtension implements BotLoomExtension {

    private MessageForwardingConfig config;
    private Subscription subscription;
    private org.vaelow233.botloom.core.game.Subscription chatSubscription;

    @Override
    public void enable(BotLoomContext context) {
        try {
            Path configDir = context.dataDirectory().resolve("extensions").resolve("message-forwarding");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Path configFile = configDir.resolve("config.yml");
            ConfigProvider<MessageForwardingConfig> provider = new DefaultConfigProvider<>(configFile,
                    MessageForwardingConfig.class, MessageForwardingExtension.class);
            context.registerConfig("message-forwarding:config.yml", provider);
            this.config = provider.config();
            this.subscription = context.botWeave().events().subscribe(MessageReceivedEvent.class, event -> {
                if (ConversationKind.GROUP.equals(event.message().conversation().kind())) {
                    if (!config.server.group.enable) {
                        return;
                    }
                    if (!config.server.group.limit.source.contains(event.message().conversation().id().value())) {
                        return;
                    }
                    StringBuilder content = new StringBuilder();
                    event.message().content().elements().forEach((e) -> content.append(e.plainString()));
                    if (content.length() > config.server.group.limit.length) {
                        content.setLength(config.server.group.limit.length);
                        content.append(config.server.group.limit.replacement);
                    }
                    String finalContent = config.server.group.content
                            .replace("%group_id%", event.message().conversation().id().value())
                            .replace("%sender_id%", event.message().sender().id().value())
                            .replace("%message%", content);
                    context.runSync(() -> context.broadcast(finalContent));
                } else if (ConversationKind.PRIVATE.equals(event.message().conversation().kind())) {
                    if (!config.server.direct.enable) {
                        return;
                    }
                    if (!config.server.direct.limit.source.contains(event.message().conversation().id().value())) {
                        return;
                    }
                    StringBuilder content = new StringBuilder();
                    event.message().content().elements().forEach((e) -> content.append(e.plainString()));
                    if (content.length() > config.server.direct.limit.length) {
                        content.setLength(config.server.direct.limit.length);
                        content.append(config.server.direct.limit.replacement);
                    }
                    String finalContent = config.server.direct.content
                            .replace("%sender_id%", event.message().sender().id().value())
                            .replace("%message%", content);
                    context.runSync(() -> context.broadcast(finalContent));
                }
            });
            chatSubscription = context.addListener(ChatEvent.class, event -> {
                if (!config.bot.enable) {
                    return;
                }
                StringBuilder content = new StringBuilder(event.message());
                if (content.length() > config.bot.limit.length) {
                    content.setLength(config.bot.limit.length);
                    content.append(config.bot.limit.replacement);
                }
                String finalContent = config.bot.content
                        .replace("%player%", event.player().name())
                        .replace("%message%", content);
                for (String destination : config.bot.destination) {
                    ConversationRef conversation = new OneBotConversation(new ConversationId(destination), ConversationKind.GROUP);
                    context.botWeave().bots().all().forEach(bot -> bot.capability(Messaging.class).ifPresent(messaging -> {
                        messaging.send(conversation, new MessageContent(Collections.singletonList(new TextElement(finalContent))))
                                .whenComplete((sent, error) -> {
                                    if (error != null) {
                                        context.logger().error("Failed to forward message to {}", destination, error);
                                    }
                                });
                    }));
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize message-forwarding extension", e);
        }
    }

    @Override
    public void disable() {
        if (subscription != null) {
            subscription.close();
            subscription = null;
        }
        if (chatSubscription != null) {
            chatSubscription.close();
            chatSubscription = null;
        }
    }

    @Override
    public String name() {
        return "message-forwarding";
    }

    @Override
    public String version() {
        return "1.0.0";
    }
}
