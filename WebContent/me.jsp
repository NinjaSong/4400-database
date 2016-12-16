<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Me</title>
</head>
<%
String username = request.getParameter("username");
if(username==null)
	response.sendRedirect("/SLS_cs4400/welcome.html");
%>
<body bgcolor='#33BEFF'><div id='border' style='text-align:center; border:1px solid #00FFFF'><br/>
<h2>Me</h2>
<a href="/SLS_cs4400/editProfile?username=<%= request.getParameter("username") %>">Edit Profile</a><br/><br/>
<a href="/SLS_cs4400/myApps?username=<%= request.getParameter("username") %>">My Applications</a><br/><br/>
<button onclick='window.history.back();'>Back</button>&emsp;&emsp;&emsp;&emsp;
<form action='/SLS_cs4400/main' method='POST'>
<input name="username" type="hidden" value="<%= request.getParameter("username") %>"/>
<input type="submit" value="Main Page"/>
</form>
</div>
</body>
</html>