<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Shelter.Earthquake" %>

<%
	request.setCharacterEncoding("UTF-8");
	String arcd = request.getParameter("arcd");    //  지역코드
	
	Earthquake earthquake = new Earthquake();
	
	out.clear();
	out.print(earthquake.getData(arcd));
	out.flush();
%>