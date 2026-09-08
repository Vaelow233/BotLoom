package org.vaelow233.botloom.core.bot.provider;

import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botweave.connector.qq.ob11.config.OneBotConfig;
import org.vaelow233.botweave.connector.qq.ob11.connector.OneBotConnectorFactory;
import org.vaelow233.botweave.core.BotWeave;

import java.util.concurrent.CompletionStage;

public class QQOB11Provider implements BotProvider {
    private final BotWeave botWeave;
    private final BotLoomConfig.BotConfig.QQOB11Config config;

    public QQOB11Provider(BotWeave botWeave, BotLoomConfig.BotConfig.QQOB11Config config) {
        this.botWeave = botWeave;
        this.config = config;
    }

    @Override
    public CompletionStage<Void> load() {
        OneBotConfig botConfig = new OneBotConfig(config.scheme, config.host, config.port, config.token);
        return botWeave.connectors().start("botloom-qq-ob11", new OneBotConnectorFactory(), botConfig);
    }
}
