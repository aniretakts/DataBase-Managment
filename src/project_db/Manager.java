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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author katerina
 */
public class Manager {

    static final String URL = "jdbc:mysql://localhost:3306/db1"; 
    static final String USER = "root";
    static final String PASS = "";
    public static void showInfo(String mfullname) throws SQLException {
        String sql, sql1, teamname = "";
        Connection conn = null;
        Statement stmt = null;
        int teamid=0;
        
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            System.out.println("Connecting to database...\n") ;
            conn = DriverManager.getConnection(URL,USER,PASS);
			
            stmt = conn.createStatement();                       
            sql="SELECT managerId FROM Manager WHERE fullname2='" + mfullname + "'";
            ResultSet rs = stmt.executeQuery(sql);
                        
            //check is the manager with that name exists
            if(rs.next()) {
                int pid = rs.getInt("managerId");              
                System.out.printf("Hello manager!\nYour team is:\n");

                //find the team
                Statement stmt1 = conn.createStatement();
                sql="SELECT team2 FROM Manager WHERE managerId=" + pid;               
                ResultSet rs1 = stmt1.executeQuery(sql);
                if(rs1.next()) { 
                    teamname=rs1.getString("team2"); 
                }
                System.out.print(teamname + "\n");
                
                System.out.print("1. Tickets Information.\n");
                System.out.print("2. Players Information.\n");
                System.out.print("What is your option? (1,2):\n");

            
                Scanner in = new Scanner(System.in);
                int num=0;
                while(num!=1) {
                    System.out.println("Press 1 or 2:\nPress only 1");
                    try{ num = in.nextInt(); }
                    catch(Exception e) { 
                        System.out.println("Please select 1 or 2.\n");
                        System.exit(0); 
                    }
                }
            
                switch (num) {
                    case 1: tickets(teamid,conn);
                        break;
                  //case 2: athletes(teamid,conn);
                  //        break;
                    default: break;
                }
            } else { System.out.print("The name wasn't found.\n"); }
            stmt.close();
            conn.close();                                         
        } catch(SQLException | ClassNotFoundException se){ 
            System.out.print(se.getMessage() + "\n"); 
        }  
    }
    
    static void tickets(int tid, Connection conn) {
        String sql;
        int sum=0,j=0;
        try {
            //no of tickets
            Statement stmt = conn.createStatement();
            sql="SELECT count(*) AS sum FROM Season  INNER JOIN Ticket_Owner ON Ticket_Owner.tID=Season.tID2 INNER JOIN Sportsmen ON Sportsmen.spId=Ticket_Owner.spID WHERE Sportsmen.team4="+tid;
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                int i=rs.getInt("sum"); //number 
                System.out.println("The number of season tickets sold is: " + i);
                sum = sum + i*200; //money earned from season tickets
            }
            
            //simple tickets. I have to find first the games his team played
            Statement stmt1 = conn.createStatement();
            sql="SELECT gameId FROM Games WHERE gteamA=" +tid + "OR gteamB=" +tid ;
            ResultSet rs1 = stmt1.executeQuery(sql);   
            while(rs1.next()) {
                int matchid = rs1.getInt("gameId");
                //nested sql stmt - upologismos ton aplon eisitirion
                Statement stmt2 = conn.createStatement();
                String sql1="SELECT count(*) AS sum FROM Games INNER JOIN Tickets ON Tickets.Game=Games.gameId WHERE gameId=" + matchid + " AND gdate<CURDATE()";
                ResultSet rs2 = stmt2.executeQuery(sql1);
                if(rs2.next()) {
                    int i=rs2.getInt("sum"); //plithos tou sugkekrimenou agona
                    j=j+i; //athroisi gia olous tous agones pou epaikse i omada
                    sum = sum + i*10; //esoda apo auta
                }
            }    
            System.out.println("Number of simple tickets sold: " + j);
            System.out.println("Total income of tickets: " + sum);   
                
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}


