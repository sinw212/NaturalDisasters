<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Board.*" %>
<%@ page import="File.FileDAO" %>
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
					int ID = 0;
					if(request.getParameter("ID") != null) {
						ID = Integer.parseInt(request.getParameter("ID"));
					}
					if(ID < 1) {
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('유효하지 않은 글입니다.')");
						script.println("history.back()");
						script.println("</script>");
					}
					BoardDTO boardDTO = new BoardDAO().getPost(ID);
				%>
				<div>
					<table class="table table-striped">
						<thead  class="table-info">
							<tr>
								<th class="text-center" colspan="2"><%= boardDTO.getTitle() %></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td style="width: 20%">등록일</td>
								<td><%= boardDTO.getDate() %></td>
							</tr>
							<tr>
								<td style="width: 20%">작성자기관</td>
								<td><%= boardDTO.getReWriter() %></td>
							</tr>
							<tr>
								<td style="width: 20%">최종 수정일</td>
								<td><%= boardDTO.getReDate() %></td>
							</tr>
							<tr>
								<td style="width: 20%">최종 수정자</td>
								<td><%= boardDTO.getReWriter() %></td>
							</tr>
							<tr>
								<td colspan="2" class="text-center"><br>
									<div>
										<%
											FileDAO fileDAO = new FileDAO();
											ArrayList<String> fileNames = fileDAO.getFile(ID);
											for(int i = 0; i < fileNames.size(); i++) {
										%>
											<img src="upload/<%= fileNames.get(i) %>" height=350>
										<%
											}
										%>
										</div>
									<br><br>
									<div><%= boardDTO.getContent() %></div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="ml-4">
				<div id="prop"></div>
			</div>
		</div>
	</div>
</body>
</html>