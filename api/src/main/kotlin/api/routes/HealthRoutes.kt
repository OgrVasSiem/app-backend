package api.routes

import api.dto.ErrorResponse
import api.dto.HealthResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.healthRoutes(
    checkDatabase: suspend () -> Boolean,
) {
    get("/health") {
        call.respond(
            HttpStatusCode.OK,
            HealthResponse(status = "ok"),
        )
    }

    get("/health/db") {
        if (checkDatabase()) {
            call.respond(
                HttpStatusCode.OK,
                HealthResponse(status = "ok"),
            )
        } else {
            call.respond(
                HttpStatusCode.ServiceUnavailable,
                ErrorResponse(
                    code = "DATABASE_UNAVAILABLE",
                    message = "Database is unavailable",
                ),
            )
        }
    }
}