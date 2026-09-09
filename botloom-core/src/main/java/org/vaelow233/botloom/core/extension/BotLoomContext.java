package org.vaelow233.botloom.core.extension;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.storage.StorageProvider;

public interface BotLoomContext {
    StorageProvider storage();
    Logger logger();
}
