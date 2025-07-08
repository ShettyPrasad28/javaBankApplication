package org.example.manager;

import org.example.db.DbConnection;

import java.sql.*;

public class ManagerServices {
    //Manager login
    public boolean login(String user_name,String pass){
        String logquery="SELECT * FROM manager WHERE username=? AND password=?";
        try(Connection conn= DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(logquery)){
            ps.setString(1,user_name);
            ps.setString(2,pass);
            ResultSet rs= ps.executeQuery();
            return rs.next();
        }catch (SQLException e){
            System.out.println("Error "+e);
            return false;
        }
    }

    //Customer management
    public int Addcustomer(String cust_name,String cust_email,long cust_phone,String muser,String cuser,String cpass){
        try(Connection conn=DbConnection.getConnection()) {
            //to get manager id from username logged in
            String mid = "SELECT managerid FROM manager WHERE username=?";
            PreparedStatement mps=conn.prepareStatement(mid);
            mps.setString(1,muser);
            ResultSet mrs=mps.executeQuery();
            mrs.next();
            int managerid=mrs.getInt("managerid");

            //check phone number
            String query_phone="SELECT customer_id FROM customer WHERE PhoneNumber=?";
            PreparedStatement cphone=conn.prepareStatement(query_phone);
            cphone.setLong(1,cust_phone);
            ResultSet prs=cphone.executeQuery();
            if(prs.next()){
                return 0;
            }else{
                //auto generate customer id
                String maxIdQuery = "SELECT MAX(customer_id) AS max_id FROM customer";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(maxIdQuery);

                int nextCustomerId = 1; // default if no records
                if (rs.next()&& rs.getInt("max_id") > 0) {
                    nextCustomerId = rs.getInt("max_id") + 1;
                }

                String insertquery="INSERT INTO customer(customer_id,customer_name,Email,PhoneNumber,manager_id,customer_username,password)VALUES(?,?,?,?,?,?,?)";
                PreparedStatement insert=conn.prepareStatement(insertquery);
                insert.setInt(1,nextCustomerId);
                insert.setString(2,cust_name);
                insert.setString(3,cust_email);
                insert.setLong(4,cust_phone);
                insert.setInt(5,managerid);
                insert.setString(6,cuser);
                insert.setString(7,cpass);
                insert.executeUpdate();
                return nextCustomerId;
            }
        }catch (SQLException e){
            System.out.println(e);
            return -1;
        }
    }

    //add account
    public void addAccount(int cid,String acctype,double inbal){
        try(Connection conn= DbConnection.getConnection()) {
            //auto generate account id
            String query = "SELECT MAX(account_id) AS max_id FROM account";
            PreparedStatement ps=conn.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            int nextacc_id=0;
            if(rs.next()){
                nextacc_id+=rs.getInt("max_id")+1;
            }
            //insert account to account table
            String accquery = "INSERT INTO account(account_id,account_type,initial_bal,customer_id)VALUES(?,?,?,?)";
            PreparedStatement aps=conn.prepareStatement(accquery);
            aps.setInt(1,nextacc_id);
            aps.setString(2,acctype);
            aps.setDouble(3,inbal);
            aps.setInt(4,cid);
            aps.executeUpdate();
            System.out.println("Account created for customer");

        }catch(SQLException e){
            System.out.println("ERROR in account creation \n"+e);
        }

    }





}
