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
 * Servlet implementation class MyAppsServlet
 */
/*
 * Servlet handling student's applications view
 * URL = /myApps
 */

public class MyAppsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String username = request.getParameter("username");
		if(username==null){
			response.sendRedirect("/SLS_cs4400/welcome.html");
			return;
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=ISO-8859-1"); 
		out.println("<!DOCTYPE html>"); 
		out.println("<html>");
		out.print("<head>");
		out.print("<title>My Applications</title>"); 
	
		out.print("<script type='text/javascript' src='jquery-latest.js'></script>");
		out.print("<script type='text/javascript' src='jquery.tablesorter.js'></script>");
		out.print("<script type='text/javascript' src='jquery.metadata.js'></script>");
		out.print("<script type='text/javascript' src='jquery.tablesorter.min.js'></script>");
		out.print("<link rel='shortcut icon' href='http://d15dxvojnvxp1x.cloudfront.net/assets/favicon.ico'/>");
		out.print("<link rel='icon' href='http://d15dxvojnvxp1x.cloudfront.net/assets/favicon.ico'/>");
		out.print("<link rel='stylesheet' type='text/css' media='all' href='css/styles.css'/>");  
		out.print("<script type='text/javascript'>");
		out.print("$(document).ready(function(){ $('#apps').tablesorter(); });");
		out.print("</script>");
		out.print("</head>");
		out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
		out.print("<h2>My Applications</h2><br/>");
		out.print("<table id='apps' class='tablesorter'><thead><tr><th><span>Date</span></th><th><span>Project Name</span></th><th><span>Status</span></th></tr></thead>");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
		  
		  String query = "SELECT ApplDate,PName,Status FROM APPLY WHERE StudentName=?";
		  stmt = conn.prepareStatement(query);
		  stmt.setString(1, username);
		  ResultSet rs = stmt.executeQuery();
		  out.print("<tbody>");
		  while(rs.next()){
			  out.print("<tr>");
			  out.print("<td>"+rs.getString("ApplDate")+"</td>");
			  out.print("<td>"+rs.getString("PName")+"</td>");
			  out.print("<td>"+rs.getString("Status")+"</td>");
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
		out.print("<button id='backButton' onclick='window.history.back();'>Back</button>");
		out.print("</div></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
	}

}