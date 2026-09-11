# BotLoom

> A modular bot integration framework for Minecraft servers, powered by BotWeave

![BotLoom](icon.png "BotLoom")

**BotLoom** is a modular and extensible framework for integrating bots with Minecraft servers. It is powered by BotWeave for bot connectivity and provides common infrastructure for extensions, commands, configuration and persistent storage.

BotLoom currently provides an implementation for Paper and is under active development.

## Features

- **Bot integration**: Uses BotWeave to connect Minecraft servers with external bot platforms and protocols.
- **Extension system**: Features can be implemented and loaded independently as BotLoom extensions.
- **Persistent storage**: Provides a common storage interface with SQLite, MySQL and PostgreSQL support.
- **Platform abstraction**: Common functionality is implemented in `botloom-core`, while Minecraft platform support is provided by separate modules such as `botloom-paper`.
- **Configurable**: Bot connections, storage and extensions can be configured without modifying the core.

## Usage

### Requirements

The following table shows the requirement of all supported platforms:

| Platform | Minimum Java Version | Minimum Minecraft Version | 
|----------|----------------------|---------------------------|
| Paper    | 17                   | 1.20.4                    |

### Installation

To install BotLoom, first build the plugin JAR from source, then follow these steps:

1. Place the generated plugin JAR in the server's plugins directory (`plugins/`).
2. Start the server.
3. Run the `/plugins` command to verify that BotLoom is enabled.

The configuration file is located at plugins/BotLoom/config.yml. You can edit it as needed.

## Build

BotLoom uses Gradle to build the project:

```shell
./gradlew clean build
```

The deployable Paper plugin is built using Shadow:

```shell
./gradlew :botloom-paper:shadowJar
```

## License

BotLoom is released under the [LGPL-3.0 license](LICENSE).