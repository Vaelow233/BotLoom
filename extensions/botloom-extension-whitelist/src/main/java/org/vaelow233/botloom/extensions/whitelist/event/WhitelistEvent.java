package org.vaelow233.botloom.extensions.whitelist.event;

import org.vaelow233.botloom.core.game.event.LoginEvent;
import org.vaelow233.botloom.extensions.whitelist.WhitelistExtension;
import org.vaelow233.botloom.extensions.whitelist.handler.WhitelistHandler;
import org.vaelow233.botweave.api.capability.Messaging;
import org.vaelow233.botweave.api.conversation.ConversationKind;
import org.vaelow233.botweave.api.conversation.ConversationRef;
import org.vaelow233.botweave.api.event.MessageReceivedEvent;
import org.vaelow233.botweave.api.message.MessageContent;
import org.vaelow233.botweave.api.message.element.TextElement;

import java.util.Collections;

public class WhitelistEvent implements AutoCloseable {
    private final WhitelistExtension extension;
    private final WhitelistHandler handler;

    public WhitelistEvent(WhitelistExtension extension) {
        this.extension = extension;
        this.handler = new WhitelistHandler(extension);
    }

    @Override
    public void close() {
        handler.close();
    }

    public void onLogin(LoginEvent event) {
        if (!extension.config().enable || !extension.config().needBindToLogin) {
            return;
        }
        if (extension.config().bypass.enable) {
            if (event.player().hasPermission(extension.config().bypass.permission)) {
                return;
            }
            if (extension.config().bypass.names.contains(event.player().name())) {
                return;
            }
        }
        try {
            String result = handler.kick(event.player().name());
            if (result != null) {
                event.disallow(result);
            }
        } catch (Exception | LinkageError e) {
            event.disallow(extension.message().serverbound.error);
            extension.logger().error("Failed to check whitelist", e);
        }
    }

    public void onUserMessage(MessageReceivedEvent event) {
        if (!extension.config().enable) {
            return;
        }
        ConversationRef conversation = event.message().conversation();
        if (ConversationKind.PRIVATE.equals(conversation.kind())) {
            if (!extension.config().sources.users.contains("*") && !extension.config().sources.users.contains(event.message().sender().id().value())) {
                return;
            }
        } else if (ConversationKind.GROUP.equals(conversation.kind())) {
            if (!extension.config().sources.groups.contains(conversation.id().value())) {
                return;
            }
        } else {
            return;
        }
        MessageContent content = event.message().content();
        if (content.elements().size() != 1 || !(content.elements().get(0) instanceof TextElement)) {
            return;
        }
        String message = ((TextElement) content.elements().get(0)).text();
        if ("NAME".equalsIgnoreCase(extension.config().method)) {
            for (String prefix : extension.config().prefixes.bind) {
                if (message.startsWith(prefix)) {
                    String result = handler.bind(event.message().sender().id().value(), message.substring(prefix.length()));
                    event.bot().capability(Messaging.class).ifPresent(messaging -> {
                        messaging.send(conversation, new MessageContent(Collections.singletonList(new TextElement(result))))
                                .whenComplete((sent, error) -> {
                            if (error != null) {
                                extension.logger().error("Failed to forward message to {}", conversation.id(), error);
                            }
                        });
                    });
                    return;
                }
            }
        } else if ("VERIFY_CODE".equalsIgnoreCase(extension.config().method)) {
            for (String prefix : extension.config().prefixes.verify) {
                if (message.startsWith(prefix)) {
                    String result = handler.verify(event.message().sender().id().value(), message.substring(prefix.length()));
                    event.bot().capability(Messaging.class).ifPresent(messaging -> {
                        messaging.send(conversation, new MessageContent(Collections.singletonList(new TextElement(result))))
                                .whenComplete((sent, error) -> {
                            if (error != null) {
                                extension.logger().error("Failed to forward message to {}", conversation.id(), error);
                            }
                        });
                    });
                    return;
                }
            }
        }
        for (String prefix : extension.config().prefixes.unbind) {
            if (message.startsWith(prefix)) {
                String result = handler.unbind(event.message().sender().id().value(), message.substring(prefix.length()));
                event.bot().capability(Messaging.class).ifPresent(messaging -> {
                    messaging.send(conversation, new MessageContent(Collections.singletonList(new TextElement(result))))
                            .whenComplete((sent, error) -> {
                        if (error != null) {
                            extension.logger().error("Failed to forward message to {}", conversation.id(), error);
                        }
                    });
                });
                return;
            }
        }
    }
}
