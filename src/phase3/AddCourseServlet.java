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
 * Servlet implementation class AddCourseServlet
 */
/*
 Servlet for adding courses
*/
public class AddCourseServlet extends HttpServlet {
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
		out.println("<head><script type='text/javascript' src='addCatFunc.js'></script><title>Add Course</title></head>");
		
		out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
		out.print("<h2>Add Course</h2>");
		out.print("<form action='/SLS_cs4400/add_course' method='POST'>");
		out.print("<input type='hidden' name='adminname' value='"+ adminName +"'/>");
		out.print("Course Number:&emsp;&emsp;<input type='text' name='cnumber' required/><br/><br/>");
		out.print("Course Name:&emsp;&emsp;<input type='text' name='cname' required/><br/><br/>");
		out.print("Instructor:&emsp;&emsp;<input type='text' name='instructor' required/><br/><br/>");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");

		  out.print("Designation:&emsp;&emsp;<select name='designation'>");
		  String query = "SELECT Name FROM DESIGNATION";
		  stmt = conn.prepareStatement(query);
		  ResultSet rs = stmt.executeQuery();
		  while(rs.next()){
		    out.print("<option value='"+rs.getString("Name")+"'>"+rs.getString("Name"));
		  }
		  out.print("</select><br/><br/>");  
		  out.print("<p id='catParagraph'>");
		  out.print("Category:&emsp;&emsp;<select id='addCat' name='category'>"); 
		  query = "SELECT CatName FROM CATEGORY";
		  stmt = conn.prepareStatement(query);
		  rs = stmt.executeQuery();
		  while(rs.next()){
			    out.print("<option value='"+rs.getString("CatName")+"'>"+rs.getString("CatName"));
			  }
		  out.print("</select>&emsp;");
		  out.print("</p>");
		  out.println("<button type='button' id='addCatButton' onclick='return myFunction(this.id)'>Add a Category</button>");
		  out.print("<br/><br/>");
		  rs.close();
		} catch(Exception e) {
			   System.err.println("Exception: "+e.getMessage());
		} finally {
		   try {
			if(conn != null)
			  conn.close();
		   } catch(SQLException e) {}
		}  
		out.print("Estimated # of students:&emsp;&emsp;<input type='number' name='estStudents' required/><br/><br/>");
		out.print("<input type='submit' value='Submit'/><br/>");
		out.print("</form>");
		out.print("<button id='backButton' onclick='window.history.back();'>Back</button>");
		out.print("</div></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String adminName = request.getParameter("adminname");
		String cnumber = request.getParameter("cnumber");
		String cname = request.getParameter("cname");
		String instructor = request.getParameter("instructor");
		String designation = request.getParameter("designation");
		String estStudents = request.getParameter("estStudents");
		
		String[] categories = request.getParameterValues("category");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
		  
		  String query = "INSERT INTO COURSE VALUES(?,?,?,?,?)";
		  stmt = conn.prepareStatement(query);
		  stmt.setString(1, cname);
		  stmt.setString(2, cnumber);
		  stmt.setString(3, designation);
		  stmt.setString(4, instructor);
		  stmt.setString(5, estStudents);
		  stmt.executeUpdate();
		  
		  stmt.clearParameters();
		  query = "INSERT INTO COURSE_CAT VALUES(?,?)";
		  stmt = conn.prepareStatement(query);
		  for(String s : categories){
			  stmt.setString(1, cname);
			  stmt.setString(2, s);
			  stmt.executeUpdate();
			  stmt.clearParameters();	
		  }

		} catch(Exception e) {
			   System.err.println("Exception: "+e.getMessage());
		} finally {
		   try {
			 if(conn != null)
		  conn.close();
		   } catch(SQLException e) {}
		} 
		response.sendRedirect("/SLS_cs4400/admin_view.jsp?username="+adminName);
		//request.getRequestDispatcher("/WEB-INF/admin_view.jsp").forward(request, response);
	}
}