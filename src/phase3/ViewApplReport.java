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
 * Servlet implementation class ViewApplReport
 */
/*
 * Servlet responsible for displaying the Application Report to admins
 * URL: /app_report
 */
public class ViewApplReport extends HttpServlet {
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
		out.print("<head><title>Application Report</title></head>");
    	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
    	out.print("<h2>Application Report</h2><br/>");
    	
    	Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
            
            // total applications, total accepted
            String query = "SELECT (SELECT count(*) FROM APPLY) as total, (SELECT count(*) FROM APPLY WHERE Status=?) as accept";
            stmt = conn.prepareStatement(query);
            stmt.setString(1,"ACCEPTED");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int total = rs.getInt("total");
            int accept = rs.getInt("accept");
            rs.close();
            out.print("<p>"+total+" applications in total, accepted "+accept+" applications</p><br/>");
            
	    	out.print("<table bgcolor='#FFDDFF' align='center'>");	// frame='box'
			out.print("<thead><tr><th style='border:2px solid black;' bgcolor='#FF88FF'>Project</th><th style='border:2px solid black;' bgcolor='#FF88FF'>#of Applicants</th><th style='border:2px solid black;' bgcolor='#FF88FF'>accept rate</th><th style='border:2px solid black;' bgcolor='#FF88FF'>top 3 major</th></tr></thead>");
			out.print("<tbody>");
			
			String pname;
            query="SELECT PName, count(*) AS total, SUM(CASE WHEN Status='ACCEPTED' THEN 1 ELSE 0 END)*100/count(*) AS rate FROM APPLY GROUP By PName ORDER BY rate DESC";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            int rate;
            String query1,major=null;
            PreparedStatement stmt1;
            ResultSet rs1;
            while (rs.next()){
                pname=rs.getString("PName");
                total=rs.getInt("total");
                rate=rs.getInt("rate");
                query1="SELECT Major, count(*) as count FROM APPLY,USER WHERE StudentName=Username AND PName=? GROUP BY Major ORDER BY count DESC LIMIT 3";
                stmt1 = conn.prepareStatement(query1);
                stmt1.setString(1,pname);
                rs1 = stmt1.executeQuery();
                if(rs1.next()){
                	major = rs1.getString("Major");
                }
                while (rs1.next()){
                    major += "/"+rs1.getString("Major");
                }
                out.print("<tr>");
	           	out.print("<td bgcolor='#FFAAFF'>"+ pname +"</td><td bgcolor='#FFAAFF'>"+ total +"</td><td bgcolor='#FFAAFF'>"+ rate+"%" +"</td><td bgcolor='#FFAAFF'>"+ major +"</td>");				  
	            out.print("</tr>");
                rs1.close();
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
