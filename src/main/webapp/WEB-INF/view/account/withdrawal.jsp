<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header.jsp -->
<%@ include file='/WEB-INF/view/layout/header.jsp'%>


<!-- start of content.jsp(xxx.jsp) -->
<div class="col-sm-8">
	<h2>출금 요청(인증)</h2>
	<h5>Bank App 에 오신걸 환영합니다</h5>
	<!-- 예외적으로 로그인은 POST 방식으로  -->
	<!-- 
		insert into account_tb(number, password, balance, user_id, created_at)
	 -->
	<form action="/account/withdrawal" method="POST">
		<div class="form-group">
			<label for="amount">출금 금액</label> 
			<input type="number" class="form-control" placeholder="Enter amount" id="amount" name="amount" value="1000">
		</div>
		<div class="form-group">
			<label for="wAccountNumber">출금 계좌 번호:</label> 
			<input type="text" class="form-control" placeholder="Enter account number" id="wAccountNumberord" name="wAccountNumber" value="1111">
		</div>
		<div class="form-group">
			<label for="pwd">출금 계좌 비밀번호:</label> 
			<input type="password" class="form-control" placeholder="Enter password" id="pwd" name="wAccountPassword" value="1234">
		</div>
		<div class="text-right">
		<button type="submit" class="btn btn-primary">출금</button>
		</div>
	</form>
</div>

<%@ include file='/WEB-INF/view/layout/footer.jsp'%>

