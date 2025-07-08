package org.example;

import org.example.admin.AdminServices;
import org.example.manager.ManagerServices;

import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args)  {

       Scanner sc=new Scanner(System.in);
       AdminServices ad=new AdminServices();
        ManagerServices ms=new ManagerServices();


       System.out.println("Welcome to Bank,Choose user type:");
       System.out.println("1.Admin\t2.Manager\t3.Customer");
       int choice=sc.nextInt();
       sc.nextLine();
       switch(choice){
           case 1: System.out.println("Enter the user name");
                    String user=sc.nextLine();
                    System.out.println("Enter password");
                    String pass=sc.nextLine();

                    if(ad.login(user,pass)){
                        System.out.println("logged in Successfully");
                        System.out.println("Welcome Admin ");
                        boolean running=true;
                        do{
                            System.out.println("Select task 1.Create Branch\t2.Create Manager\t3.logout");
                            int select= sc.nextInt();
                            sc.nextLine();
                            switch(select){
                                case 1:System.out.println("Enter Branch id:");
                                        int branchid=sc.nextInt();
                                        sc.nextLine();
                                        System.out.println("Enter Branch name:");
                                        String branchname=sc.nextLine();
                                        System.out.println("Enter location");
                                        String  location=sc.nextLine();
                                        System.out.println("Enter ifsc code");
                                        String ifsc=sc.nextLine();
                                        int bres=ad.createBranch(branchid,branchname,location,ifsc);
                                        if(bres==0){
                                            System.out.println("Branch already exists");
                                        }else if(bres==1){
                                            System.out.println("Branch created successfully");
                                        }else{
                                            System.out.println("Error");
                                        }
                                        break;
                                case 2:System.out.println("Enter Manager id");
                                        int Mid=sc.nextInt();
                                        sc.nextLine();
                                        System.out.println("Enter Manager name");
                                        String Mname=sc.nextLine();
                                        System.out.println("Enter user name");
                                        String uname=sc.nextLine();
                                        System.out.println("Enter password");
                                        String mpass=sc.nextLine();
                                        System.out.println("Enter the branch id");
                                        int bid=sc.nextInt();
                                        int mres= ad.createManager(Mid,Mname,uname,mpass,bid);
                                        if(mres==0){
                                            System.out.println("Manager and username already exists");
                                        } else if (mres==1){
                                            System.out.println("Manager data added Successfully");
                                        }else if(mres==-2){
                                            System.out.println("Branch does not exists");
                                        }else{
                                            System.out.println("Error");
                                        }
                                        break;
                                case 3:System.out.println("logged out Successfully");
                                        running=false;
                                        break;
                                default:System.out.println("Invaild Input");
                            }
                        }while(running);
                    }else{
                        System.out.println("login failed");
                    }
                    break;
           case 2:
                    System.out.println("Welcome Manager!");
                    System.out.println("Enter username:");
                    String muser=sc.nextLine();
                    System.out.println("Enter password:");
                    String mpass=sc.nextLine();
                    if(ms.login(muser,mpass)){
                        System.out.println("logged in successfully");
                        boolean running=true;
                        do{
                            System.out.println("Select task 1.Add Customer\t2.Manage Account\t3.Transaction\t4.Report\t5.logout");
                            int select=sc.nextInt();
                            sc.nextLine();
                            switch(select){
                                case 1:
                                        System.out.println("Enter Customer name:");
                                        String cname=sc.nextLine();
                                        System.out.println("Enter email:");
                                        String cemail=sc.nextLine();
                                        System.out.println("Enter phone number");
                                        long cust_phone=sc.nextLong();
                                        sc.nextLine();
                                        System.out.println("Enter customer username");
                                        String cuser=sc.nextLine();
                                        System.out.println("Enter password");
                                        String cpass=sc.nextLine();
                                        int cust=ms.Addcustomer(cname,cemail,cust_phone,muser,cuser,cpass);
                                        if(cust==-1){
                                            System.out.println("ERROR while inserting customer data");


                                        } else if (cust==0) {
                                            System.out.println("Phone number is aLready registered in another customer ");

                                        }else{
                                            System.out.println("Added Customer Successfully");
                                            System.out.println("Enter account type");
                                            String acctype=sc.nextLine();
                                            System.out.println("Enter intial deposited balance:");
                                            double inBal=sc.nextDouble();
                                            sc.nextLine();
                                            ms.addAccount(cust,acctype,inBal);

                                        }
                                        break;
                                case 2:System.out.println("Welcome to Account management");
                                        System.out.println("Enter the customer id");
                            }

                        }while(running);

                    }else{
                        System.out.println("login failed username does not exists");
                    }
                    break;



       }


    }
}
