<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="User.UserDAO" %>	
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  아이디
	String password = request.getParameter("password");    //  비밀번호
	String newPassword = request.getParameter("newPassword");    //  새로운 비밀번호
	
	UserDAO userDAO = new UserDAO();
	
	out.clear();
	out.print(userDAO.changePW(id, password, newPassword));
	out.flush();
%>