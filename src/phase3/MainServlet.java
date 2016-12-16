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
 * Servlet implementation class MainServlet
 */
/*
 * Servlet handling student's main page and search/filter
 * URL: /main
 */

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=null,title=null,designation=null,major=null,year=null,p_c_b=null,query=null;
		String[] categories=null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=ISO-8859-1"); 
		out.println("<!DOCTYPE html>"); 
		out.println("<html>");
		out.print("<head><script type='text/javascript' src='addCatFunc.js'></script><title>Main Page</title></head>");
		out.print("<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>");
		out.print("<h2>Main Page</h2><br/>");
		
		// Username and link to avatar
		username = request.getParameter("username");
		out.print("<p id='user'>"+username+"</p>");
		String url = "/SLS_cs4400/me.jsp?username="+username;
		out.print("<a href='"+url+"'><img src='avatar.png' alt='avatar' style='width:42px;height:42px;'/></a><br/><br/>");
		out.print("<form action='/SLS_cs4400/main' method='POST'>");
		
		// Title view
		title = request.getParameter("title");
		 if(request.getParameter("applyFilter")==null || title==null)
			 out.print("Title&emsp;&emsp;<input type='text' name='title'/><br/><br/>");
		 else {
			 out.print("Title&emsp;&emsp;<input type='text' name='title' value='"+title+"'/><br/><br/>");
		 }
		 
	 try {
		 Class.forName("com.mysql.jdbc.Driver").newInstance();
		 conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
		 // Designation view
		 out.print("Designation&emsp;&emsp;<select name='designation'>");
		 query = "SELECT Name FROM DESIGNATION";
		 stmt = conn.prepareStatement(query);
		 rs = stmt.executeQuery();
		 designation = request.getParameter("designation");
		 if(request.getParameter("applyFilter")==null || designation.equals("None")){
			 out.print("<option value='None' selected='selected'> Please Select");
			 while(rs.next())
			    out.print("<option value='"+rs.getString("Name")+"'>"+rs.getString("Name"));
			 
		 } else {
			 out.print("<option value='None'> Please Select");
			 while(rs.next()){
				if(rs.getString("Name").equals(designation)) 
					out.print("<option value='"+rs.getString("Name")+"' selected='selected' >"+rs.getString("Name"));
				else
					out.print("<option value='"+rs.getString("Name")+"'>"+rs.getString("Name"));
			 }
		 }
		 out.print("</select><br/><br/>"); 
		 
		 // Category(ies) view
		 categories = request.getParameterValues("category");
		 out.print("<p id='catParagraph'>");
		 if(request.getParameter("applyFilter")==null || categories==null){
			  out.print("Category&emsp;&emsp;<select id='addCat' name='category'>"); 
			  query = "SELECT CatName FROM CATEGORY";
			  stmt = conn.prepareStatement(query);
			  rs = stmt.executeQuery();
			  out.print("<option value='None' selected='selected'> Please Select");
			  while(rs.next()){
				    out.print("<option value='"+rs.getString("CatName")+"'>"+rs.getString("CatName"));
				  }
			  out.print("</select>&emsp;");
			  out.print("</p>");
			  out.println("<button type='button' id='addCatButton' onclick='return myFunction(this.id)'>Add a Category</button>");
			  out.print("<br/><br/>");
			  
		 } else {
			 int catLength = categories.length;
			 out.print("Category&emsp;&emsp;<select id='addCat' name='category'>"); 
			  query = "SELECT CatName FROM CATEGORY";
			  stmt = conn.prepareStatement(query);
			  rs = stmt.executeQuery();
			  // First drop-down
			  if(categories[0].equals("None")){
				  out.print("<option value='None' selected='selected'> Please Select");
				  while(rs.next()){
					    out.print("<option value='"+rs.getString("CatName")+"'>"+rs.getString("CatName"));
					  }
				  
			  } else {
				  out.print("<option value='None'> Please Select");
				  while(rs.next()){
					  if(rs.getString("CatName").equals(categories[0])) 
						out.print("<option value='"+rs.getString("CatName")+"' selected='selected' >"+rs.getString("CatName"));
					  else
						out.print("<option value='"+rs.getString("CatName")+"'>"+rs.getString("CatName"));
						   
				  }
			  }
			  rs.close();
			  out.print("</select>&emsp;");	// end of first drop-down
			 for(int ctr=1;ctr<catLength;ctr++){	// if any more left (depends on catLength)
				 out.print("<select name='category'>"); 
				  query = "SELECT CatName FROM CATEGORY";
				  stmt = conn.prepareStatement(query);
				  rs = stmt.executeQuery();
				  if(categories[ctr].equals("None")){
					  out.print("<option value='None' selected='selected'> Please Select");
					  while(rs.next()){
						    out.print("<option value='"+rs.getString("CatName")+"'>"+rs.getString("CatName"));
						  }
					  
				  } else {
					  out.print("<option value='None'> Please Select");
					  while(rs.next()){
						  if(rs.getString("CatName").equals(categories[ctr])) 
							out.print("<option value='"+rs.getString("CatName")+"' selected='selected' >"+rs.getString("CatName"));
						  else
							out.print("<option value='"+rs.getString("CatName")+"'>"+rs.getString("CatName"));
							   
					  }
				  }
				  out.print("</select>&emsp;");
			 }
			 out.print("</p>");
			  out.println("<button type='button' id='addCatButton' onclick='return myFunction(this.id)'>Add a Category</button>");
			  out.print("<br/><br/>");
			  
		 }
		 
		 // Major view
		 out.print("Major&emsp;&emsp;<select name='major'> ");
		 query = "SELECT MName FROM MAJOR";
		 stmt = conn.prepareStatement(query);
		 rs = stmt.executeQuery();
		 major = request.getParameter("major"); 
		 if(request.getParameter("applyFilter")==null || major.equals("None")){
			 out.print("<option value='None' selected='selected'> Please Select");
			 while(rs.next())
			    out.print("<option value='"+rs.getString("MName")+"'>"+rs.getString("MName"));
			 
		 } else {
			 out.print("<option value='None'> Please Select");
			 while(rs.next()){
				if(rs.getString("MName").equals(major)) 
					out.print("<option value='"+rs.getString("MName")+"' selected='selected' >"+rs.getString("MName"));
				else
					out.print("<option value='"+rs.getString("MName")+"'>"+rs.getString("MName"));
			 }
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
	 
		 // Year view
		  String[] yearOptions = {"freshman","sophomore","junior","senior"};
		  year = request.getParameter("year");
		  out.print("Year&emsp;&emsp;<select name='year'/>");
		  if(request.getParameter("applyFilter")==null || year.equals("None")){
				 out.print("<option value='None' selected='selected'> Please Select");
				 for(String itr : yearOptions)
				    out.print("<option value='"+itr+"'>"+itr);
				 
		  } else {
				 out.print("<option value='None'> Please Select");
				 for(String itr : yearOptions){
					if(itr.equals(year)) 
						out.print("<option value='"+itr+"' selected='selected' >"+itr);
					else
						out.print("<option value='"+itr+"'>"+itr);
				 }
		  }
		  out.print("</select><br/><br/>");
		  
		  // Radio button view
		  p_c_b = request.getParameter("p_c_b");
		  if(request.getParameter("applyFilter")==null || p_c_b.equals("both")){
			  out.print("<input type='radio' name='p_c_b' value='project'> Project&emsp;");
			  out.print("<input type='radio' name='p_c_b' value='course'> Course&emsp;");
			  out.print("<input type='radio' name='p_c_b' value='both' checked> Both<br/><br/>");
				
		  } else {
			  if(p_c_b.equals("project")){
				  out.print("<input type='radio' name='p_c_b' value='project' checked> Project&emsp;");
				  out.print("<input type='radio' name='p_c_b' value='course'> Course&emsp;");
			  }
			  if(p_c_b.equals("course")){
				  out.print("<input type='radio' name='p_c_b' value='project'> Project&emsp;");
				  out.print("<input type='radio' name='p_c_b' value='course' checked> Course&emsp;");
			  }
			  out.print("<input type='radio' name='p_c_b' value='both'> Both<br/><br/>");
		  }
		  
		  // Submit buttons
		  out.print("<input type='hidden' name='username' value='"+username+"'/>");
			out.print("<input type='submit' name='applyFilter' value='Apply Filter'/>&emsp;");
			out.print("<input type='submit' name='resetFilter' value='Reset Filter'/>");
			out.print("</form><br/>");
			out.print("<button onclick=\"window.location.href='welcome.html'\">Log out</button><br/>");
			out.print("<hr>");
	
			// DISPLAYING SEARCH RESULTS IN A TABLE HERE
			
			conn = null;
			stmt = null;
			rs = null;
			out.print("<div style='height:300px; overflow:auto; display:block;'>");
			out.print("<table frame='box' bgcolor='#FFDDFF' align='center'>");
			out.print("<thead><tr><th style='border:2px solid black;' bgcolor='#FF88FF'>Name</th><th style='border:2px solid black;' bgcolor='#FFAAFF'>Type</th></tr></thead>");
			out.print("<tbody>");
			
			if(request.getParameter("applyFilter")==null){
				try {
				  Class.forName("com.mysql.jdbc.Driver").newInstance();
				  conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
				 query = "(SELECT PName AS Name,'project' AS Type FROM PROJECT) UNION (SELECT CName AS Name,'course' AS Type FROM COURSE) ORDER BY Name ASC";
				  stmt = conn.prepareStatement(query);
				  rs = stmt.executeQuery();
				  while(rs.next()){
					  out.print("<tr>");
					  out.print("<td bgcolor='#FF88FF'><a href='/SLS_cs4400/viewPC?name="+ rs.getString("Name")+"&type="+rs.getString("Type")+"&username="+username+"'>"+ rs.getString("Name")+"</a></td><td bgcolor='#FFAAFF'>"+ rs.getString("Type")+"</td>");
					  out.print("</tr>");
				  }
				  out.print("</tbody></table></div>");
				  rs.close();
				} catch(Exception e) {
					   System.err.println("Exception: "+e.getMessage());
				} finally {
				   try {
					if(conn != null)
					  conn.close();
				   } catch(SQLException e) {}
				}
				out.print("</div></body></html>");
			} else {	// if 'applyFilter' button is clicked - display search results
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_93","cs4400_Team_93","bW7TmA2i");
					
					if(p_c_b.equals("project") || p_c_b.equals("both")){
						 
						 int help[] = new int[5];// 0 category, 1 major, 2 year, 3 title, 4 designation
			             query="SELECT DISTINCT PName AS Name,'project' AS Type FROM PROJECT ";
			             if (categories!=null){	// assumes that if multiple categories are selected, the first one isn't left as 'None'
			                 if(!categories[0].equals("None")){
			            	 help[0]=1;
			                 query+="NATURAL JOIN PROJECT_CAT ";
			                 }
			             }
			             if(major!=null || year!=null){
			                 if(!major.equals("None"))
			                	 help[1]=1;
			                 if(!year.equals("None"))
			                	 help[2]=1;
			                 if(help[1]==1||help[2]==1)
			                	 query+="NATURAL JOIN PROJECT_REQ ";
			             }
			             query+="WHERE 1=1 ";
			
			             if (title!=null){
			            	if(!title.equals(""))
			            	 help[3]=1;
			             }
			                 
			             if (designation!=null){
			            	if(!designation.equals("None"))
			            	 help[4]=1;
			             }
			             // if length is 3, (?,?,?) 
			             String bufCat = "AND CatName IN (";	// Categories are considered in OR fashion. Cat1 OR Cat2 OR ...
			             int catLength = categories.length;
			             for(int itr=0;catLength!=0 && itr<catLength-1;itr++){
			            	 bufCat += "?,";
			             }
			             bufCat += "?) ";
			             String []buffer ={bufCat,"AND PName IN (SELECT PName FROM PROJECT_REQ WHERE Requirement=?) ","AND PName IN (SELECT PName FROM PROJECT_REQ WHERE Requirement=?) ","AND PName LIKE ? ","AND DesigName=?"};
			             for (int i=0;i<5;i++){
			                 if(help[i]==1)
			                     query+=buffer[i];
			             }
			             query+=";";
			             stmt = conn.prepareStatement(query);
			             int j=1;
			             if(help[0]==1){
			            	 for(int itr=0;itr<catLength;itr++)
			            		 stmt.setString(j++, categories[itr]);
			             }
			              
			             if(help[1]==1)
			                 stmt.setString(j++, major);
			
			             if(help[2]==1)
			                 stmt.setString(j++, year);
			
			             if(help[3]==1)
			                 stmt.setString(j++, "%"+title+"%");
			
			             if(help[4]==1)
			                 stmt.setString(j++, designation);
			             
			             rs = stmt.executeQuery();
			             while(rs.next()){
			       			  out.print("<tr>");
			       			  out.print("<td bgcolor='#FF88FF'><a href='/SLS_cs4400/viewPC?name="+ rs.getString("Name")+"&type="+rs.getString("Type")+"&username="+username+"'>"+ rs.getString("Name")+"</a></td><td bgcolor='#FFAAFF'>"+ rs.getString("Type")+"</td>");
			       			  out.print("</tr>");
		       		  	 }
			             
					}	// relevant projects displayed
					
					if((p_c_b.equals("course") || p_c_b.equals("both")) && major.equals("None") && year.equals("None")){
						
						int help[] = new int[3];// 0 category, 1 title, 2 designation
		                query="SELECT DISTINCT CName AS Name,'course' AS Type FROM COURSE ";
		                if (categories!=null){ 
		                    if(!categories[0].equals("None")){
				            	 help[0]=1;
				                 query+="NATURAL JOIN COURSE_CAT ";
			                 }
		                }

		                query+="WHERE 1=1 ";

		                if (title!=null)
		                	if(!title.equals(""))
		                		help[1]=1;

		                if (designation!=null)
		                	if(!designation.equals("None"))
		                		help[2]=1;
		                
		                String bufCat = "AND CatName IN (";
			             int catLength = categories.length;
			             for(int itr=0;catLength!=0 && itr<catLength-1;itr++){
			            	 bufCat += "?,";
			             }
			             bufCat += "?) ";
		                String []buffer = {bufCat,"AND CName LIKE ? ","AND DesigName=?"};

		                for(int i=0;i<3;i++){
		                    if(help[i]==1)
		                        query+=buffer[i];
		                }
		                query+=";";
		                stmt = conn.prepareStatement(query);
		                int j=1;
		                if(help[0]==1){
		                	 for(int itr=0;itr<catLength;itr++)
			            		 stmt.setString(j++, categories[itr]);
		                }
		                   
		                if(help[1]==1)
		                    stmt.setString(j++, "%"+title+"%");

		                if(help[2]==1)
		                    stmt.setString(j++, designation);

		                rs = stmt.executeQuery();
		                while (rs.next()){
		                	out.print("<tr>");
		       			  	out.print("<td bgcolor='#FF88FF'><a href='/SLS_cs4400/viewPC?name="+ rs.getString("Name")+"&type="+rs.getString("Type")+"&username="+username+"'>"+ rs.getString("Name")+"</a></td><td bgcolor='#FFAAFF'>"+ rs.getString("Type")+"</td>");
		       			  	out.print("</tr>");
		                }
		            
					}	// relevant courses displayed
					
				 rs.close();		         
				} catch(Exception e) {
					   System.err.println("Exception: "+e.getMessage());
				} finally {
				   try {
					if(conn != null)
					  conn.close();
				   } catch(SQLException e) {}
				}
				
				out.print("</tbody></table></div>");
				out.print("</div><br/><br/>");
				out.print("</body></html>");
			}
		
	}

}
