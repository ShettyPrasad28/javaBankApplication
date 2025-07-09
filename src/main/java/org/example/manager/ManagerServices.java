package org.example.manager;

import org.example.db.DbConnection;

import java.awt.geom.Arc2D;
import java.sql.*;
import java.time.format.DateTimeFormatter;

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
            System.out.println(e.getMessage());
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

    //update customer
    public boolean updateCustomer(int cid,String field,String newVal){
        try(Connection conn=DbConnection.getConnection()){
            //checking if phoneNumber already exists
            if(field.contains("PhoneNumber")){
                long phone;
                try{
                    phone=Long.parseLong(newVal);
                }catch(NumberFormatException e){
                    System.out.println("Invalid phone number format");
                    return false;
                }

                String checkphonequery="SELECT customer_id FROM customer WHERE PhoneNumber=? AND customer_id!=?";
                PreparedStatement ps=conn.prepareStatement(checkphonequery);
                ps.setLong(1,phone);
                ps.setInt(2,cid);
                ResultSet rs= ps.executeQuery();

                if(rs.next()){
                    System.out.println("Phone number already in use by another customer");
                    return false;
                }
            }
            String query="UPDATE customer SET "+field+" =? WHERE customer_id=?";
            PreparedStatement ps=conn.prepareStatement(query);
            if(field.contains("PhoneNumber")){
                ps.setLong(1,Long.parseLong(newVal));
            }else{
                ps.setString(1,newVal);
            }
            ps.setInt(2,cid);
            int row=ps.executeUpdate();
            if(row>0){
                return true;
            }else{
                System.out.println("Customer not found! update failed");
                return false;
            }

        }catch (SQLException e){
            System.out.println("Database error"+e.getMessage());
            return false;
        }
    }

    //transsaction performs deposite or withdraw
    public boolean transaction(int account_id,String type,double amount){
        try(Connection conn= DbConnection.getConnection()){

            //fetch balance using account id
            String balquery="SELECT initial_bal FROM account WHERE account_id=?";
            PreparedStatement ps=conn.prepareStatement(balquery);
            ps.setInt(1,account_id);
            ResultSet rs=ps.executeQuery();

            if(!rs.next()){
                System.out.println("Account not found");
                return false;
            }

            double currentBalance=rs.getDouble("initial_bal");
            double newBalance=currentBalance;

            //Processing the transaction
            if(type.equalsIgnoreCase("deposit")){
                newBalance+=amount;
            } else if (type.equalsIgnoreCase("withdraw")) {
                if(currentBalance<amount){
                    System.out.println("Insufficient Balance available balance is ₹"+currentBalance);
                    return false;
                }
                newBalance-=amount;
            }else{
                return false;
            }

            //updating the balance in account table
            String update="UPDATE account SET initial_bal=? WHERE account_id=?";
            PreparedStatement upstmt=conn.prepareStatement(update);
            upstmt.setDouble(1,newBalance);
            upstmt.setInt(2,account_id);
            int row=upstmt.executeUpdate();
            //insert transaction details into transaction table
            if(row>0){
                String insert_trans="INSERT INTO transaction(account_id,transaction_type,amount,transaction_date)VALUES(?,?,?,NOW())";
                PreparedStatement upps=conn.prepareStatement(insert_trans);
                upps.setInt(1,account_id);
                upps.setString(2,type.toUpperCase());
                upps.setDouble(3,amount);
                upps.executeUpdate();
                System.out.println("Transaction successfull! balance : ₹"+newBalance);
                return true;
            }else{
                System.out.println("failed to update balance");
                return false;
            }

        }catch (SQLException e){
            System.out.println("Database Error"+e.getMessage());
            return false;
        }
    }

    //generating Report about transaction
    public void generateReport(String type,String muser,int accid){
        String date_condition;
        if(type.equalsIgnoreCase("daily")){
            date_condition="DATE(t.transaction_date)=CURDATE()";
        } else if (type.equalsIgnoreCase("monthly")) {
            date_condition="MONTH(t.transaction_date)=MONTH(CURDATE()) AND YEAR(t.transaction_date)=YEAR(CURDATE())";
        }else{
            System.out.println("invalid report type");
            return ;
        }

        String query="SELECT t.transaction_id,t.account_id,t.transaction_type,t.amount,t.transaction_date "+
                "FROM transaction t "+
                "JOIN account a ON t.account_id=a.account_id "+
                "JOIN customer c ON a.account_id=c.customer_id "+
                "JOIN manager m ON c.manager_id=m.managerid "+
                "WHERE "+date_condition+" AND m.username=? AND t.account_id=?";

        try(Connection conn= DbConnection.getConnection();PreparedStatement ps= conn.prepareStatement(query
        )){
            ps.setString(1,muser);
            ps.setInt(2, accid);
            ResultSet rs=ps.executeQuery();
            System.out.println(type.toUpperCase()+" Transaction Report");
            System.out.printf("%-5s %-10s %-12s %-10s %-20s%n", "ID", "Acc ID", "Type", "Amount", "Date/Time");

            boolean found=false;
            while(rs.next()){
                found=true;
                int tid=rs.getInt("transaction_id");
                int acid=rs.getInt("account_id");
                String trans_type=rs.getString("transaction_type");
                double amt=rs.getDouble("amount");
                Timestamp ts=rs.getTimestamp("transaction_date");
                String formattedDate=ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                System.out.printf("%-5d %-10d %-12s ₹%-10.2f %-20s%n", tid, acid, trans_type, amt, formattedDate);

            }
            if(!found){
                System.out.println("transaction not found for this peroid");
            }


        }catch(SQLException e){
            System.out.println("data base error "+e.getMessage());

        }

    }





}
