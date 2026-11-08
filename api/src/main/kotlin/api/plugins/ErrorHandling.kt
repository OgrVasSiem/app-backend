package api.plugins

import api.dto.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.UnsupportedMediaTypeException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory
import java.util.concurrent.CancellationException

private val errorLogger = LoggerFactory.getLogger("HttpErrors")

fun Application.configureErrorHandling() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    code = "NOT_FOUND",
                    message = "Resource not found"
                )
            )
        }

        exception<Exception> { call, cause ->
            when (cause) {
                is CancellationException -> throw cause

                is BadRequestException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            code = "BAD_REQUEST",
                            message = "Invalid request"
                        )
                    )
                }

                is NotFoundException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(
                            code = "NOT_FOUND",
                            message = "Resource not found"
                        )
                    )
                }

                is UnsupportedMediaTypeException -> {
                    call.respond(
                        HttpStatusCode.UnsupportedMediaType,
                        ErrorResponse(
                            code = "UNSUPPORTED_MEDIA_TYPE",
                            message = "Unsupported request content type"
                        )
                    )
                }

                else -> {
                    errorLogger.error("Unhandled request exception", cause)

                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse(
                            code = "INTERNAL_ERROR",
                            message = "Internal server error"
                        )
                    )
                }
            }
        }
    }
}