package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

/**
 * Created by uzer on 02.09.2016.
 */
public class Main {

   static Connection conn;

    public static void main(String[] args) {

        try  {
            conn = DBConfiguration.getConnection();
            DBConfiguration.initDB();
            try(Scanner sc = new Scanner(System.in)) {

                while (true) {
                    System.out.println("Add client -> add client");
                    System.out.println("Add product -> add product");
                    System.out.println("Create order -> create order");
                    System.out.println("Print tables data -> print");
                    System.out.print("-> ");

                    String command = sc.nextLine();
                    switch (command) {
                        case "add client":
                            addClient(sc);
                            break;
                        case "add product":
                            addProduct(sc);
                            break;
                        case "create order":
                            addOrder(sc);
                            break;
                        case "print":
                            printTables(sc);
                            break;
                        default:
                            System.out.println("Enter correct command, please");
                    }
                }
            } finally {
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public static void addClient(Scanner sc) {
        Client client = new Client();
        System.out.println("enter first name:");
        client.setFirstName(sc.nextLine());
        System.out.println("enter last name:");
        client.setLastName(sc.nextLine());
        System.out.println("enter email:");
        client.setEmail(sc.nextLine());
        System.out.println("enter address:");
        client.setAddress(sc.nextLine());
        System.out.println("enter phone:");
        client.setPhone(sc.nextLine());
        client.addClient(conn);
    }

    public static void addProduct(Scanner sc) {
        Product product = new Product();
        System.out.println("enter product name:");
        product.setName(sc.nextLine());
        System.out.println("enter product serial number:");
        product.setSerialNumber(sc.nextLine());
        System.out.println("enter unit price:");
        product.setPrice(Double.parseDouble(sc.nextLine()));
        product.addProduct(conn);
    }

    public static void addOrder(Scanner sc) throws SQLException {
        Order order = new Order();
        order.setDate();
        System.out.println("enter client phone:");
        Client client = new Client();
        boolean res = client.getByPhone(conn, sc.nextLine());
        if (!res) {
            System.out.println("Wrong client phone number or client not found.");
            return;
        } else {
            order.setClient(client);
            order.setOrderNumber();
            String command = "yes";
            while (command.equals("yes")) {
                System.out.println("enter product serial number:");
                Product product = new Product();
                res = product.getBySerial(conn, sc.nextLine());
                if (res)
                    System.out.println("enter the product quantity:");
                order.getProducts().put(product, Integer.parseInt(sc.nextLine()));
                System.out.println("Add new product? -> yes/no");
                command = sc.nextLine();
            }
            double amount = order.addOrder(conn);
            System.out.println("Your order is accepted. Order amount is " + amount + "$");
        }
    }

    private static void printTables(Scanner sc) {

        System.out.println("Enter name of tables to print -> Clients, Products, Orders");
        String tableName = sc.nextLine();
        switch (tableName) {
            case "Clients":
                Client.printAll(conn);
                break;
            case "Products":
                Product.printAll(conn);
                break;
            case "Orders":
                Order.printAll(conn);
                break;
            default:
                System.out.println("Enter correct command, please");
        }
    }
}