package org.example.customer;
import org.example.db.DbConnection;

import java.sql.*;

public class CustomerService {
    //customer login
    public boolean login(String user,String pass){
        String qry="SELECT * FROM customer WHERE customer_username=? AND password=?";
        try(Connection conn= DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(qry)){
            ps.setString(1,user);
            ps.setString(2,pass);
            ResultSet rs=ps.executeQuery();
            return rs.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    //view account details
    public void viewAccount(String username){
        String qry="SELECT a.account_id,a.account_type,a.initial_bal "+" FROM account a JOIN customer c ON a.customer_id=" +
                "c.customer_id WHERE c.customer_username=?";
        try(Connection conn=DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(qry)){
            ps.setString(1,username);
            ResultSet rs=ps.executeQuery();
            System.out.println("Account details");
            while(rs.next()){
                System.out.println("Account_id: "+rs.getInt("account_id"));
                System.out.println("Account_type: "+rs.getString("account_type"));
                System.out.println("Balance: ₹"+rs.getDouble("initial_bal"));
                System.out.println("----------------------------------------------------------------------------------");
            }
        }catch (SQLException e){
            System.out.println("Account view error"+e.getMessage());
        }
    }
}
