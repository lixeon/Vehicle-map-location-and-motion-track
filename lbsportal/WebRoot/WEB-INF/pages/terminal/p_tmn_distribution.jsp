<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>定位平台</title>
<%@ include file="../global/header.jsp" %> 

<c:if test="${mapCnf.type == 'baidu'}">
<!-- baidu -->
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=${mapCnf.key4locate}"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/DistanceTool/1.2/src/DistanceTool_min.js"></script>
<!-- 
<script type="text/javascript" src="http://api.go2map.com/maps/js/api_v2.5.1.js"></script>
<script type="text/javascript" src="http://api.go2map.com/maps/js/component/ruler.js"></script>
 -->
</c:if>

<c:if test="${mapCnf.type == 'google-overseas'}">
<!-- google overseas -->
<script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.15&key=${mapCnf.key4locate}&sensor=false&language=en"></script>
</c:if>

<c:if test="${mapCnf.type == 'google-china'}">
<!-- google China -->
<script type="text/javascript" src="http://ditu.google.cn/maps/api/js?v=3.15&key=${mapCnf.key4locate}&sensor=false&language=zh-CN"></script>
</c:if>

<link href="assets/jqbootstrap/css/custom-theme/jquery-ui-1.10.0.custom.css" type="text/css" rel="stylesheet" />
<link href="css/terminal/distributed-main.css" type="text/css" rel="stylesheet" />
<style type="text/css">
</style>

</head>

<body style="overflow: hidden;">

<!-- header -->
<div class="navbar navbar-static-top" >
  	<div class="navbar-inner my-navbar-inner">
   	    <div class="nav" >
		    <table>
		        <tr>
		            <th>企业组织：</th>
		            <td>
		                <select id="select_orgId" >
		                    <c:forEach var="cp" items="${company_list}">
		                        <option value="${cp.org_id}">${cp.org_name}</option>
		                    </c:forEach>
		                </select>
		            </td>
		            <td>&nbsp;&nbsp;</td>
		            <th>分组：</th>
		            <td>
		                <select id="select_grpId" >
		                    <option value="all">--全选--</option>
		                </select>
		             </td>
		        </tr>
		    </table>
		</div>

        <div class="nav" style="margin-top: -3px;">
            <!--  -->
            <a id="btn_show" class="btn btn-small" title="查看分布" style="font-size: 12px;"><i class="icon-globe"></i> 查看分布</a>
            
            <a id="btn_clear" class="btn btn-small" title="清除分布" style="font-size: 12px;"><i class="icon-globe"></i> 清除分布</a>
            <span>&nbsp;</span>
            <input id="chk_autoRefresh" type="checkbox" value="1" ><span>自动刷新</span>
            <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
            <div class="btn-group" >
           		<a id="btn_maptype_baidu" class="btn btn-mini <c:if test="${mapCnf.engine == 'baidu'}">btn-primary</c:if>" title="百度" style="font-size: 12px;"><i class="icon-globe"></i> 百度</a>
	            <a id="btn_maptype_google" class="btn btn-mini <c:if test="${mapCnf.engine == 'google'}">btn-primary</c:if>" style="font-size: 12px;" title="谷歌"><i class="icon-globe"></i> 谷歌</a>
            </div>
            <span>&nbsp;&nbsp;</span>
            <span id="sp_error_info"></span>
        </div>
  	</div>
</div>

<!-- console -->
<div id="console" style="display: none;" title="信息窗">
    <div id="console1" style="overflow: auto; height: 173px; z-index: 3"></div>
</div>

<!-- map panel -->
<div id="map_canvas" style="margin: 2px"></div>

</body>

<%@ include file="../global/footer.jsp" %>
<script type="text/javascript" src="assets/jqbootstrap/js/jquery-ui-1.10.0.custom.min.js" ></script>
<script type="text/javascript" src="js/lbs.ns.default.js"></script>
<script type="text/javascript">
vp.v.ctx_path = "<%= basePath %>";
vp.v.map_type = "${mapCnf.type}";
vp.v.engine = "${mapCnf.engine}";
vp.v.city_clng = "${city.clng}";
vp.v.city_clat = "${city.clat}";
</script>
<script type="text/javascript" src="js/terminal/distribution-main.js"></script>
<script type="text/javascript" src="js/map/map-${mapCnf.engine}-tools.js"></script>
<script type="text/javascript" src="js/map/map-${mapCnf.engine}.js"></script>
<script type="text/javascript">

// initialize map
vp.m.init({
	panelId : "map_canvas",
    toolbarId: "panel_map_toolbar",

    zoom: (vp.v.map_type == "baidu") ? 6 : 5
});

// init page
vp.page.main.init();



</script>

</html>
