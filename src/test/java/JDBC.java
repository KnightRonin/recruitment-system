//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
///**
// * @Author HuangGuoFu
// * @Date 2022/3/24 12:20
// **/
//public class JDBC {
//    public static void main(String[] args) {
//        String driver = "com.mysql.cj.jdbc.Driver";
//        String url = "jdbc:mysql://175.178.196.83:3306/hbase";
//        Connection connetcion = null;
//        try {
//            Class.forName(driver);
//            connetcion = DriverManager.getConnection(url, "root", "root");
//            System.out.println("success");
//            connetcion.close();
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//            System.out.println("fail");
//        }
//    }
//}
