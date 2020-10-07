<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
 <%
 	session.invalidate();    //  모든 세션정보 삭제
 	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("location.href='index.jsp'");
	script.println("</script>");
	script.close();
	return;
 %>