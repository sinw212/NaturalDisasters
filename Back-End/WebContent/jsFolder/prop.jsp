<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if (session.getAttribute("userID") != null) {
%>
<div class="card mb-4 shadow-sm text-center">
	<div class="card-header">
		<h4 class="my-0 font-weight-normal">프로필</h4>
	</div>
	<div class="card-body">
		<div>
			<h5 class="mb=2 text-success"><%= session.getAttribute("userName") %></h5>
			<div class="ml-3 mr-3 row flex-nowrap justify-content-between align-items-center">
				<%
					if(session.getAttribute("userAuthority").equals("admin")) {
				%>
				<a class="btn btn-outline-secondary" href="adminPage.jsp">+더보기</a>
				<%
					} else {
				%>
				<a class="btn btn-outline-secondary" href="myPage.jsp">+더보기</a>
				<%
					}
				%>
				<a class="btn btn-outline-secondary" href="Logout.jsp">로그아웃</a>
			</div>
		</div>
	</div>
</div>
<%		
	} else {
%>
<div class="card mb-4 shadow-sm text-center">
	<div class="card-header">
		<h4 class="my-0 font-weight-normal">로그인</h4>
	</div>
	<div class="card-body">
		<div>
			<a class="btn btn-outline-secondary" href="Login.jsp">로그인</a>
		</div>
		<small class="text-muted">아이디를 발급받은 회원만 로그인 가능합니다.</small>
	</div>
</div>
<%
	}
%>