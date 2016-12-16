<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Home</title>
</head>
<%
if(request.getParameter("username")==null){
	response.sendRedirect("/SLS_cs4400/welcome.html");
	return;
}
%>
<body bgcolor='#33BEFF'>
<div id="border" style="border:1px solid #00FFFF" ><br/>
<center>
<h2>Choose Functionality</h2>
<a href="/SLS_cs4400/appl_view?adminname=<%= request.getParameter("username") %>">View Applications</a><br/><br/>
<a href="/SLS_cs4400/pop_proj_report?adminname=<%= request.getParameter("username") %>">View popular project report</a><br/><br/>
<a href="/SLS_cs4400/app_report?adminname=<%= request.getParameter("username") %>">View Application report</a><br/><br/>
<a href="/SLS_cs4400/add_project?adminname=<%= request.getParameter("username") %>">Add a Project</a><br/><br/>
<a href="/SLS_cs4400/add_course?adminname=<%= request.getParameter("username") %>">Add a Course</a><br/><br/>
<button onclick="window.location.href='welcome.html'">Log out</button>
</center>
</div>

</body>
</html>