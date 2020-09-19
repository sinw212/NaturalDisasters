<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>자연재해를 부탁해</title>
<style>
</style>
</head>
<body>
	<div class="text-center mt-4">
		<h1>자연재해를 부탁해</h1>
	</div>
	<div class="container mt-5 d-flex flex-column flex-md-row justify-content-between">
		<div style="width: 70%" class="card text-center">
			<div class="card-header">
				<h4 class="my-0 font-weight-normal">소개</h4>
			</div>
			<div class="card-body">소개</div>
		</div>
		<div style="width: 30%;"class="ml-4">
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
			<div class="card text-center">
				<div class="card-header">
					<div class="row flex-nowrap justify-content-between align-items-center">
						<h4 class="col-8 text-center my-0 font-weight-normal">최근 게시물</h4>
						<a class="btn btn-dark" href="#">+더보기</a>
					</div>
				</div>
				<div id="carouselExampleFade" class="carousel slide carousel-fade"
					data-ride="carousel">
					<ol class="carousel-indicators">
						<li data-target="#carouselExampleFade" data-slide-to="0" class="active"></li>
						<li data-target="#carouselExampleFade" data-slide-to="1"></li>
						<li data-target="#carouselExampleFade" data-slide-to="2"></li>
					</ol>
					<div class="carousel-inner">
						<div class="carousel-item active">
							<a href="#">
								<img src="Image/img1.JPG" class="d-block w-100" height="200" alt="...">
							</a>
						</div>
						<div class="carousel-item">
							<a href="#">
								<img src="Image/img2.JPG" class="d-block w-100" height="200" alt="...">
							</a>
						</div>
						<div class="carousel-item">
							<a href="#">
							<img src="Image/img3.JPG" class="d-block w-100" height="200" alt="...">
							</a>
						</div>
					</div>
					<a class="carousel-control-prev" href="#carouselExampleFade" role="button" data-slide="prev">
						<span class="carousel-control-prev-icon" aria-hidden="true"></span>
						<span class="sr-only">Previous</span>
					</a>
					<a class="carousel-control-next" href="#carouselExampleFade" role="button" data-slide="next">
						<span class="carousel-control-next-icon" aria-hidden="true"></span>
						<span class="sr-only">Next</span>
					</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>