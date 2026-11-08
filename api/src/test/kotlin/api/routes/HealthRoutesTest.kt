package api.routes

import api.plugins.configureSerialization
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class HealthRoutesTest {

    @Test
    fun `health responds without checking database`() = testApplication {
        application {
            configureSerialization()
            configureRouting(
                checkDatabase = {
                    error("Health endpoint must not query the database")
                },
            )
        }

        val response = client.get("/health")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            Json.parseToJsonElement("""{"status":"ok"}"""),
            Json.parseToJsonElement(response.bodyAsText()),
        )
    }

    @Test
    fun `database health returns 200 when database is available`() =
        testApplication {
            application {
                configureSerialization()
                configureRouting(checkDatabase = { true })
            }

            val response = client.get("/health/db")

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
                Json.parseToJsonElement("""{"status":"ok"}"""),
                Json.parseToJsonElement(response.bodyAsText()),
            )
        }

    @Test
    fun `database health returns 503 when database is unavailable`() =
        testApplication {
            application {
                configureSerialization()
                configureRouting(checkDatabase = { false })
            }

            val response = client.get("/health/db")

            assertEquals(
                HttpStatusCode.ServiceUnavailable,
                response.status,
            )
            assertEquals(
                Json.parseToJsonElement(
                    """
                    {
                        "code": "DATABASE_UNAVAILABLE",
                        "message": "Database is unavailable"
                    }
                    """.trimIndent(),
                ),
                Json.parseToJsonElement(response.bodyAsText()),
            )
        }
}