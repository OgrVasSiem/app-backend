package config

import database.DatabaseConfig

data class ServerConfig(
    val host: String,
    val port: Int
) {
    init {
        require(host.isNotBlank()) {
            "SERVER_HOST must not be blank"
        }
        require(port in 1..65535) {
            "SERVER_PORT must be between 1 and 65535"
        }
    }
}

class AppConfig(
    val server: ServerConfig,
    val database: DatabaseConfig
) {
    companion object {
        fun fromEnvironment(): AppConfig {
            return AppConfig(
                server = ServerConfig(
                    host = System.getenv("SERVER_HOST") ?: "127.0.0.1",
                    port = intEnvironment("SERVER_PORT", default = 8080)
                ),
                database = DatabaseConfig(
                    url = requiredEnvironment("DB_URL"),
                    user = requiredEnvironment("DB_USER"),
                    password = requiredEnvironment("DB_PASSWORD"),
                    maximumPoolSize = intEnvironment(
                        "DB_POOL_SIZE",
                        default = 5
                    ),
                    minimumIdle = intEnvironment(
                        "DB_MIN_IDLE",
                        default = 1
                    )
                )
            )
        }
    }
}

private fun requiredEnvironment(name: String): String {
    return System.getenv(name)
        ?.takeIf { it.isNotBlank() }
        ?: error("$name is not set or is blank")
}

private fun intEnvironment(name: String, default: Int): Int {
    val value = System.getenv(name) ?: return default

    return value.toIntOrNull()
        ?: error("$name must be an integer")
}