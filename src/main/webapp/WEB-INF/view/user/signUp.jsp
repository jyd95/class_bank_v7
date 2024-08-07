<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header.jsp -->
<%@ include file='/WEB-INF/view/layout/header.jsp'%>


<!-- start of content.jsp(xxx.jsp) -->
<div class="col-sm-8">
	<h2>회원 가입</h2>
	<h5>Bank App 에 오신걸 환영합니다</h5>
	<form action="/user/sign-up" method="POST">
		<div class="form-group">
			<label for="username">username:</label> 
			<input type="text" class="form-control" placeholder="Enter username" id="username" name="username" value="소드마스터고길동">
		</div>
		<div class="form-group">
			<label for="password">Password:</label> 
			<input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="asd123">
		</div>
		<div class="form-group">
			<label for="fullname">fullname:</label> 
			<input type="text" class="form-control" placeholder="Enter fullname" id="fullname" name="fullname" value="이 서늘한 감각 오랜만이구나">
		</div>
		<button type="submit" class="btn btn-primary">회원 가입</button>
	</form>
</div>
<!--  end of col-sm -8  -->

<!-- end of content.jsp(xxx.jsp) -->
<%@ include file='/WEB-INF/view/layout/footer.jsp'%>

