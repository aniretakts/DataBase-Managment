package project_db;
import java.sql.*;
import java.text.SimpleDateFormat; 
import java.util.Scanner;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class db_project {     
    public static void main(String[] args) throws SQLException {                            
        System.out.print("Hello!\n");
        System.out.print("To login in as a Sportsman-Fan, press 1\nTo login in as a referee, press 2\n");
        System.out.print("To login in as a coach, press 3\nTo login in as a manager, press 4\n");
        Scanner in = new Scanner(System.in);
        int num=0;
        try{ num = in.nextInt(); }
        catch(Exception e) { 
            System.out.println("Enter a number from 1 to 4.\n"); 
            System.exit(0); 
        }
        while(num<1 || num>5) {
            System.out.print("Please, enter a number from 1 to 4\n");
            try{ num = in.nextInt(); }
            catch(Exception e) { 
                System.out.println("You did not enter a number.\n");
                System.exit(0); 
            }
        }
        
        String fname;
        System.out.print("Please enter your fullname\n");
        Scanner in1 = new Scanner(System.in);
        fname=in1.nextLine();
         
        
        switch (num) {
            case 1: Sportsmen.showInfo(fname);
                    break;
            case 2: Referee.showInfo(fname);
                    break;
            case 3: Coach.showInfo(fname);
                    break;
            case 4: Manager.showInfo(fname);
                    break;
            default: break;
        }   
    }   
}
