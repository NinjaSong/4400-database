package phase3;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewPCServlet
 */
/*
 * Servlet handling view course/project requests, apply project requests from main page
 * URL: /viewPC
 */

public class ViewPCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=ISO-8859-1"); 
		out.println("<!DOCTYPE html>"); 
		out.println("<html>");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String username = request.getParameter("username");
		
		if(type.equalsIgnoreCase("course")){	// view course
			
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
			  Class.forName("com.mysql.jdbc.Driver").newInstance();
			  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
			  
			  String query = "SELECT * FROM COURSE WHERE CName=?";
			  stmt = conn.prepareStatement(query);
			  stmt.setString(1, name);
			  ResultSet rs = stmt.executeQuery();
			  ResultSet rsCat;
			  while(rs.next()){
				  out.print("<head><title>View Course</title></head>");
				  out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
				  out.print("<h2>"+rs.getString("CNumber")+"</h2>");
				  out.print("Course Name:&emsp;"+rs.getString("CName")+"</br></br>");
				  out.print("Instructor:&emsp;"+rs.getString("Instructor")+"</br></br>");
				  out.print("Designation:&emsp;"+rs.getString("DesigName")+"</br></br>");
				  query = "SELECT CatName FROM COURSE_CAT WHERE CName=?";
			      stmt = conn.prepareStatement(query);
				  stmt.setString(1, rs.getString("CName"));
				  rsCat = stmt.executeQuery();
				  out.print("Category:&emsp;");
				  rsCat.next();
				  out.print(rsCat.getString("CatName"));
				  while(rsCat.next()){
					  out.print(", "+rsCat.getString("CatName"));
				  }
				  rsCat.close();
				  out.print("<br/></br>");
				  out.print("Estimated number of Students:&emsp;"+rs.getString("EstStudents")+"</br></br>");
				  out.print("<button onclick='window.history.back();'>Back</button>");
				  out.println("</div></body></html>");
					
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
			  
		} else {	// view project
			
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
			  Class.forName("com.mysql.jdbc.Driver").newInstance();
			  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
			  
			  String query = "SELECT * FROM PROJECT WHERE PName=?";
			  stmt = conn.prepareStatement(query);
			  stmt.setString(1, name);
			  ResultSet rs = stmt.executeQuery();
			  ResultSet rsCat, rsReq;
			  while(rs.next()){
				  out.print("<head><title>View Project</title></head>");
				  out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
				  out.print("<h2>"+rs.getString("PName")+"</h2>");
				  out.print("Advisor:&emsp;"+rs.getString("AdvisorName")+"&nbsp;("+rs.getString("AdvisorEmail")+")</br></br>");
				  out.print("Description:&emsp;"+rs.getString("Description")+"</br></br>");
				  out.print("Designation:&emsp;"+rs.getString("DesigName")+"</br></br>");
				  query = "SELECT CatName FROM PROJECT_CAT WHERE PName=?";
			      stmt = conn.prepareStatement(query);
				  stmt.setString(1, rs.getString("PName"));
				  rsCat = stmt.executeQuery();
				  out.print("Category:&emsp;");
				  rsCat.next();
				  out.print(rsCat.getString("CatName"));
				  while(rsCat.next()){
					  out.print(", "+rsCat.getString("CatName"));
				  }
				  rsCat.close();
				  out.print("<br/></br>");
				  
				  query = "SELECT Requirement FROM PROJECT_REQ WHERE PName=?";
			      stmt = conn.prepareStatement(query);
				  stmt.setString(1, rs.getString("PName"));
				  rsReq = stmt.executeQuery();
				  out.print("Requirements:&emsp;");
				  if(rsReq.next())	// 0 upto 3 requirements
					  out.print(rsReq.getString("Requirement")+" only");
				  while(rsReq.next()){
					  out.print(", "+rsReq.getString("Requirement")+" only");
				  }
				  rsReq.close();
				  out.print("<br/></br>");
				  
				  out.print("Estimated number of Students:&emsp;"+rs.getString("EstStudents")+"</br></br>");
				  
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
			 
			// apply button code
			out.print("<form action='/SLS_cs4400/viewPC?name="+name+"&username="+username+"' method='POST'>");
			out.print("<input type='submit' name='apply' value='Apply'/>");
			out.print("</form><br/><br/>");
			out.print("<button onclick='window.history.back();'>Back</button>");
			out.print("</div></body></html>");
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*	'APPLY' PSEUDO CODE 
		 * 	check if user has updated both major and year
		 *  if yes, check if user has applied to this project before
		 * 	if no, then check if user meets all requirements (note: requirements are optional so some might not exist)
		 * 	if yes, allow to apply with status 'pending'
		 */
		/*
		 * This method handles the case when 'apply' button is clicked. Username and PName are passed as request parameters.
		 */
		String username = request.getParameter("username");
		String pname = request.getParameter("name");	
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=ISO-8859-1"); 
		out.println("<!DOCTYPE html>"); 
		out.println("<html>");
		
		Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93", "cs4400_Team_93", "bW7TmA2i");
		
            String query;
	        String major=null,year=null;
	        ResultSet rs;
	
	        query = "SELECT Major, Year FROM USER WHERE Username = ?" ;
	        stmt = conn.prepareStatement(query);
	        stmt.setString(1, username);
	        rs = stmt.executeQuery();
	
	        while(rs.next()) {
	            major = rs.getString("Major");
	            year = rs.getString("Year");
	        }
	        // rs.close();
	        
	        if(major == null || year == null){
	        	out.print("<head><title>Application</title></head>");
	        	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
				out.print("<p>Please update your student profile before applying</p><br/>");
				out.print("<form action='/SLS_cs4400/editProfile' method='GET'>");
				out.print("<input type='hidden' name='username' value='"+username+"'/>");
				out.print("<input type='submit' value='Go to Edit Profile'/>");
				out.print("</form><br/>");
				out.print("<button onclick='window.history.back();'>Back</button>");
				out.print("</div></body></html>");
	        }
	        // rs.close();
	        if (major!=null && year!=null){
				boolean isOk=true;
				query="SELECT Status FROM APPLY WHERE StudentName=? AND PName=?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, username);
				stmt.setString(2, pname);
				rs = stmt.executeQuery();
				String status=null;
				while(rs.next()){
				status=rs.getString("Status");
				}
				//rs.close();

				if (status!=null){
					// show error message
					out.print("<head><title>Application</title></head>");
    	        	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
    				out.print("<p>You have already applied to this project before</p><br/>");
    				out.print("<button onclick='window.history.back();'>Back</button>");
    				out.print("</div></body></html>");
					isOk=false;
				}

              if (isOk){
				//String []reqs =new String[2];	// only for major and dept requirements
				//int index=0;
				
				query = "SELECT Requirement FROM PROJECT_REQ WHERE PName=?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, pname);
				rs = stmt.executeQuery();
				//rs.next();
				//reqs[index++]=rs.getString("Requirement");
				String year_req=null;	// no year requirement (initially)
				ArrayList<String> md_req = new ArrayList<String>();
				//String major_req=null;	// no major requirement (initially)
				//String dept_req=null;	// no dept requirement (initially)
				String req=null;
				while(rs.next()){
					req=rs.getString("Requirement");
					if(req.equals("freshman")||req.equals("sophomore")||req.equals("junior")||req.equals("senior")){
						year_req=req;
						if(!year.equals(year_req)){	// year mismatch
							isOk = false;
							out.print("<head><title>Application</title></head>");
	        	        	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
	        				out.print("<p>You do not meet the requirements of this project</p><br/>");
	        				out.print("<button onclick='window.history.back();'>Back</button>");
	        				out.print("</div></body></html>");
	        				
							break;
						}
					} else {	// only for major and dept requirements
						md_req.add(req);
					}

				}
				//rs.close();

				String department=null;
				query="SELECT DName FROM MAJOR WHERE MName =?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, major);
				rs = stmt.executeQuery();
				while(rs.next())
				department=rs.getString("DName");
				//rs.close();

				if(md_req.size()==1){
					String temp = md_req.get(0);
					if(!(major.equals(temp)||department.equals(temp)) && isOk==true){
						isOk = false;
						out.print("<head><title>Application</title></head>");
        	        	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
        				out.print("<p>You do not meet the requirements of this project</p><br/>");
        				out.print("<button onclick='window.history.back();'>Back</button>");
        				out.print("</div></body></html>");
        				
					}
				}
				if(md_req.size()==2){
					String req1 = md_req.get(0);
					String req2 = md_req.get(1);
					if(!(req1.equals(major)||req1.equals(department)||req2.equals(major)||req2.equals(department)) && isOk==true){
						isOk = false;
						out.print("<head><title>Application</title></head>");
        	        	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
        				out.print("<p>You do not meet the requirements of this project</p><br/>");
        				out.print("<button onclick='window.history.back();'>Back</button>");
        				out.print("</div></body></html>");
        				
					}
					
				}
			
            }

		    	if (isOk){
					query="INSERT INTO APPLY VALUES(?,?,?,?)";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, username);
					stmt.setString(2, pname);
					stmt.setString(3, "PENDING");
					stmt.setDate(4,new java.sql.Date(System.currentTimeMillis()));	// sets the current date
					stmt.executeUpdate();
					out.print("<head><title>Application</title></head>");
    	        	out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
    				out.print("<p>You have successfully applied to this project!</p><br/>");
    				out.print("<form action='/SLS_cs4400/main' method='POST'>");
    				out.print("<input type='submit' value='Back to Main Page'/>");
    				out.print("<input type='hidden' name='username' value='"+username+"'/>");
    				//out.print("<button onclick='window.history.back();'>Back</button>");
    				out.print("</form></div></body></html>");
					
				} 

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