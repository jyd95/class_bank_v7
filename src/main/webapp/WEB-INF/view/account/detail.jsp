<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header.jsp -->
<%@ include file='/WEB-INF/view/layout/header.jsp'%>


<!-- start of content.jsp(xxx.jsp) -->
<div class="col-sm-8">
	<h2>계좌 상세 보기(인증)</h2>
	<h5>Bank App 에 오신걸 환영합니다</h5>
	
	<div class="bg-light p-md-5">
		<div class="user--box">
			${principal.username}님의 계좌<br> 계좌번호 : ${account.number} <br> 잔액 : ${account.balance}원 
		</div>
		<br>
		<div>
			<a href="/account/detail/${account.id}?type=all" class="btn btn-outline-primary">전체 내역</a>&nbsp;
			<a href="/account/detail/${account.id}?type=deposit" class="btn btn-outline-primary">입금 내역</a>&nbsp;
			<a href="/account/detail/${account.id}?type=withdrawal" class="btn btn-outline-primary">출금 내역</a>&nbsp;
		</div>
		<br>
		<table class="table table-striped">
			<thead>
				<tr>
					<th>날짜</th>
					<th>보낸이</th>
					<th>받은이</th>
					<th>입출금 금액</th>
					<th>거래 후 잔액</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="history" items="${historyList}">
				<tr>
					<th>${history.createdAt}</th>					
					<th>${history.sender}</th>					
					<th>${history.receiver}</th>					
					<th>${history.amount}</th>					
					<th>${history.balance}</th>					
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<br>
		<!-- pagenation -->
		<div class="d-flex justify-content-center">
			<ul class="pagination">
				<!-- previous Page Link -->
				<li class="page-item <c:if test='${curruntPage == 1}'>disabled</c:if>">
				<a class="page-link" href="?type=${type}&page=1&size=${size}">First Page</a>
				</li>
				<li class="page-item <c:if test='${curruntPage == 1}'>disabled</c:if>">
				<a class="page-link" href="?type=${type}&page=${curruntPage-1}&size=${size}">Previous</a>
				</li>
				<!-- Page Numbers -->
				<c:forEach begin="1" end="${totalPages}" var="page">
				<li class="page-item <c:if test='${page == curruntPage}'>active</c:if>">
					<a class="page-link" href="?type=${type}&page=${page}&size=${size}">${page}</a>
				</li>
				</c:forEach>
				<!-- next Page Link -->
				<li class="page-item <c:if test='${curruntPage == totalPages}'>disabled</c:if>">
					<a class="page-link" href="?type=${type}&page=${curruntPage+1}&size=${size}">Next</a>
				</li>
				<li class="page-item <c:if test='${curruntPage == totalPages}'>disabled</c:if>">
					<a class="page-link" href="?type=${type}&page=${totalPages}&size=${size}">Last Page</a>
				</li>
			</ul>
		</div>
	</div>
	

	
	
</div>

<%@ include file='/WEB-INF/view/layout/footer.jsp'%>

