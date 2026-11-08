package api.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    checkDatabase: suspend () -> Boolean,
) {
    routing {
        healthRoutes(checkDatabase)
    }
}