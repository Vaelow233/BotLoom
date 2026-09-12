package org.vaelow233.botloom.extensions.whitelist.handler;

import org.vaelow233.botloom.extensions.whitelist.WhitelistExtension;
import org.vaelow233.botloom.extensions.whitelist.dao.WhitelistBindDao;
import org.vaelow233.botloom.extensions.whitelist.data.WhitelistBind;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WhitelistHandler implements AutoCloseable {
    private final WhitelistExtension extension;
    private final Map<String, String> codeMap = new ConcurrentHashMap<>();
    private final List<String> bindCooldowns = new CopyOnWriteArrayList<>();
    private final List<String> unbindCooldowns = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final SecureRandom RANDOM = new SecureRandom();

    public WhitelistHandler(WhitelistExtension extension) {
        this.extension = extension;
    }

    @Override
    public synchronized void close() {
        executor.shutdownNow();
        codeMap.clear();
        bindCooldowns.clear();
        unbindCooldowns.clear();
    }

    private String generateCode(int length, String characters) {
        if (length <= 0 || characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Invalid verification code config");
        }
        for (int attempt = 0; attempt < 32; attempt++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
            }
            String code = sb.toString();
            if (!codeMap.containsValue(code)) {
                return code;
            }
        }
        throw new IllegalStateException("Unable to allocate verification code");
    }

    public synchronized String bind(String userId, String name) {
        if (executor.isShutdown()) {
            return extension.message().botbound.shuttingDown;
        }
        WhitelistBindDao dao = extension.jdbi().onDemand(WhitelistBindDao.class);
        if (!dao.findByName(name).isEmpty()) {
            return extension.message().botbound.boundByOther;
        }
        if (dao.findByUserId(userId).size() >= extension.config().maxBindCount) {
            return extension.message().botbound.reachLimit
                    .replace("%limit%", String.valueOf(extension.config().maxBindCount));
        }
        if (bindCooldowns.contains(userId)) {
            return extension.message().botbound.inCooldown
                    .replace("%total%", "" + extension.config().cooldown.bind);
        }
        dao.insert(LocalDateTime.now(), userId, name);
        bindCooldowns.add(userId);
        executor.schedule(() -> bindCooldowns.remove(userId), extension.config().cooldown.bind, TimeUnit.SECONDS);
        return extension.message().botbound.operationSuccessful;
    }

    public synchronized String unbind(String userId, String name) {
        if (executor.isShutdown()) {
            return extension.message().botbound.shuttingDown;
        }
        WhitelistBindDao dao = extension.jdbi().onDemand(WhitelistBindDao.class);
        List<WhitelistBind> binds = dao.findByUserIdAndName(userId, name);
        if (binds.isEmpty()) {
            return extension.message().botbound.notBound;
        }
        if (unbindCooldowns.contains(userId)) {
            return extension.message().botbound.inCooldown
                    .replace("%total%", "" + extension.config().cooldown.unbind);
        }
        for (WhitelistBind bind : binds) {
            dao.delete(bind.id());
        }
        unbindCooldowns.add(userId);
        executor.schedule(() -> unbindCooldowns.remove(userId), extension.config().cooldown.unbind, TimeUnit.SECONDS);
        return extension.message().botbound.operationSuccessful;
    }

    public synchronized String kick(String name) {
        if (executor.isShutdown()) {
            return extension.message().serverbound.shuttingDown;
        }
        WhitelistBindDao dao = extension.jdbi().onDemand(WhitelistBindDao.class);
        if (!dao.findByName(name).isEmpty()) {
            return null;
        }
        if ("VERIFY_CODE".equalsIgnoreCase(extension.config().method)) {
            String code = generateCode(extension.config().code.length, extension.config().code.characters);
            codeMap.put(name, code);
            executor.schedule(() -> {
                synchronized (WhitelistHandler.this) {
                    codeMap.remove(name, code);
                }
            }, extension.config().code.expiresAt, TimeUnit.SECONDS);
            return extension.message().serverbound.unverify
                    .replace("%code%", code);
        } else {
            return extension.message().serverbound.unbind;
        }
    }

    public synchronized String verify(String userId, String code) {
        if (executor.isShutdown()) {
            return extension.message().botbound.shuttingDown;
        }
        String name = null;
        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            if (entry.getValue().equals(code)) {
                name = entry.getKey();
                break;
            }
        }
        if (name == null) {
            return extension.message().botbound.unknownCode;
        }
        synchronized (WhitelistHandler.this) {
            codeMap.remove(name);
        }
        WhitelistBindDao dao = extension.jdbi().onDemand(WhitelistBindDao.class);
        if (!dao.findByName(name).isEmpty()) {
            return extension.message().botbound.boundByOther;
        }
        if (dao.findByUserId(userId).size() >= extension.config().maxBindCount) {
            return extension.message().botbound.reachLimit
                    .replace("%limit%", String.valueOf(extension.config().maxBindCount));
        }
        if (bindCooldowns.contains(userId)) {
            return extension.message().botbound.inCooldown
                    .replace("%total%", "" + extension.config().cooldown.bind);
        }
        dao.insert(LocalDateTime.now(), userId, name);
        bindCooldowns.add(userId);
        executor.schedule(() -> bindCooldowns.remove(userId), extension.config().cooldown.bind, TimeUnit.SECONDS);
        return extension.message().botbound.operationSuccessful;
    }
}
