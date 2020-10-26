<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Board.BoardDAO" %>

<%
	request.setCharacterEncoding("UTF-8");
	String parentID = request.getParameter("ID");    //  부모 아이디
	
	BoardDAO boardDAO = new BoardDAO();
	
	out.clear();
	out.print(boardDAO.getBoardData());
	out.flush();
%>