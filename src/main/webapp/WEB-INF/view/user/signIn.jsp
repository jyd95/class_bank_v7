<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header.jsp -->
<%@ include file='/WEB-INF/view/layout/header.jsp'%>


<!-- start of content.jsp(xxx.jsp) -->
<div class="col-sm-8">
	<h2>로그인</h2>
	<h5>Bank App 에 오신걸 환영합니다</h5>
	<form action="/user/sign-in" method="POST">
	<!-- 예외적으로 로그인은 POST 방식으로  -->
		<div class="form-group">
			<label for="username">username:</label> 
			<input type="text" class="form-control" placeholder="Enter username" id="username" name="username" value="대악마둘리">
		</div>
		<div class="form-group">
			<label for="password">Password:</label> 
			<input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="1234">
		</div>
		<button type="submit" class="btn btn-primary">로그인</button>
	</form>
</div>
<!--  end of col-sm -8  -->

<!-- end of content.jsp(xxx.jsp) -->
<%@ include file='/WEB-INF/view/layout/footer.jsp'%>

