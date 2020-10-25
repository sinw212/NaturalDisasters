<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Board.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<link rel="stylesheet" href="style.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
<script src="jsFolder/fixing.js"></script>
<title>자연재해를 부탁해</title>
<style>
	a, a:hover {
	color: black;
	text-decoration: none;
	}
</style>
</head>
<body>
	<div id="logo"></div>
	<div class="container">
		<div class="row d-flex flex-column flex-md-row justify-content-between">
			<div style="width: 70%">
				<%
					int pageNumber = 1;
					if(request.getParameter("pageNumber") != null) {
						pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
					}
				%>
				<table class="table table-striped table-sm text-center"">
					<thead class="thead-dark">
						<tr>
							<th scope="col">번호</th>
							<th scope="col">제목</th>
							<th scope="col">작성기관</th>
							<th scope="col">등록일</th>
						</tr>
					</thead>
					<tbody>
						<%
							BoardDAO boardDAO = new BoardDAO();
							ArrayList<BoardDTO> list = boardDAO.getList(pageNumber);
							if(list.size() == 0) {
						%>
						<tr>
							<td colspan="5" class="text-center">데이터가 없습니다.</td>
						</tr>
						<%
							} else {
								for(int i = 0; i < list.size(); i++) {
						%>
						<tr>
							<td class="abb" style="background-color #eee; text-align: center;"><%= list.get(i).getId() %></td>
							<td class="abb" style="background-color #eee; text-align: center;"><a href="view.jsp?ID=<%= list.get(i).getId() %>"><%= list.get(i).getTitle() %></a></td>
							<td class="abb" style="background-color #eee; text-align: center;"><%= list.get(i).getWriter() %></td>
							<td class="abb" style="background-color #eee; text-align: center;"><%= list.get(i).getDate().substring(0,11) %></td>
						</tr>
						<%
								}
							}
						%>
					</tbody>
				</table>
				<%
					if(session.getAttribute("userID") != null) {
				%>
				<div class="text-right">
					<a href="write.jsp" class="btn btn-primary">글쓰기</a>
				</div>
				<%
					}
				%>
			</div>
			<div class="ml-4">
				<div id="prop"></div>
			</div>
		</div>
	</div>
</body>
</html>