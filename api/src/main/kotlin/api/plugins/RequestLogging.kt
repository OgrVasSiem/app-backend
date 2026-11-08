package api.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.calllogging.processingTimeMillis
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import io.ktor.server.plugins.callid.callIdMdc

fun Application.configureRequestLogging() {
    install(CallLogging) {
        level = Level.INFO
        logger = LoggerFactory.getLogger("HttpRequests")

        callIdMdc("requestId")

        format { call ->
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val status = call.response.status()?.value ?: "unknown"
            val duration = call.processingTimeMillis()

            "$method $path status=$status duration=${duration}ms"
        }
    }
}