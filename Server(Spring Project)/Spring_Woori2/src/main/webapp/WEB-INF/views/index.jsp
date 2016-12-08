<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>SpringMVCProject2</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/css/index.css">
		<script type="text/javascript" src="<%=request.getContextPath() %>/resources/javascript/jquery-1.11.3.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/resources/javascript/app.js"></script>
	</head>
	<body>
		<div id="pageWrapper">
			<div id = "header">
				<img src="<%=request.getContextPath() %>/resources/image/home_top.jpg"/>
			</div>
			<div id = "content">
				<div id = "contentLeft">
					<div id = "info">
						<div id ="info1">
							<h3>Spring Framework</h3>
						</div>
						<div id ="info2">
							<table>
								<tr>
									<td>
										<span id="mid">
											<c:if test="${login==null}">
											who are you?
											</c:if>
											<c:if test="${login=='ok'}">
											admin
											</c:if>
										</span>
									</td>
									<td>
										<img src="<%=request.getContextPath() %>/resources/image/member_num.png"/> <!-- 상대경로는 "/" 로시작하지 않는다. -->
										<br/>
										<span>1</span>
									</td>
								</tr>
							</table>
						</div>
						<div id ="info3">
							<p>채팅하기</p>
						</div>
					</div>
					<div id = "bookIndex">
						<h4 class="chapter">Ch02. <a href="ch02/content" target="contentCenter">의존성 주입</a></h4><hr/>
						<h4 class="chapter">Ch03. <a href="ch03/content" target="contentCenter">MVC 기본</a></h4><hr/>
						<h4 class="chapter">Ch04. <a href="ch04/list" target="contentCenter">게시판 실습</a></h4><hr/>
						<h4 class="chapter">Ch06. <a href="ch06/content" target="contentCenter">기본 객체와 영역</a></h4><hr/>
						<h4 class="chapter">Ch08. <a href="ch08/content" target="contentCenter">에러처리</a></h4><hr/>
						<h4 class="chapter">Ch09. <a href="ch09/content" target="contentCenter">쿠키</a></h4><hr/>
						<h4 class="chapter">Ch10. <a href="ch10/content" target="contentCenter">커넥션풀</a></h4><hr/>
						<h4 class="chapter">Ch11. <a href="ch11/list" target="contentCenter">jdbc 탬플릿</a></h4><hr/>
						<h4 class="chapter">Ch12. <a href="ch12/list" target="contentCenter">MyBatis 사용</a></h4><hr/>
						<h4 class="chapter">Ch13. <a href="ch13/content" target="contentCenter">트랜잭션 처리</a></h4><hr/>
						<h4 class="chapter">Ch14. <a href="ch14/content" target="contentCenter">파일 업/다운로드</a></h4><hr/>
					</div>
				</div>
				<!-- <iframe id = "contentCenter" name = "contentCenter" src="ch03/content">></iframe> -->
			</div>
			<div id = "footer">
				<p>SpringMVCProject2</p>
			</div>
		</div>
	</body>
</html>