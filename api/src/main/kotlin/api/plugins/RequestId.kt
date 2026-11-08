package api.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import java.util.UUID

fun Application.configureRequestId() {
    install(CallId) {
        generate {
            UUID.randomUUID().toString()
        }

        replyToHeader("X-Request-Id")
    }
}