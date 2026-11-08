package database.migration

import org.flywaydb.core.Flyway
import javax.sql.DataSource

internal class DatabaseMigrator(
    private val dataSource: DataSource,
) {
    fun migrate() {
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .cleanDisabled(true)
            .load()
            .migrate()
    }
}