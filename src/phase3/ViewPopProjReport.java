package phase3;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class ViewPopProjReport
 */
/*
 * Servlet responsible for displaying popular project report for admins
 * URL: /pop_proj_report
 */

public class ViewPopProjReport extends HttpServlet {
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
		out.print("<head><title>Popular Project</title></head>");
    	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
    	out.print("<h2>Popular Project</h2><br/>");
		
    	out.print("<table bgcolor='#FFDDFF' align='center'>");	// frame='box'
		out.print("<thead><tr><th style='border:2px solid black;' bgcolor='#FF88FF'>Project</th><th style='border:2px solid black;' bgcolor='#FFAAFF'>#of Applicants</th></tr></thead>");
		out.print("<tbody>");
		
    	Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
    
            // String pname,count;

            String query = "SELECT PName, count(*) as Count FROM APPLY GROUP BY PName ORDER BY Count DESC LIMIT 10;";
            stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            	 out.print("<tr>");
            	 // pname=rs.getString("PName");
                 // count=rs.getString("Count");
            	 out.print("<td bgcolor='#FF88FF'>"+ rs.getString("PName")+"</td><td bgcolor='#FFAAFF'>"+ rs.getString("Count")+"</td>");				  
            	 out.print("</tr>");
            }
            out.print("</tbody></table>");
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
		// TODO Auto-generated method stub
	}

}