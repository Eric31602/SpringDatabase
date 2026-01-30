package be.vdab.geld;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class DataSourceTest {
    private final DataSource dataSource;
    public DataSourceTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    public void deDataSourceKanEenConnectionGeven() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            assertThat(connection.getCatalog()).isEqualTo("geld");
        }
    }
}
