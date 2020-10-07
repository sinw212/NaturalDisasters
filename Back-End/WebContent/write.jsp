<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%
	if(session.getAttribute("userID") == null) {
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('로그인 후 이용해주세요')");
	script.println("history.back()");
	script.println("</script>");
	script.close();
	return;
}
%>
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
<script>
	function setThumbnail(event) {
		for (var image of event.target.files) {
			var reader = new FileReader();
			reader.onload = function(event) {
				var img = document.createElement("img");
				img.setAttribute("src", event.target.result);
				img.setAttribute("style", "width: 30%; height: 150px");
				document.querySelector("#image_container").appendChild(img);
			};
			console.log(image);
			reader.readAsDataURL(image);
		}
	}
</script>
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
				<form method="post"
					action="<%=application.getContextPath()%>/Write"
					enctype="multipart/form-data">
					<table class="table table-striped table-sm">
						<thead class="table-info">
							<tr>
								<td><input type="text" class="form-control" placeholder="글 제목" name="title" maxlength="50" required></td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="2"><textarea class="form-control"
										placeholder="글 내용" name="content" maxlength="4096"
										style="height: 450px;" required></textarea></td>
							</tr>
						</tbody>
					</table>
					<input type="hidden" id="writer" name="writer"
						value="<%=(String) session.getAttribute("userID")%>">
					<hr>
					이미지 첨부: <input multiple="multiple" type="file" id="file" name="file" accept="image/*" onchange="setThumbnail(event);" required><br>
					<div id="image_container"></div>
					<br> <input type="submit" class="btn btn-primary" value="글쓰기">
				</form>
			</div>
			<div class="ml-4">
				<div id="prop"></div>
			</div>
		</div>
	</div>
</body>
</html>