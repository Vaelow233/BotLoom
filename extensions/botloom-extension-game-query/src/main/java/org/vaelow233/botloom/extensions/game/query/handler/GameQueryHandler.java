package org.vaelow233.botloom.extensions.game.query.handler;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import org.vaelow233.botloom.extensions.game.query.GameQueryExtension;
import org.vaelow233.botweave.api.bot.Bot;
import org.vaelow233.botweave.api.capability.Messaging;
import org.vaelow233.botweave.api.conversation.ConversationKind;
import org.vaelow233.botweave.api.conversation.ConversationRef;
import org.vaelow233.botweave.api.event.MessageReceivedEvent;
import org.vaelow233.botweave.api.message.MessageContent;
import org.vaelow233.botweave.api.message.element.TextElement;

import java.util.Collections;

public class GameQueryHandler {
    private final GameQueryExtension extension;

    public GameQueryHandler(GameQueryExtension extension) {
        this.extension = extension;
    }

    private Spark sparkOrNull() {
        try {
            return SparkProvider.get();
        } catch (IllegalStateException e) {
            extension.logger().warn("spark is not available yet");
            return null;
        }
    }

    public void handle(MessageReceivedEvent event) {
        ConversationRef conversation = event.message().conversation();
        if (ConversationKind.GROUP.equals(conversation.kind())) {
            if (!extension.config().source.groups.contains(conversation.id().value())) {
                return;
            }
        } else if (ConversationKind.PRIVATE.equals(conversation.kind())) {
            if (!extension.config().source.users.contains(conversation.id().value())) {
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
        if (extension.config().tps.enable && extension.config().tps.commands.contains(message)) {
            sendTPS(event.bot(), conversation);
        }
        if (extension.config().mspt.enable && extension.config().mspt.commands.contains(message)) {
            sendMSPT(event.bot(), conversation);
        }
        if (extension.config().cpu.enable && extension.config().cpu.commands.contains(message)) {
            sendCPUUsage(event.bot(), conversation);
        }
    }

    public void sendTPS(Bot bot, ConversationRef conversation) {
        Spark spark = sparkOrNull();
        if (spark == null) {
            return;
        }
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        double tpsLast5Secs = tps == null ? -1 : tps.poll(StatisticWindow.TicksPerSecond.SECONDS_5);
        double tpsLast10Secs = tps == null ? -1 : tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
        double tpsLastMin = tps == null ? -1 : tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1);
        double tpsLast5Mins = tps == null ? -1 : tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5);
        double tpsLast15Mins = tps == null ? -1 : tps.poll(StatisticWindow.TicksPerSecond.MINUTES_15);
        String message = extension.config().tps.message
                .replace("%tps_last_5_secs%", String.format("%.2f", tpsLast5Secs))
                .replace("%tps_last_10_secs%", String.format("%.2f", tpsLast10Secs))
                .replace("%tps_last_min%", String.format("%.2f", tpsLastMin))
                .replace("%tps_last_5_mins%", String.format("%.2f", tpsLast5Mins))
                .replace("%tps_last_15_mins%", String.format("%.2f", tpsLast15Mins));
        bot.capability(Messaging.class).ifPresent(messaging -> {
            MessageContent content = new MessageContent(Collections.singletonList(new TextElement(message)));
            messaging.send(conversation, content).whenComplete((sent, error) -> {
                if (error != null) {
                    extension.logger().error("Failed to send message to {}", conversation.id(), error);
                }
            });
        });
    }

    public void sendMSPT(Bot bot, ConversationRef conversation) {
        Spark spark = sparkOrNull();
        if (spark == null) {
            return;
        }
        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = spark.mspt();
        DoubleAverageInfo msptLast10Secs = mspt == null ? null : mspt.poll(StatisticWindow.MillisPerTick.SECONDS_10);
        DoubleAverageInfo msptLastMin = mspt == null ? null : mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1);
        DoubleAverageInfo msptLast5Mins = mspt == null ? null : mspt.poll(StatisticWindow.MillisPerTick.MINUTES_5);
        double msptLast10SecsMin = msptLast10Secs == null ? -1 : msptLast10Secs.min();
        double msptLast10SecsMean = msptLast10Secs == null ? -1 : msptLast10Secs.mean();
        double msptLast10SecsMed = msptLast10Secs == null ? -1 : msptLast10Secs.median();
        double msptLast10SecsPercentile95th = msptLast10Secs == null ? -1 : msptLast10Secs.percentile95th();
        double msptLast10SecsMax = msptLast10Secs == null ? -1 : msptLast10Secs.max();
        double msptLastMinMin = msptLastMin == null ? -1 : msptLastMin.min();
        double msptLastMinMean = msptLastMin == null ? -1 : msptLastMin.mean();
        double msptLastMinMed = msptLastMin == null ? -1 : msptLastMin.median();
        double msptLastMinPercentile95th = msptLastMin == null ? -1 : msptLastMin.percentile95th();
        double msptLastMinMax = msptLastMin == null ? -1 : msptLastMin.max();
        double msptLast5MinsMin = msptLast5Mins == null ? -1 : msptLast5Mins.min();
        double msptLast5MinsMean = msptLast5Mins == null ? -1 : msptLast5Mins.mean();
        double msptLast5MinsMed = msptLast5Mins == null ? -1 : msptLast5Mins.median();
        double msptLast5MinsPercentile95th = msptLast5Mins == null ? -1 : msptLast5Mins.percentile95th();
        double msptLast5MinsMax = msptLast5Mins == null ? -1 : msptLast5Mins.max();
        String message = extension.config().mspt.message
                .replace("%mspt_last_10_secs_min%", String.format("%.2f", msptLast10SecsMin))
                .replace("%mspt_last_10_secs_mean%", String.format("%.2f", msptLast10SecsMean))
                .replace("%mspt_last_10_secs_med%", String.format("%.2f", msptLast10SecsMed))
                .replace("%mspt_last_10_secs_95ile%", String.format("%.2f", msptLast10SecsPercentile95th))
                .replace("%mspt_last_10_secs_max%", String.format("%.2f", msptLast10SecsMax))
                .replace("%mspt_last_min_min%", String.format("%.2f", msptLastMinMin))
                .replace("%mspt_last_min_mean%", String.format("%.2f", msptLastMinMean))
                .replace("%mspt_last_min_med%", String.format("%.2f", msptLastMinMed))
                .replace("%mspt_last_min_95ile%", String.format("%.2f", msptLastMinPercentile95th))
                .replace("%mspt_last_min_max%", String.format("%.2f", msptLastMinMax))
                .replace("%mspt_last_5_mins_min%", String.format("%.2f", msptLast5MinsMin))
                .replace("%mspt_last_5_mins_mean%", String.format("%.2f", msptLast5MinsMean))
                .replace("%mspt_last_5_mins_med%", String.format("%.2f", msptLast5MinsMed))
                .replace("%mspt_last_5_mins_95ile%", String.format("%.2f", msptLast5MinsPercentile95th))
                .replace("%mspt_last_5_mins_max%", String.format("%.2f", msptLast5MinsMax));
        bot.capability(Messaging.class).ifPresent(messaging -> {
            MessageContent content = new MessageContent(Collections.singletonList(new TextElement(message)));
            messaging.send(conversation, content).whenComplete((sent, error) -> {
                if (error != null) {
                    extension.logger().error("Failed to send message to {}", conversation.id(), error);
                }
            });
        });
    }

    public void sendCPUUsage(Bot bot, ConversationRef conversation) {
        Spark spark = sparkOrNull();
        if (spark == null) {
            return;
        }
        DoubleStatistic<StatisticWindow.CpuUsage> cpuUsage = spark.cpuProcess();
        double usageLast10Secs = cpuUsage.poll(StatisticWindow.CpuUsage.SECONDS_10);
        double usageLastMin = cpuUsage.poll(StatisticWindow.CpuUsage.MINUTES_1);
        double usageLast15Mins = cpuUsage.poll(StatisticWindow.CpuUsage.MINUTES_15);
        String message = extension.config().cpu.message
                .replace("%cpu_last_10_secs%", String.format("%.2f%%", usageLast10Secs * 100))
                .replace("%cpu_last_min%", String.format("%.2f%%", usageLastMin * 100))
                .replace("%cpu_last_15_mins%", String.format("%.2f%%", usageLast15Mins * 100));
        bot.capability(Messaging.class).ifPresent(messaging -> {
            MessageContent content = new MessageContent(Collections.singletonList(new TextElement(message)));
            messaging.send(conversation, content).whenComplete((sent, error) -> {
                if (error != null) {
                    extension.logger().error("Failed to send message to {}", conversation.id(), error);
                }
            });
        });
    }
}
