package dao;

import java.sql.*;

/**
 * Created by uzer on 02.09.2016.
 */
public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != client.id) return false;
        if (firstName != null ? !firstName.equals(client.firstName) : client.firstName != null) return false;
        if (lastName != null ? !lastName.equals(client.lastName) : client.lastName != null) return false;
        if (email != null ? !email.equals(client.email) : client.email != null) return false;
        if (address != null ? !address.equals(client.address) : client.address != null) return false;
        return phone != null ? phone.equals(client.phone) : client.phone == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    public void addClient(Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement
                ("INSERT INTO Clients (first_name, last_name, email,address,phone) VALUES(?, ?,?,?,?)")) {
            ps.setString(1, this.firstName);
            ps.setString(2, this.lastName);
            ps.setString(3, this.email);
            ps.setString(4, this.address);
            ps.setString(5, this.phone);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getByPhone(Connection conn, String phone) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clients WHERE phone = ?")) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.id = rs.getInt(1);
                this.firstName = rs.getString(2);
                this.lastName = rs.getString(3);
                this.phone = rs.getString(4);
                this.address = rs.getString(5);
                this.email = rs.getString(6);
                return true;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void printAll(Connection conn) {

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clients");
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
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
