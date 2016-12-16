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
 * Servlet implementation class EditProfileServlet
 */
/*
 * Servlet handling Edit profile page of a Student
 */
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");	
		String major = request.getParameter("major");
		if(username==null){
			response.sendRedirect("/SLS_cs4400/welcome.html");
			return;
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=ISO-8859-1"); 
		out.println("<!DOCTYPE html>"); 
		out.println("<html>");
		out.print("<head><title>Edit Profile</title></head>");
		out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
		out.print("<h2>Edit Profile</h2><br/>");
		
		out.print("<form name='form1' action='editProfile' method='GET'>");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
			// hidden username
			out.print("<input type='hidden' name='username' value='"+username+"'/>");
			out.print("Major:&emsp;&emsp;<select name='major' onchange='javascript:this.form.submit();'>");
			String query = "SELECT MName,DName FROM MAJOR";
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			if(major==null || major.equals("None")) {
				out.print("<option value='None' selected='selected'> Choose major");
				while(rs.next()){
					out.print("<option value='"+rs.getString("MName")+"'>"+rs.getString("MName"));
				}
			} else {
				out.print("<option value='None'> Choose major");
				while(rs.next()){
					String temp = rs.getString("MName");
					if(temp.equals(major))
						out.print("<option value='"+rs.getString("MName")+"' selected='selected'>"+rs.getString("MName"));
					else
						out.print("<option value='"+rs.getString("MName")+"'>"+rs.getString("MName"));
				}
			}
			out.print("</select><br/><br/>");  
			
			out.print("Department:&emsp;");
			String department = null;
			if(major!=null){
				query = "SELECT DName FROM MAJOR WHERE MName=?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1,major);
				rs = stmt.executeQuery();
				if(rs.next())
					department = rs.getString("DName");
				out.print(department);
			
			}	
			out.print("<br/>");
			rs.close();
		} catch(Exception e) {
		   System.err.println("Exception: "+e.getMessage());
		} finally {
		   try {
			if(conn != null)
			  conn.close();
		   } catch(SQLException e) {}
		} 
		out.print("</form><br/>");
		
		out.print("<form action='editProfile' method='POST'>");
		// hidden username
		out.print("<input type='hidden' name='username' value='"+username+"'/>");
		// hidden major
		if(major!=null && !major.equals("None")){
			out.print("<input type='hidden' name='major' value='"+major+"'/>");
		}
		out.print("Year:&emsp;&emsp;<select name='year'/>");
		out.print("<option value='freshman'> freshman");
		out.print("<option value='sophomore'> sophomore");
		out.print("<option value='junior'> junior");
		out.print("<option value='senior'> senior");
		out.print("</select><br/><br/>");
		out.print("<input type='submit' value='Submit'/>");	  
		out.print("</form>");
		
		out.print("<button id='backButton' onclick='window.history.back();'>Back</button>");
		out.print("</div></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String major = request.getParameter("major");
		String year = request.getParameter("year");
		
		if(major==null || major.equals("None") || year==null)
			response.sendRedirect("/SLS_cs4400/editProfile?username="+username);
		else {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
		  
		  String query = "UPDATE USER SET Major=?,Year=? WHERE Username=?";
		  stmt = conn.prepareStatement(query);
		  stmt.setString(1, major);
		  stmt.setString(2, year);
		  stmt.setString(3, username);
		  stmt.executeUpdate();
		} catch(Exception e) {
			   System.err.println("Exception: "+e.getMessage());
		} finally {
		   try {
			 if(conn != null)
			  conn.close();
		   } catch(SQLException e) {}
		}
		response.sendRedirect("me.jsp?username="+username);
	}
	}	
}
