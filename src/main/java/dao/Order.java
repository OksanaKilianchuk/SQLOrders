package dao;


import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by uzer on 02.09.2016.
 */
public class Order {

    private int id;
    private Date date;
    private int orderNumber;
    private Client client;
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        this.date = new Date();
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber() {
        this.orderNumber = generateOrderNumber();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    private int generateOrderNumber() {
        return client.hashCode() + date.hashCode();
    }

    public double addOrder(Connection conn) throws SQLException {
        double amount = 0;
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement
                    ("INSERT INTO Orders (date, client, product, quantity, order_number) VALUES(?, ?,?,?,?)")) {
                for (Map.Entry<Product, Integer> product : products.entrySet()) {
                    ps.setDate(1, null);//(java.sql.Date) this.date);
                    ps.setInt(2, this.client.getId());
                    ps.setInt(3, product.getKey().getId());
                    ps.setInt(4, product.getValue());
                    ps.setInt(5, this.orderNumber);
                    ps.executeUpdate();
                    amount += product.getValue() * product.getKey().getPrice();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        }
        return amount;
    }

    public static void printAll(Connection conn) {

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Orders");
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++)
                System.out.print("| \t" + md.getColumnName(i) + "\t");
            System.out.println("\n --------------------------------------------------------------------------------");
            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print("| \t" + rs.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
