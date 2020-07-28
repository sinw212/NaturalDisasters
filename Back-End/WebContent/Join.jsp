<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="User.UserDAO" %>	
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  아이디
	String name = request.getParameter("name");    //  이름
	String phone = request.getParameter("phone");    //  핸드폰번호
	String password = request.getParameter("password");    //  비밀번호
	
	UserDAO userDAO = new UserDAO();
	
	out.clear();
	out.print(userDAO.join(id, name, phone, password));
	out.flush();
%>