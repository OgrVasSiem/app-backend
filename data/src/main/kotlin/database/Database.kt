package database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import database.migration.DatabaseMigrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.sql.SQLException

class Database(
    config: DatabaseConfig
) : Closeable {
    private val logger = LoggerFactory.getLogger(Database::class.java)

    private val pool = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = config.url
            username = config.user
            password = config.password

            maximumPoolSize = config.maximumPoolSize
            minimumIdle = config.minimumIdle
            connectionTimeout = 5_000
            validationTimeout = 2_000

            poolName = "DatabasePool"
        }
    )

    fun migrate() {
        DatabaseMigrator(pool).migrate()
    }

    fun checkConnection() {
        pool.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.queryTimeout = 5

                statement.executeQuery("SELECT 1").use { result ->
                    check(result.next() && result.getInt(1) == 1)
                    logger.debug("PostgreSQL connection OK")
                }
            }
        }
    }

    suspend fun isAvailable(): Boolean = withContext(Dispatchers.IO) {
        try {
            checkConnection()
            true
        } catch (exception: SQLException) {
            logger.warn("Database health check failed", exception)
            false
        }
    }

    override fun close() {
        pool.close()
    }
}