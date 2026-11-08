package database

class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String,
    val maximumPoolSize: Int,
    val minimumIdle: Int
) {
    init {
        require(url.startsWith("jdbc:postgresql://")) {
            "DB_URL must start with jdbc:postgresql://"
        }
        require(user.isNotBlank()) {
            "DB_USER must not be blank"
        }
        require(password.isNotBlank()) {
            "DB_PASSWORD must not be blank"
        }
        require(maximumPoolSize > 0) {
            "DB_POOL_SIZE must be greater than 0"
        }
        require(minimumIdle in 0..maximumPoolSize) {
            "DB_MIN_IDLE must be between 0 and DB_POOL_SIZE"
        }
    }
}