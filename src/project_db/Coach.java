/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author katerina
 */
public class Coach {
    static final String URL = "jdbc:mysql://localhost:3306/db1"; 
    static final String USER = "root";
    static final String PASS = "";
    public static void showInfo(String cfullname) throws SQLException {
        String sql, sqll, teamname = "";
        Connection conn = null;
        Statement stmt = null;
        int teamid=0;
        
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            System.out.println("Connecting to database...\n") ;
            conn = DriverManager.getConnection(URL,USER,PASS);
            stmt = conn.createStatement();
            
            //check if that coach exists  
            sql="SELECT fullname1 FROM Coaches " +
                    "WHERE fullname1='" + cfullname ;
            ResultSet rs = stmt.executeQuery(sql);
            
            //if he exists
            if(rs.next()) {
                int cid = rs.getInt("coachId");
                System.out.printf("Hello coach!\nYour team is: ");
            
                //nested sql stmt gia tin euresi tou id kai tou onomatos tis omadas tou
                Statement stmt1 = conn.createStatement();
                sql="SELECT team1 FROM Coaches WHERE coachId=" + cid;                 
                ResultSet rs1 = stmt1.executeQuery(sql);
                if(rs1.next()) {  
                    teamname=rs1.getString("team1"); 
                    teamid=rs1.getInt("team1"); 
                }
                System.out.print(teamname + "\n");
            
                System.out.print("1. Information about upcoming matches\n");
                System.out.print("2. Information about athletes.\n");
                System.out.print("What is your option? (1,2):\n");
            
                Scanner in = new Scanner(System.in);
                int num=0;
                while(num<1 || num>2) {
                    System.out.print("Press 1 or 2:\n");
                    try{ num = in.nextInt(); }
                    catch(Exception e) { 
                        System.out.println("Please enter 1 or 2.\n");
                        System.exit(0); 
                    }
                }
            
                switch (num) {
                    case 1: upcoming(teamid,conn);
                        break;
                    case 2: athletes(teamid,conn);
                            break;
                    default: break;
                }
            } else { System.out.print("That name wasn't found.\n"); }
            stmt.close();
            conn.close();
        }catch(SQLException | ClassNotFoundException se){ 
            System.out.print("Something went wrong.\n"); 
        }
        
        System.out.println("\nGoodbye! Have a nice day\n");
    }
    
    static void upcoming(int tid, Connection conn) {
        String sql, sql1;
        int flag=1, yesgoals=0, nogoals=0;    
        
        try {
            //find his team's games
            Statement stmt = conn.createStatement();
            sql="SELECT gameId FROM Games WHERE gteamA=" + tid + "OR gteamB=" +tid;
            ResultSet rs = stmt.executeQuery(sql);
                                       
            while(rs.next()) {
                int mid = rs.getInt("gameId");
                
                //games info
                Statement stmt1 = conn.createStatement();
                sql1="SELECT gdate,ghour,field FROM Games where gameId=" +mid;                        
                ResultSet rs1 = stmt1.executeQuery(sql1);
                                                         
                while(rs1.next()) {
                    String day = rs1.getDate("gdate").toString();                        
                    String time = rs1.getTime("ghour").toString(); 
                    int courtid=rs1.getInt("field");
                    
                    
                    Date date = new Date();
                    String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
                   
                    flag=1;
                    
                    //upcoming games info
                    System.out.print("\nUpcoming game on " + day + " at " + time);
                    String sql3="SELECT field_name, town FROM Fields WHERE Games.field=" + courtid;
                    Statement stmt3 = conn.createStatement();
                    ResultSet rs3=stmt3.executeQuery(sql3);
                    if(rs3.next()) {
			String fname=rs3.getString("field_name");
			String ftown=rs3.getString("town");
			System.out.print("\n Field: " + fname + "\n Town: " + ftown);
                    }
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
        }       
        if(flag==0) { System.out.print("There isn't any upcoming game.\n"); }

    }
    
    static void athletes(int tid, Connection conn) {
        String sql;
        int sum=0;
        try {
            //athletes of the team
            Statement stmt = conn.createStatement();
            sql="SELECT fullname3 FROM Players WHERE tema3=" +tid;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                    sum++; //no of player
                    String pfullname=rs.getString("fullname3");
                    System.out.print("Athlete " + sum +": " + pfullname + "\n");
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
        } 
        if(sum==18) { return; } //limit 18 players
        System.out.println("Your team has " +sum +" players.");
        System.out.print("Do you want to add a new player?(y/n)\n");
        Scanner in = new Scanner(System.in);
        char x='a';
        while(x!='y' && x!='n' && x!='Y' && x!='N') {
            System.out.print("Please press (y/n)\n");
            x = in.next().charAt(0);            
        }
        if(x=='N' || x=='n') { return; }
        
        //add a new player
        Scanner in1 = new Scanner(System.in);
        System.out.print("His information \nFullname: ");
        String namea = in1.nextLine();
        System.out.print("Age: ");
        int agea = in1.nextInt();
        Scanner in2 = new Scanner(System.in);
        System.out.print("Biography: ");
        String bioa = in2.nextLine();
        
        //insert in db with SQL commands the new player 
        try {
            Statement stmt1 = conn.createStatement();
            stmt1.executeUpdate("INSERT INTO Players VALUES (0,"+namea+",'"
                    +agea+"','"+bioa+"')");
            //Statement stmt2 = conn.createStatement();
            sql="SELECT fullname3 FROM Players WHERE fullname3='"+namea+"'";
            ResultSet rs2=stmt1.executeQuery(sql);
            System.out.print("Insertion completed.\n");
        } catch (SQLException ex) {
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
}

