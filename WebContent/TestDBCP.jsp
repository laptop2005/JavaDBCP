<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%

	String dbcpName = "jdbc:apache:commons:dbcp:cp";
	Connection conn = DriverManager.getConnection(dbcpName);
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	String query = "SELECT * FROM TEST WHERE TEST_ID = ?";
	
	// PreparedStatement를 가져온다.
	pstmt = conn.prepareStatement(query);
	
	pstmt.setString(1, "0000000001");

	// SQL문을 실행한다.
	rs = pstmt.executeQuery();

	while (rs.next()) {
		String test_id = rs.getString(1);
		String test_name = rs.getString(2);
		String test_date = rs.getString(3);
		// 결과를 출력한다.
		out.println(test_id+","+test_name+","+test_date);
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>

</body>
</html>