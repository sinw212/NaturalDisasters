<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<link rel="stylesheet" href="../StyleCSS/Base.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
<script src="../jsFolder/fixing.js"></script>
<title>자연재해를 부탁해</title>
<style>
</style>
</head>
<body>
	<div class="text-center">
		<h1>자연재해를 부탁해</h1>
	</div>
	<nav class="site-header sticky-top py-1" style="background-color: skyblue;">
		<div class="container d-flex flex-column flex-md-row justify-content-between">
			<a class="py-2 d-md-inline-block" href="../Introduction/NSLab.jsp">INTRODUCTION</a>
			<a class="py-2 d-md-inline-block" href="../Project/Project.jsp">PROJECT</a>
			<a class="py-2 d-md-inline-block" href="../Activity/Contest.jsp">ACTIVITY</a>
			<a class="py-2 d-md-inline-block" href="../Board/Notice.jsp">BOARD</a>
		</div>
	</nav>
	<div class="container mt-4">
		<div id="carouselExampleFade" class="carousel slide carousel-fade"
			data-ride="carousel">
			<ol class="carousel-indicators">
				<li data-target="#carouselExampleFade" data-slide-to="0" class="active"></li>
				<li data-target="#carouselExampleFade" data-slide-to="1"></li>
				<li data-target="#carouselExampleFade" data-slide-to="2"></li>
			</ol>
			<div class="carousel-inner">
				<div class="carousel-item active">
					<img src="../Image/Index/img1.JPG" class="d-block w-100" height="500" alt="...">
				</div>
				<div class="carousel-item">
					<img src="../Image/Index/img2.JPG" class="d-block w-100" height="500" alt="...">
				</div>
				<div class="carousel-item">
					<img src="../Image/Index/img3.JPG" class="d-block w-100" height="500" alt="...">
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
</body>
</html>