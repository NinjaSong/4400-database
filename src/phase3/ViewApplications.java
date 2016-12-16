package phase3;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewApplications
 */
/*
 * Servlet responsible for displaying all applications to admins
 * URL: /appl_view
 */
@WebServlet("/ViewApplications")
public class ViewApplications extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String adminName = request.getParameter("adminname");
		if(adminName==null){
			response.sendRedirect("/SLS_cs4400/welcome.html");
			return;
		}
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=ISO-8859-1"); 
		out.println("<!DOCTYPE html>"); 
		out.println("<html>");
		out.print("<head><title>View Applications</title></head>");
    	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
    	out.print("<h2>Applications</h2><br/>");
    	out.print("<form action='/SLS_cs4400/appl_view' method='POST'>");
    	out.print("<input type='hidden' name='adminname' value='"+adminName+"'/>");
    	out.print("<table bgcolor='#FFDDFF' align='center'>");	// frame='box'
		out.print("<thead><tr><th style='border:2px solid black;' bgcolor='#FF88FF'>Project</th><th style='border:2px solid black;' bgcolor='#FF88FF'>Applicant Major</th><th style='border:2px solid black;' bgcolor='#FF88FF'>Applicant Year</th><th style='border:2px solid black;' bgcolor='#FF88FF'>Status</th><th style='border:2px solid black;' bgcolor='#FF88FF'>Username</th></tr></thead>");
		out.print("<tbody>");
		
    	Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
            
            String pname,status,username,major,year;

            String query = "SELECT PName, Status, Username, Major,Year FROM USER,APPLY WHERE Username=StudentName";
            stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                pname=rs.getString("PName");
                status=rs.getString("Status");
                username=rs.getString("Username");	
                major=rs.getString("Major");
                year=rs.getString("Year");
                out.print("<tr>");
                if(status.equals("PENDING")){
                	out.print("<td bgcolor='#FFAAFF' align='left'><input type='radio' name='a_r' value='"+pname+" "+username+"'/>"+ pname +"</td><td bgcolor='#FFAAFF'>"+ major +"</td><td bgcolor='#FFAAFF'>"+ year +"</td><td bgcolor='#FFAAFF'>"+ status +"</td><td bgcolor='#FFAAFF'>"+ username +"</td>");
                } else {
                	out.print("<td bgcolor='#FFAAFF' align='left'>"+ pname +"</td><td bgcolor='#FFAAFF'>"+ major +"</td><td bgcolor='#FFAAFF'>"+ year +"</td><td bgcolor='#FFAAFF'>"+ status +"</td><td bgcolor='#FFAAFF'>"+ username +"</td>");
                }
	           			  
	            out.print("</tr>");
                
            }
            out.print("</tbody></table><br/>");
            
            out.print("<input type='submit' name='accept' value='Accept'/>&emsp;");
            out.print("<input type='submit' name='reject' value='Reject'/>");
            out.print("</form>");
            rs.close();

            
        } catch(Exception e) {
            System.err.println("Exception: "+e.getMessage());
        } finally {
            try {
                if(conn != null)
                    conn.close();
            } catch(SQLException e) {}
        }
        out.print("<br/><br/>");
    	out.print("<button onclick='window.history.back();'>Back</button>");
    	out.print("</div></body></html>");
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String adminName = request.getParameter("adminname");
		String a_r = request.getParameter("a_r");
		int splitIndex = a_r.lastIndexOf(' ');
		String pname = a_r.substring(0, splitIndex+1);
		String username = a_r.substring(splitIndex+1);
		String change=null;
		if(request.getParameter("accept")!=null)
			change="ACCEPTED";
	
		if(request.getParameter("reject")!=null)
			change="REJECTED";
		
		Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
            
			String query="UPDATE APPLY SET Status=? WHERE PName=? AND StudentName=?";
	        stmt = conn.prepareStatement(query);
	        stmt.setString(1,change);
	        stmt.setString(2,pname);
	        stmt.setString(3,username);
	        stmt.executeUpdate();
	        
	        response.sendRedirect("/SLS_cs4400/appl_view?adminname="+adminName);
        } catch(Exception e) {
            System.err.println("Exception: "+e.getMessage());
        } finally {
            try {
                if(conn != null)
                    conn.close();
            } catch(SQLException e) {}
        }
        
        
        
	}

}
