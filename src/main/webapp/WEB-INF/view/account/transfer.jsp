<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file='/WEB-INF/view/layout/header.jsp'%>


<div class="col-sm-8">
	<h2>이체 요청(인증)</h2>
	<h5>Bank App 에 오신걸 환영합니다</h5>
	<form action="/account/transfer" method="POST">
		<div class="form-group">
			<label for="amount">이체 금액</label> 
			<input type="number" class="form-control" placeholder="Enter amount" id="amount" name="amount" value="1000">
		</div>
		<div class="form-group">
			<label for="wAccountNumber">출금 계좌 번호:</label> 
			<input type="text" class="form-control" placeholder="Enter account number" id="wAccountNumberord" name="wAccountNumber" value="1111">
		</div>
		<div class="form-group">
			<label for="password">출금 계좌 비밀번호:</label> 
			<input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="1234">
		</div>
		<div class="form-group">
			<label for="dAccountNumber">입금 계좌 번호:</label> 
			<input type="text" class="form-control" placeholder="Enter account number" id="dAccountNumberord" name="dAccountNumber" value="1111">
		</div>
		<div class="text-right">
			<button type="submit" class="btn btn-primary">이체</button>
		</div>
	</form>
</div>

<%@ include file='/WEB-INF/view/layout/footer.jsp'%>

