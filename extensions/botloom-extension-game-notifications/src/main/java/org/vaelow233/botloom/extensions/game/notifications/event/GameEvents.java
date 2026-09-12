package org.vaelow233.botloom.extensions.game.notifications.event;

import org.vaelow233.botloom.core.game.event.JoinEvent;
import org.vaelow233.botloom.core.game.event.QuitEvent;
import org.vaelow233.botloom.core.game.event.ServerStartedEvent;
import org.vaelow233.botloom.core.game.event.ServerStoppingEvent;
import org.vaelow233.botloom.extensions.game.notifications.GameNotificationsExtension;
import org.vaelow233.botweave.api.capability.Messaging;
import org.vaelow233.botweave.api.conversation.ConversationId;
import org.vaelow233.botweave.api.conversation.ConversationKind;
import org.vaelow233.botweave.api.message.MessageContent;
import org.vaelow233.botweave.api.message.element.TextElement;
import org.vaelow233.botweave.connector.qq.ob11.impl.OneBotConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class GameEvents implements AutoCloseable {
    private final GameNotificationsExtension extension;
    private final CompletionStage<Void> botsReady;

    private boolean closed;
    private boolean stopping;

    public GameEvents(GameNotificationsExtension extension, CompletionStage<Void> botsReady) {
        this.extension = extension;
        this.botsReady = botsReady;
    }

    public synchronized void onServerStarted(ServerStartedEvent event) {
        if (closed || stopping || !extension.config().serverStarted.enable) {
            return;
        }
        botsReady.whenComplete((unused, startupError) -> {
            synchronized (GameEvents.this) {
                if (closed || stopping) {
                    return;
                }
                try {
                    sendMessage(extension.config().serverStarted.message);
                } catch (Exception | LinkageError e) {
                    extension.logger().warn("Failed to submit server started notification", e);
                }
            }
        });
    }

    public void onServerStopping(ServerStoppingEvent event) {
        synchronized (this) {
            stopping = true;
        }
        if (!extension.config().serverStopping.enable) {
            return;
        }
        try {
            sendMessage(extension.config().serverStopping.message)
                    .toCompletableFuture().get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            extension.logger().warn("Failed to send the server stopping message because the thread was interrupted!");
        } catch (ExecutionException | TimeoutException e) {
            extension.logger().warn("Failed to send the server stopping message", e);
        }
    }

    public void onJoin(JoinEvent event) {
        if (!extension.config().playerJoin.enable) {
            return;
        }
        sendMessage(extension.config().playerJoin.message
                .replace("%player%", event.player().name()));
    }

    public void onQuit(QuitEvent event) {
        if (!extension.config().playerQuit.enable) {
            return;
        }
        sendMessage(extension.config().playerQuit.message
                .replace("%player%", event.player().name()));
    }

    public CompletableFuture<?> sendMessage(String message) {
        List<CompletableFuture<?>> sends = new ArrayList<>();
        extension.botWeave().bots().all().forEach(bot -> {
            bot.capability(Messaging.class).ifPresent(messaging -> {
                MessageContent content = new MessageContent(Collections.singletonList(new TextElement(message)));
                for (String user : extension.config().destination.users) {
                    OneBotConversation conversation = new OneBotConversation(new ConversationId(user), ConversationKind.PRIVATE);
                    sends.add(messaging.send(conversation, content).toCompletableFuture());
                }
                for (String group : extension.config().destination.groups) {
                    OneBotConversation conversation = new OneBotConversation(new ConversationId(group), ConversationKind.GROUP);
                    sends.add(messaging.send(conversation, content).toCompletableFuture());
                }
            });
        });
        return CompletableFuture.allOf(sends.toArray(new CompletableFuture<?>[0])).whenComplete((unused, error) -> {
            if (error != null) {
                extension.logger().warn("Failed to send game notification", error);
            }
        });
    }

    @Override
    public synchronized void close() {
        closed = true;
    }
}
