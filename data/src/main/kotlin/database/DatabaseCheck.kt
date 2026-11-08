package database

import java.sql.DriverManager

fun checkDatabaseConnection() {
    val url = requireNotNull(System.getenv("DB_URL")) {
        "DB_URL is not set"
    }
    val user = requireNotNull(System.getenv("DB_USER")) {
        "DB_USER is not set"
    }
    val password = requireNotNull(System.getenv("DB_PASSWORD")) {
        "DB_PASSWORD is not set"
    }

    DriverManager.getConnection(url, user, password).use { connection ->
        connection.createStatement().use { statement ->
            statement.queryTimeout = 5

            statement.executeQuery("SELECT 1").use { result ->
                check(result.next() && result.getInt(1) == 1)
                println("PostgreSQL connection OK")
            }
        }
    }
}