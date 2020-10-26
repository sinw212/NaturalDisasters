<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="User.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<%
	if(session.getAttribute("userID") != null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("history.back()");
		script.println("</script>");
		script.close();
		return;
	}
%>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="User.UserDTO" scope="page" />
<jsp:setProperty name="user" property="id" />
<jsp:setProperty name="user" property="password" />
<%
	UserDAO userDAO = new UserDAO();
	String log = userDAO.login(user.getId(), user.getPassword());
	String result[] = log.split(",");
	if(result[0].equals("success")) {
		session.setAttribute("userID", user.getId());
		session.setAttribute("userName", result[1]);
		session.setAttribute("userAuthority", result[2]);
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("location.href='index.jsp'");
		script.println("</script>");
		script.close();
	} else if(result[0].equals("error")) {
		if(result[1].equals("password")) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('비밀번호가 틀렸습니다.')");
			script.println("history.back()");
			script.println("</script>");
			script.close();
			return;
		} else if(result[1].equals("ID")) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('존재하지 않는 아이디입니다.')");
			script.println("history.back()");
			script.println("</script>");
			script.close();
			return;
		} else if(result[1].equals("DB")) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('DB오류입니다.')");
			script.println("history.back()");
			script.println("</script>");
			script.close();
			return;
		}
	}
%>