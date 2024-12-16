package repository.databaseRepository;

import domain.Identifiable;
import org.sqlite.SQLiteDataSource;
import repository.MemoryRepository;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseRepository<ID,T extends Identifiable<ID>> extends MemoryRepository<ID,T> {

    protected String JDBC_URL;
    protected Connection conn = null;

    public void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void loadFromDatabase();

    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
