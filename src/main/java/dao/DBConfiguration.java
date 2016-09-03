package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by uzer on 03.09.2016.
 */
public class DBConfiguration {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "1111";

    static Connection conn = null;

    private static void createConnection() throws SQLException {
        conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }


    public static Connection getConnection() throws SQLException {
        if (conn == null)
            createConnection();
        return conn;
    }

    public static void initDB() throws SQLException {

        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Clients");
            st.execute("CREATE TABLE Clients (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(45),last_name VARCHAR(45), email VARCHAR(45), address VARCHAR(45), phone VARCHAR(45))");
            st.execute("DROP TABLE IF EXISTS Products");
            st.execute("CREATE TABLE Products (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45), serialNumber VARCHAR(45),price DOUBLE)");
            st.execute("DROP TABLE IF EXISTS Orders");
            st.execute("CREATE TABLE `orders` (" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `date` datetime DEFAULT NULL," +
                    "  `client` int(11) DEFAULT NULL," +
                    "  `product` int(11) DEFAULT NULL," +
                    "  `quantity` int(11) DEFAULT NULL," +
                    "  `order_number` int(11) NOT NULL," +
                    "  PRIMARY KEY (`id`)," +
                    "  KEY `productFK_idx` (`product`)," +
                    "  KEY `clientFK_idx` (`client`)," +
                    "  CONSTRAINT `clientFK` FOREIGN KEY (`client`) REFERENCES `clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
                    "  CONSTRAINT `productFK` FOREIGN KEY (`product`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)");
        }
    }
}
