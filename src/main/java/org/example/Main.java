package org.example;

import org.example.admin.AdminServices;
import org.example.customer.CustomerService;
import org.example.manager.ManagerServices;

import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.exit;


public class Main {
    public static void main(String[] args)  {

       Scanner sc=new Scanner(System.in);
       AdminServices ad=new AdminServices();
        ManagerServices ms=new ManagerServices();
        CustomerService cs=new CustomerService();


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
                                        int cust_id=sc.nextInt();
                                        sc.nextLine();
                                        String field=null;
                                        String newVal=null;
                                        System.out.println("Enter the which field to be updated:\n1.Email\n2.Phone number");
                                        int sel=sc.nextInt();
                                        sc.nextLine();
                                        switch (sel){
                                            case 1:field="Email";
                                                System.out.println("Enter new Email");
                                                newVal=sc.nextLine();
                                                if(ms.updateCustomer(cust_id,field,newVal)){
                                                    System.out.println("Email of "+cust_id+" updated successfully");
                                                }else{
                                                    System.out.println("Error update failed");
                                                }
                                                break;
                                            case 2:field="PhoneNumber";
                                                  System.out.println("Enter new number");
                                                  newVal=sc.nextLine();
                                                  if(ms.updateCustomer(cust_id,field,newVal)){
                                                      System.out.println("Phone number of "+cust_id+" updated successfully");
                                                  }else{
                                                      System.out.println("Error has occurred");
                                                  }
                                                  break;
                                            default:System.out.println("Invalid input");
                                                    break;
                                        }
                                        break;
                                case 3:System.out.println("Make Transaction");
                                        System.out.println("Enter account Id:");
                                        int acc_id=sc.nextInt();
                                        System.out.println("Choose transaction type:\n1. Deposit\n2. Withdraw");
                                        int trnc_type=sc.nextInt();
                                        System.out.println("Enter the amount:");
                                        double amount=sc.nextDouble();
                                        sc.nextLine();
                                        String type=(trnc_type==1)?("Deposit"):(trnc_type==2)?("Withdraw"):null;
                                        if(type!=null){
                                            boolean res=ms.transaction(acc_id,type,amount);
                                            if(!res){
                                                System.out.println("Transaction failed");
                                            }
                                        }else{
                                            System.out.println("Invalid transaction type");
                                        }
                                        break;
                                case 4:System.out.println("Generate report:\n1. Daily Report \n2. Monthly report");
                                    System.out.println("Enter your choice");
                                    int ch=sc.nextInt();
                                    System.out.print("Enter Account ID: ");
                                    int accId = sc.nextInt();
                                    sc.nextLine();
                                    String repo_type=(ch==1)?("Daily"):(ch==2)?("Monthly"):null;
                                    if(repo_type==null){
                                        System.out.println("Invalid choice");
                                        break;
                                    }
                                    ms.generateReport(repo_type,muser,accId);
                                    break;
                                case 5:
                                    System.out.println("logged out successfully");
                                    running=false;
                                    break;
                                default:
                                    System.out.println("Invalid choice");

                            }

                        }while(running);

                    }else{
                        System.out.println("login failed username does not exists");
                    }
                    break;
           case 3:System.out.println("Welcome to Banking");
                    System.out.println("Enter username:");
                    String uname= sc.nextLine();
                    System.out.println("Enter passwaord:");
                    String cpwd=sc.nextLine();
                    if(cs.login(uname,cpwd)){
                        System.out.println("logged in successfully");
                        System.out.println("Choose\n1. View Account\n2. logout");
                        int ch=sc.nextInt();
                        sc.nextLine();
                        switch (ch){
                            case 1:cs.viewAccount(uname);
                                    break;
                            case 2:System.out.println("logged out successfully");
                                    exit(0);
                            default:System.out.println("Invalid choice");
                        }
                    }else{
                        System.out.println("Login failed");
                    }
                    break;
           default:
               System.out.println("Invalid Selection");
               break;



       }


    }
}
