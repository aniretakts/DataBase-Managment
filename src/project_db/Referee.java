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

/**
 *
 * @author katerina
 */
public class Referee {
    

    static final String URL = "jdbc:mysql://localhost:3306/db1"; 
    static final String USER = "root";
    static final String PASS = "";
    public static void showInfo(String fullname) throws SQLException {
        String sql, sql1;
        Connection conn = null;
        Statement stmt = null;
        int flag=1;
        
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            System.out.println("Connecting to database...\n") ;
            conn = DriverManager.getConnection(URL,USER,PASS);
			
            //check is the name exists in the db and if it is a referee
            stmt = conn.createStatement();                       
            sql="SELECT refereeid FROM Referee " +
                "WHERE fullname5='" + fullname + "'" ;
            ResultSet rs = stmt.executeQuery(sql);
                    
            //if that name exists continue here...
            if(rs.next()) {
                int rid = rs.getInt("refereeId");              
                System.out.printf("Hello referee!\n The information you need are displayed below :\n");

                //find the games of that referee
                Statement stmt2 = conn.createStatement();
                sql="SELECT gameID,refereeID FROM GameRefs WHERE refereeID=" +rid;
                ResultSet rs2 = stmt2.executeQuery(sql);                   
		while(rs2.next()) {
                    int matchid = rs2.getInt("gameID");  
                    
                    try{
                        //find game info
                        Statement stmt1 = conn.createStatement();
                        sql1="SELECT gdate,ghour,field FROM Games WHERE Games.gameId=" + matchid;                       
                        ResultSet rs1 = stmt1.executeQuery(sql1);
                        while(rs1.next()) {
                            String day = rs1.getDate("gdate").toString();                        
                            String hour = rs1.getTime("ghour").toString(); 
                            int courtid=rs1.getInt("field");

                            //compare the current date with the game date. With the flag I check if i exists and then follows the appropriate message
                            Date date = new Date();
                            String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
                            if(day.compareTo(modifiedDate) < 0) { 
                                    flag=0;
                                    continue; 
                            } 
                            flag=1;
						

                            System.out.print("\nDay: " + day + "\nTime: " + hour);

                            
                            String sql2="SELECT town, field_name FROM Fields WHERE Fields.field_name=" +courtid;
                            Statement stmt3 = conn.createStatement();
                            ResultSet rs3=stmt3.executeQuery(sql2);
                            if(rs3.next()) {
                                String fname=rs3.getString("field_name");
                                String ftown=rs3.getString("town");
								System.out.print("\n Field: " + fname + "\n Town: " + ftown);
                            }
						
                            
                            Statement stmt4 = conn.createStatement();
                            Statement stmt5 = conn.createStatement();
                            String sql4="SELECT gteamB, gteamA FROM Games WHERE gameId=" +matchid;
                            ResultSet rs4=stmt4.executeQuery(sql4); 
                            ResultSet rs5=stmt5.executeQuery(sql4); 
                            int i=1;                    
                            while(rs4.next()) {
                                String teamnameA=rs4.getString("gteamA");
                                String teamnameB=rs5.getString("gteamB");
								System.out.print("\nTeam " + i + ": " + teamnameA);
								System.out.print("\nTeam " + i + ": " + teamnameB);
								i++;
                            } 

                        }
			stmt1.close();                       
                    } catch(SQLException s) {
                        System.out.println("SQL code does not execute.\n"); 
                    }
                } 
            } else { System.out.print("Your ID wasn't found.\n"); }
            stmt.close();           
            conn.close(); 
        } catch(SQLException | ClassNotFoundException se){ 
            System.out.print(se.getMessage() + "\n"); 
        }    
        if(flag==0) { System.out.print("You don't have any upcoming game.\n"); }
        System.out.println("Goodbye! Have a nice day!\n");              
    }      
}

