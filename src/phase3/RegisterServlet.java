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
 * Servlet implementation class RegisterServlet
 */
/*
 Servlet handling input from/output to register page
*/
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String username=null,password=null,email=null;
		try {
			  Class.forName("com.mysql.jdbc.Driver").newInstance();
			  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
			  username = request.getParameter("username");
			  password = request.getParameter("password");
			  email = request.getParameter("email");
			  
			  String query = "SELECT Count(*) AS ctr FROM USER WHERE Email=?";
			  stmt = conn.prepareStatement(query);
			  stmt.setString(1, email);
			  
			  ResultSet rs = stmt.executeQuery();
			    rs.next();
			    int count = rs.getInt("ctr"); 
			    stmt.clearParameters();
			    if(count==0){
			    	// insert the tuple
			    	// control shift to LOGIN page
			    	query = "INSERT INTO USER VALUES(?,?,?,NULL,NULL,?)";
			    	stmt = conn.prepareStatement(query);
			    	stmt.setString(1, username);
			    	stmt.setString(2, password);
			    	stmt.setString(3, email);
			    	stmt.setString(4, "S");
			    	int entered = stmt.executeUpdate();
			    	if(entered==1)
			    		response.sendRedirect("welcome.html");
			    } else {
			    	// display error message
			    	response.setContentType("text/html");  
					PrintWriter out = response.getWriter();
					out.println("<html>");
					out.println("<body bgcolor='33BEFF'>");
					out.println("<p>User account already exists. Please login to access application</p>");
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
