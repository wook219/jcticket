<%--
  Created by IntelliJ IDEA.
  User: Gram17
  Date: 2024-02-06
  Time: 오후 9:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/resources/css/admin/admin.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
</head>
<body>

    <jsp:include page="/WEB-INF/views/admin/common/adminheader.jsp"/>

    <div class="admin-common-wrap">

        <jsp:include page="/WEB-INF/views/admin/common/admincouponnav.jsp"/>


        <div class="admin-common-content">
            <div class="admin-common-title">
                <h1>쿠폰 관리</h1>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/admin/common/adminfooter.jsp"/>

    <script src="https://code.jquery.com/jquery-3.5.0.js"></script>
    <script src="/resources/js/admin/admin.js"></script>

</body>
</html>
