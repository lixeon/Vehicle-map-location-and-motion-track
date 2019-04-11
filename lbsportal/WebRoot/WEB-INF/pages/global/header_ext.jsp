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
<!--
<script type="text/javascript" src="assets/extjs/ext-all-debug-w-c.js"></script>
<link rel="stylesheet" type="text/css" href="assets/extjs/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="assets/extjs/resources/css/ext-all-gray.css">
<link rel="stylesheet" type="text/css" href="assets/extjs/resources/css/ext-all-access.css">
 -->
<link rel="stylesheet" type="text/css" href="assets/extjs/resources/css/ext-all-neptune.css">
<script type="text/javascript" src="assets/extjs/ext-all.js"></script>
<script type="text/javascript" src="assets/extjs/ext-theme-neptune.js"></script>
<script type="text/javascript" src="assets/extjs/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="js/lbs.ext.abc.js"></script>
<script type="text/javascript" src="js/lbs.ext.consts.js"></script>
<script type="text/javascript" src="js/lbs.ext.common.js"></script>
<script type="text/javascript" src="js/lbs.ext.widget.js"></script>
<script type="text/javascript">
lbs.G.basePath = "<%=basePath%>";
Ext.tip.QuickTipManager.init();
</script>
<style>

.grid-tools-icon {
    margin: 0px 3px;
}

</style>