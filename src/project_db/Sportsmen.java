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
public class Sportsmen {

    //the db it will connect
    static final String URL = "jdbc:mysql://localhost:3306/db1";
    static final String USER = "root";
    static final String PASS = "";
    
    public static void showInfo(String fname) {
        String sql, sql1;
        Connection conn = null;
        Statement stmt = null;
        int flag=1;
        
        try {
            //the connection
            Class.forName("com.mysql.jdbc.Driver"); 
            conn = DriverManager.getConnection(URL,USER,PASS);
			
            //check if the name exists and if it's fan-sportsman
            stmt = conn.createStatement();                       
            sql="SELECT fullname FROM Sportsmen " +
                    "WHERE fullname='" + fname ;
            ResultSet rs = stmt.executeQuery(sql);
                        
            //if that name exists continue here...
            if(rs.next()) {
                System.out.printf("Hello fan!\n");

                
                System.out.print("1. Information about a past game.\n");
                System.out.print("2. Information about an upcoming game.\n");
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
                    case 1: past(conn);
                        break;
                    case 2: upcoming(conn);
                            break;
                    default: break;
                }  
           	//if that name doesn't exist continue here...        
            } else { System.out.println("That name wasn't found."); }
            stmt.close();
            conn.close(); 
        } catch(SQLException | ClassNotFoundException se){ 
            System.out.print(se.getMessage() + "\n"); 
        }      
    }
    

    //info for past games
    static void past(Connection conn) {
        String sql;
        try {
            //euresi ton agonon pou exoun ginei kai ton aparaititon stoixeion
            Statement stmt = conn.createStatement();
            sql="SELECT gameId,gdate,ghour FROM Games WHERE gdate<CURDATE()";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
		String day = rs.getDate("gdate").toString();                        
		String time = rs.getTime("ghour").toString(); 
                int matchid=rs.getInt("gameId");               
                System.out.println("\n\nThat game happened on: \nDay: "+day+"\nTime: "+time);
                
                //find the teams of the game and the scored goals for each team
                //dilosi allou statement afou einai emfoleumeno mesa sto proto
                String sql1="SELECT score,gteamA, gteamB FROM Games WHERE gameId=" +matchid;
                Statement stmt1 = conn.createStatement();
                ResultSet rs1 = stmt1.executeQuery(sql1);
                while(rs1.next()) {
                    int goal=rs1.getInt("score");
                    int tnames=rs1.getInt("gteamA" + "gteamB");
                    System.out.println("\n"+ tnames + ": " + goal);
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    //info for upcoming games
    static void upcoming(Connection conn) {
        String sql;
        try {
            Statement stmt = conn.createStatement();
            sql="SELECT gameId,gdate,ghour FROM Games WHERE gdate>CURDATE()";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
				String day = rs.getDate("gdate").toString();                        
				String time = rs.getTime("ghour").toString(); 
				int courtname=rs.getInt("field");
				int gid=rs.getInt("gameId");               
				System.out.println("\n\nThat game will happen on:\nDay: " + day + "\nTime: " + time);
                
				//field the game will be held
				String sql1="SELECT field_name, town FROM Fields INNER JOIN Games ON Games.field=Fields.field_name WHERE field_name=" + courtname;
				Statement stmt1 = conn.createStatement();
				ResultSet rs1=stmt1.executeQuery(sql1);
				if(rs1.next()) {
                    String fname=rs1.getString("field_name");
                    String ftown=rs1.getString("town");
                    System.out.println("\nField: " + fname + "\nTown: " + ftown);
		}
                
                //referees of the game
                
                System.out.println("\nHere are the referees of the game:");
                sql1="SELECT fullname5 FROM Referee " +
                		"INNER JOIN GameRefs ON Referee.refereeId=GameRefs.refereeID " +
                		"INNER JOIN Games ON Games.gameId=GameRefs.gameID" +
                        "WHERE gameId=" + gid;
		stmt1 = conn.createStatement();
		rs1=stmt1.executeQuery(sql1);
                while(rs1.next()) {
                    String fname=rs1.getString("fullname5");
                    System.out.println("Fullname: " + fname );
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

