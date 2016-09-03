package dao;

import java.sql.*;

/**
 * Created by uzer on 02.09.2016.
 */
public class Product {

    private int id;
    private String name;
    private String serialNumber;
    private double price;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (Double.compare(product.price, price) != 0) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        return serialNumber != null ? serialNumber.equals(product.serialNumber) : product.serialNumber == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public void addProduct(Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement
                ("INSERT INTO Products (name, serialNumber, price) VALUES(?, ?,?)")) {
            ps.setString(1, this.name);
            ps.setString(2, this.serialNumber);
            ps.setDouble(3, this.price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getBySerial(Connection conn, String serialNumber) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE serialnumber = ?")) {
            ps.setString(1, serialNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.id = rs.getInt(1);
                this.name = rs.getString(2);
                this.serialNumber = rs.getString(3);
                this.price = rs.getDouble(4);
                return true;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void printAll(Connection conn) {

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Products");
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
