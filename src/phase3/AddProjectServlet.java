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
 * Servlet implementation class AddProjectServlet
 */
/*
	Servlet for adding projects
*/
public class AddProjectServlet extends HttpServlet {
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
		out.println("<head><script type='text/javascript' src='addCatFunc.js'></script><title>Add Project</title></head>");
		out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
		out.print("<h2>Add Project</h2>");
		out.print("<form action='/SLS_cs4400/add_project' method='POST'>");		
		out.print("<input type='hidden' name='adminname' value='"+ adminName +"'/>");
		out.print("Project Name:&emsp;&emsp;<input type='text' name='pname' required/><br/><br/>");
		out.print("Advisor Name:&emsp;&emsp;<input type='text' name='advisor_name' required/><br/><br/>");
		out.print("Advisor Email:&emsp;&emsp;<input type='text' name='advisor_email' required/><br/><br/>");
		out.print("Description:&emsp;&emsp;<input type='text' name='description' required/><br/><br/>");
		
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
		  
		  out.print("Major Requirement:&emsp;&emsp;ONLY&nbsp;<select name='major'> ");
		  query = "SELECT MName FROM MAJOR";
		  stmt = conn.prepareStatement(query);
		  rs = stmt.executeQuery();
		  out.print("<option value='None'> None");
		  while(rs.next()){
		    out.print("<option value='"+rs.getString("MName")+"'>"+rs.getString("MName"));
		  }
		  out.print("</select><br/><br/>"); 

		  out.print("Department Requirement:&emsp;&emsp;ONLY&nbsp;<select name='department'> ");
		  query = "SELECT DName FROM DEPARTMENT";
		  stmt = conn.prepareStatement(query);
		  rs = stmt.executeQuery();
		  out.print("<option value='None'> None");
		  while(rs.next()){
		    out.print("<option value='"+rs.getString("DName")+"'>"+rs.getString("DName"));
		  }
		  out.print("</select><br/><br/>");
		
		  rs.close();		 
		  
		} catch(Exception e) {
			   System.err.println("Exception: "+e.getMessage());
		} finally {
		   try {
			if(conn != null)
			  conn.close();
		   } catch(SQLException e) {}
		}
		// Year Requirement
		out.print("Year Requirement:&emsp;&emsp;ONLY&nbsp;<select name='year'/>");
		out.print("<option value='None'> None");
		out.print("<option value='freshman'> freshman");
		out.print("<option value='sophomore'> sophomore");
		out.print("<option value='junior'> junior");
		out.print("<option value='senior'> senior");
		out.print("</select><br/><br/>");
		
		out.print("Estimated # of students:&emsp;&emsp;<input type='number' name='estStudents' required/><br/><br/>");
		out.print("<input type='submit' value='Submit'/><br/>");
		out.print("</form>");
		out.print("<button onclick='window.history.back();'>Back</button>");
		out.print("</div></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String adminName = request.getParameter("adminname");
		String pname = request.getParameter("pname");
		String advisor_name = request.getParameter("advisor_name");
		String advisor_email = request.getParameter("advisor_email");
		String description = request.getParameter("description");
		String designation = request.getParameter("designation");
		
		String[] categories = request.getParameterValues("category");
		
		String major = request.getParameter("major");
		String department = request.getParameter("department");
		String year = request.getParameter("year");

		String estStudents = request.getParameter("estStudents");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
		  
		  String query = "INSERT INTO PROJECT VALUES(?,?,?,?,?,?)";
		  stmt = conn.prepareStatement(query);
		  stmt.setString(1, pname);
		  stmt.setString(2, estStudents);
		  stmt.setString(3, designation);		  
		  stmt.setString(4, advisor_name);
		  stmt.setString(5, advisor_email);
		  stmt.setString(6, description);
		  stmt.executeUpdate();
		  
		  stmt.clearParameters();
		  query = "INSERT INTO PROJECT_CAT VALUES(?,?)";
		  stmt = conn.prepareStatement(query);
		  for(String s : categories){
			  stmt.setString(1, pname);
			  stmt.setString(2, s);
			  stmt.executeUpdate();
			  stmt.clearParameters();	
		  }
		  
		  if(!major.equals("None")){
			  stmt.clearParameters();
			  query = "INSERT INTO PROJECT_REQ VALUES(?,?)";
			  stmt = conn.prepareStatement(query);
			  stmt.setString(1, pname);
			  stmt.setString(2, major);
			  stmt.executeUpdate();
			}

		  if(!department.equals("None")){
		  	  stmt.clearParameters();
		  	  query = "INSERT INTO PROJECT_REQ VALUES(?,?)";
		  	  stmt = conn.prepareStatement(query);
		  	  stmt.setString(1, pname);
			  stmt.setString(2, department);
			  stmt.executeUpdate();
		    }

		  if(!year.equals("None")){
			  stmt.clearParameters();
			  query = "INSERT INTO PROJECT_REQ VALUES(?,?)";
			  stmt = conn.prepareStatement(query);
			  stmt.setString(1, pname);
			  stmt.setString(2, year);
			  stmt.executeUpdate();
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
		// request.getRequestDispatcher("/WEB-INF/admin_view.jsp").forward(request, response);
		
	}
}
