package phase3;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import java.sql.*;
/*
import java.util.List;
import java.util.LinkedList;
*/

/**
 * Servlet implementation class LoginServlet
 */
// @WebServlet(description = "servlet handling login page", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String pw=null; String ut = null;
		try {
		  Class.forName("com.mysql.jdbc.Driver").newInstance();
		  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
		  // DEBUG
		 /*
		  if(!conn.isClosed()) 
		    System.out.println("Successfully connected to " + "MySQL server using TCP/IP");
		  */
		// set username and password
		  String username = request.getParameter("username");
		  String password = request.getParameter("password");
		 
		  String query = "SELECT Password,UserType FROM USER WHERE Username=?";
		  stmt = conn.prepareStatement(query);
		  stmt.setString(1, username);
		  
		  ResultSet rs = stmt.executeQuery();
		  while(rs.next()){
		    pw = rs.getString("Password"); 
		    ut = rs.getString("UserType");
		  }
		  if(password.equals(pw)){
			// enter main page if student
			  if(ut.equalsIgnoreCase("s")==true){
				    request.getRequestDispatcher("/main").forward(request, response);
				    // response.sendRedirect("/WEB-INF/main.jsp?Username="+username);
				    
			  } else {
				  // enter admin page
				    request.getRequestDispatcher("/admin_view.jsp").forward(request, response);
				  // response.sendRedirect("admin_view.jsp?Username="+username);
			  }
			  
		  } else {
			// error message
			response.setContentType("text/html");  
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body bgcolor='33BEFF'>");
			out.println("<p>invalid login credentials</p>");
			out.println("</body>");
			out.println("</html>");
			
		  }
		  rs.close();
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
