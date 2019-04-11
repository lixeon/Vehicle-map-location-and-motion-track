<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + path + "/";
%>
<base href="<%= basePath %>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
<!-- Le styles
 -->
<link href="assets/jqbootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- 
<link href="assets/jqbootstrap/css/custom-theme/jquery-ui-1.10.0.custom.css" type="text/css" rel="stylesheet" />
 -->
 
<link href="assets/jqbootstrap/css/font-awesome.min.css" type="text/css" rel="stylesheet" />
<!--[if IE 7]>
<link href="assets/jqbootstrap/css/font-awesome-ie7.min.css" rel="stylesheet" >
<![endif]-->
<!--[if lt IE 9]>
<link href="assets/jqbootstrap/css/custom-theme/jquery.ui.1.10.0.ie.css" rel="stylesheet" type="text/css" />
<![endif]-->
<link href="assets/jqbootstrap/js/google-code-prettify/prettify.css" rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="assets/jqbootstrap/js/html5shim.js"></script>
<![endif]-->