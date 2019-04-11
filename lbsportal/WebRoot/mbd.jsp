<%@page import="com.app.platform.base.utils.SystemConfig"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
<script src="assets/jqbootstrap/js/jquery-1.9.0.min.js" type="text/javascript"></script>
<script type="text/javascript" src="http://ditu.google.cn/maps/api/js?v=3.14&key=<%= SystemConfig.get("map.google.key") %>&sensor=false&language=zh-CN"></script>
<script src="js/lbs.abc.js" type="text/javascript"></script>
<script src="js/lbs.consts.js" type="text/javascript"></script>
<script src="js/lbs.common.js" type="text/javascript"></script>
<script src="js/main/s-00-base.js" type="text/javascript" ></script>
<script src="js/main/s-02-map-google-tools.js" type="text/javascript" ></script>

<style type="text/css">
#allmap {
    margin: 0px;
    width: 500px;
    height: 500px;
    overflow: hidden;
}
</style>
</head>

<body>
<div id="allmap"></div>

<script type="text/javascript">
vp.v.mapType = "google-china";
var pos = '<%= request.getParameter("p") %>';
var lnglat = pos.split(",");
var gpos = vp.m.adjustXY(lnglat[0], lnglat[1]);
var center = new google.maps.LatLng(gpos.y, gpos.x);
var mapOptions = {
	scaleControl : false,
	center : center,
	zoom : 15
};
var mapObj = new google.maps.Map(document.getElementById("allmap"), mapOptions);
var marker = new google.maps.Marker({
    position: center
});
marker.setMap(mapObj);
</script>

</html>