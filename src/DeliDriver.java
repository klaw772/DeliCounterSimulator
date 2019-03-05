import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DeliDriver {
    static Statement query = null;
    static Connection con = null;

    public static void connect() {
        try {
            String db = "jdbc:sqlite:Law_DeliCounter.db";
            con = DriverManager.getConnection(db);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void disconnect(){
        try {
           if (con != null) {
               con.close();
           }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            connect();
            System.out.println("Welcome to the deli counter!");
            System.out.println("This is a simulation of what a typical deli counter may look like. Let's get started!");

            Thread.sleep(2000);
            try {
                query = con.createStatement();

                for (int i = 1; i <= query.executeQuery("SELECT COUNT(order_id) FROM Deli_Order").getInt(1); i++) {
                    System.out.println("Come on up, Order Number " + query.executeQuery("SELECT * FROM Deli_Order " +
                            "WHERE order_id = " + i).getString("order_id") + "\n");
                    Thread.sleep(2000);
                    System.out.println("Hello there, " + query.executeQuery("SELECT customer_name FROM Customer, Deli_Order " +
                            "WHERE Deli_Order.order_id = " + i +
                            " AND Deli_Order.customer_id = Customer.customer_id").getString("customer_name"));
                    Thread.sleep(2000);
                    System.out.println("Looks like you want this product: " + query.executeQuery("SELECT product_name FROM Deli_Product, Deli_Order " +
                            "WHERE Deli_Order.product_id = Deli_Product.product_id " +
                            "AND order_id = " + i).getString("product_name") + "\n");
                    Thread.sleep(2000);
                    System.out.println("That's great! Your order also says that you would like " + query.executeQuery("SELECT product_weight FROM Deli_Order " +
                            "WHERE order_id = " + i).getString("product_weight") +
                            " pounds of this product.");
                    System.out.println("This product costs " + query.executeQuery("SELECT product_price FROM Deli_Product_Type, Deli_Product, Deli_Order " +
                            "WHERE Deli_Product.product_type_id = Deli_Product_Type.product_type_id " +
                            "AND Deli_Order.product_id = Deli_Product.product_id " +
                            "AND Deli_Order.order_id = " + i).getString("product_price") + " per pound.\n");
                    Thread.sleep(2000);
                    System.out.println("With the desired weight of your product in mind, the final price for your order would be " +
                            query.executeQuery("SELECT printf(\"%.2f\", product_price * product_weight) AS total FROM Deli_Product_Type, Deli_Product, Deli_Order " +
                                    "WHERE Deli_Product.product_type_id = Deli_Product_Type.product_type_id " +
                                    "AND Deli_Order.product_id = Deli_Product.product_id " +
                                    "AND Deli_Order.order_id = " + i).getString("total"));
                    System.out.println("Thanks for your order!\n\n");
                    Thread.sleep(2000);
                }
                System.out.println("Thanks for stopping by the deli today! Hope you have a great day.");
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            disconnect();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}

