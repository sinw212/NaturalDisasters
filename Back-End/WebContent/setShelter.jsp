<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Shelter.Earthquake" %>

<%
	request.setCharacterEncoding("UTF-8");
	
	Earthquake earthquake = new Earthquake();
	
	int result = earthquake.shelterData();
	
	if(result == 1) {
		out.clear();
		out.print("Success");
		out.flush();
	} else {
		out.clear();
		out.print("Fail");
		out.flush();
	}
%>