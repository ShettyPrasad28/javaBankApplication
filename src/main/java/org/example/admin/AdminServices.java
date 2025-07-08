package org.example.admin;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.example.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminServices {
    private final String adminname="admin";
    private final String adminpass="admin123";

    public boolean login(String user,String name){
        return (adminname.equals(user) && adminpass.equals(name));

    }
    //creating branch
    public int createBranch(int branchid,String branchname,String location,String ifsc) {

        String check="SELECT COUNT(*) FROM branch WHERE branchname=?";
        try(Connection conn= DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(check)) {
            ps.setString(1, branchname);
            ResultSet rs = ps.executeQuery();
            if(rs.next() && rs.getInt(1)>0){
                return 0;
            }

        }catch (SQLException e){
            System.out.println("check branch error "+e);
            return -1;
        }
        String insert="INSERT INTO branch(branchid,branchname,location,ifsccode)VALUES(?,?,?,?)";
        try(Connection conn= DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(insert)){
            ps.setInt(1,branchid);
            ps.setString(2,branchname);
            ps.setString(3,location);
            ps.setString(4,ifsc);
            ps.executeUpdate();
            return 1;
        }catch(SQLException e){
            System.out.println("Insert branch error "+e);
            return -1;
        }
    }

//    creating Manager
    public int createManager(int Mangid,String Mname,String Uname,String pass,int bid){

        String checkBranch="SELECT COUNT(*) FROM branch WHERE branchid=?";
        try(Connection conn=DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(checkBranch)){
            ps.setInt(1,bid);
            ResultSet rs= ps.executeQuery();
            if(!(rs.next() && rs.getInt(1)>0)){
                return -2;
            }
        }catch(SQLException e){
            System.out.println("branch id check Error "+e);
            return -1;
        }

        String check="SELECT COUNT(*) FROM manager WHERE username=?";
        try(Connection conn=DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(check)){
            ps.setString(1,Uname);
            ResultSet rs= ps.executeQuery();
            if(rs.next() && rs.getInt(1)>0){
                return 0;
            }
        }catch(SQLException e){
            System.out.println("Manager check Error "+e);
            return -1;
        }

        String insert="INSERT INTO manager(managerid,managername,username,password,bid) VALUES(?,?,?,?,?)";
        try(Connection conn=DbConnection.getConnection();PreparedStatement ps=conn.prepareStatement(insert)){
            ps.setInt(1,Mangid);
            ps.setString(2,Mname);
            ps.setString(3,Uname);
            ps.setString(4,pass);
            ps.setInt(5,bid);
            ps.executeUpdate();
            return 1;
        }catch(SQLException e){
            System.out.println("Manager insert Error "+e);
            return -1;
        }
    }


}
