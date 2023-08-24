package data;

import lombok.SneakyThrows;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.Assertions;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var SQLCode = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        var code = runner.query(connection, SQLCode, new ScalarHandler<String>());
        return new DataHelper.VerificationCode(code);
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConnection();
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM card_transactions");
        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    public static String getUserBlockedStatus() {
        var SQLStatus = "SELECT status FROM users WHERE login='vasya'";
        var connection = getConnection();
        return runner.query(connection, SQLStatus, new ScalarHandler<String>());
    }
}