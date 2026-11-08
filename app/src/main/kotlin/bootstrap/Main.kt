package bootstrap

import api.plugins.configureErrorHandling
import api.plugins.configureRequestLogging
import api.plugins.configureSerialization
import api.routes.configureRouting
import config.AppConfig
import database.Database
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory
import api.plugins.configureRequestId

private val logger = LoggerFactory.getLogger("Application")

fun main() {
    val config = AppConfig.fromEnvironment()
    val database = Database(config.database)

    try {
        database.checkConnection()
        database.migrate()

        val server = embeddedServer(
            factory = Netty,
            host = config.server.host,
            port = config.server.port
        ) {
            configureRequestLogging()
            configureSerialization()
            configureRouting(
                checkDatabase = database::isAvailable,
            )
            configureRequestId()
            configureErrorHandling()
        }

        server.monitor.subscribe(ApplicationStarted) {
            logger.info(
                "Server started on {}:{}",
                config.server.host,
                config.server.port
            )
        }

        server.monitor.subscribe(ApplicationStopped) {
            database.close()
            logger.info("Server stopped. Database pool closed.")
        }

        server.addShutdownHook {
            server.stop(
                gracePeriodMillis = 1_000,
                timeoutMillis = 5_000
            )
        }

        server.start(wait = true)
    } catch (error: Throwable) {
        database.close()
        throw error
    }
}
