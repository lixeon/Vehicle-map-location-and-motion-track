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
<link href="css/main/c-00-platform.css" type="text/css" rel="stylesheet" />
<style type="text/css">
</style>

</head>

<body style="overflow: hidden;">

<!-- header -->
<div id="panel_header" class="navbar navbar-static-top" >
  	<div class="navbar-inner my-navbar-inner">
    	<div id="my-header-info">
            <div class="nav manager-hide" >
    			<span >欢迎您，</span>
    			<span style="font-weight: bold;" id="uname"></span>
    			<span style="color: red"><i class=""></i></span>&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
            
            <div class="nav" >
                <span class="label label-default" >编号：<span id="c_terminal_serial_no"><img src="images/common/loading.gif" /></span></span>
                <span class="label label-default" >卡号：<span id="c_op_no"><img src="images/common/loading.gif" /></span></span>
                <span class="label label-default" >状态：<span id="c_status" ><img src="images/common/loading.gif" /></span></span>
                <span class="label label-default" >追踪：<span id="c_track_status"><img src="images/common/loading.gif" /></span></span>
                <span class="label label-default" >最近访问：<span id="c_t_hbtime"><img src="images/common/loading.gif" /></span></span>
                <input type="checkbox" id="chk_auto_refresh"  style="margin-left: 5px; margin-top: -3px"/><span>&nbsp;刷新状态</span>
                <input type="checkbox" id="chk_show_blogloc" style="margin-left: 5px; margin-top: -3px" /><span>&nbsp;小区定位</span>
                <div class="btn-group" style="margin-top: -3px; margin-left: 10px;">
               		<a id="btn_maptype_baidu" class="btn btn-mini <c:if test="${mapCnf.engine == 'baidu'}">btn-primary</c:if>" title="百度" style="font-size: 12px;"><i class="icon-globe"></i> 百度</a>
					<a id="btn_maptype_google" class="btn btn-mini <c:if test="${mapCnf.engine == 'google'}">btn-primary</c:if>" style="font-size: 12px;" title="谷歌"><i class="icon-globe"></i> 谷歌</a>
                </div>
            </div>

			<div class="nav pull-right manager-hide">
				<i class="icon-time "></i> <span id="tip_now"></span>
				<a id="link-logout" href="javascript:void(0);" ><i class="icon-signout "></i> 退出</a>
	      	</div>
    	</div>
    	<div id="my-menus-panel" style="border-top: 1px solid #eeeeee;">
    		<div class="btn-group">
	    		<a class="btn " title="信息窗口" id="btn_reminder"><i class="icon-laptop "></i><span> 信息窗</span></a>
    		</div>
    		<div class="btn-group">
	    		<a class="btn " title="当前位置" id="btn_whereisme" ><i class="icon-search "></i><span> 当前位置</span></a>
	    		<a class="btn " title="查看历史" id="btn_show_histrace" ><i class="icon-camera-retro "></i><span> 查看历史</span></a>
    		</div>
    		<div class="btn-group">
	    		<a class="btn " title="开始追踪" id="btn_start_track"><i class="icon-random "></i><span> 开始追踪</span></a>
	    		<a class="btn " title="停止追踪" id="btn_stop_track"><i class=" icon-remove-circle "></i><span> 停止追踪</span></a>		
                <a class="btn " title="清除追踪轨迹" id="btn_clear_track"><i class=" icon-trash "></i><span> 清除</span></a>
    		</div>
    		<div class="btn-group">
	    		<a class="btn " title="设防" id="btn_protected" ><i class="icon-lock "></i><span> 设&nbsp;&nbsp;防</span></a>
	    		<a class="btn " title="撤防" id="btn_unprotected"><i class="icon-unlock "></i><span> 撤&nbsp;&nbsp;防</span></a>
    		</div>
    		<div class="btn-group manager-hide" >
	    		<a class="btn " title="密码修改"><i class="icon-user "></i><span> 设&nbsp;&nbsp;置</span></a>
                <a class="btn dropdown-toggle" data-toggle="dropdown" style="padding: 4px 4px 5px 4px"><span class="icon-caret-down icon-large" ></span></a>
                <ul class="dropdown-menu">
                <!-- 
                    <li><a href="javascript:void(0);" id="btn_restart_tmn" ><i class="icon-cogs" ></i> 重启终端</a></li>
                 -->
                    <li ><a href="javascript:void(0);" id="btn_modify_pwd" ><i class="icon-key" ></i> 修改密码</a></li>
                    <li ><a href="javascript:void(0);" id="btn_modify_name" ><i class="icon-user-md" ></i> 修改昵称</a></li>
                </ul>
    		</div>
    	</div>
  	</div>
</div>

<!-- name list -->
<div id="panel_terminal_info" class="well" >
	<span ><i class="icon-spinner icon-spin icon-large" ></i> 正在加载位置信息...</span>
	<div style="display: none">
		<div>
            <table class="tbl-info" border="1">
                <tr>
                    <th>经度</th><td id="c_t_p_lng"></td>
                    <th>纬度</th><td id="c_t_p_lat"></td>
                </tr>
                <tr>
                    <th>速度</th><td id="c_t_p_speed"></td>
                    <th>卫星</th><td id="c_t_p_gps"></td>
                </tr>
                <tr>
                    <th>模式</th><td id="c_t_p_locmode" ></td>
                    <th>状态</th><td id="c_t_p_status" ></td>
                </tr>
                <tr>
                    <th>电压</th><td colspan="3" id="c_t_p_vtg" ></td>
                </tr>
                <tr>
                    <th>时间</th><td colspan="3" id="c_t_p_time" ></td>
                </tr>
                <tr>
                    <th>位置</th><td id="c_t_p_desc" colspan="3" style="height: 42px; width: 185px"></td>
                </tr>
            </table>
		</div>
	</div>
</div>

<!-- Map Legend -->
<div id="panel_map_legend" class="well" >
    <span class="speed-tag"><span class="low" >&nbsp;</span>&nbsp;小于20</span>
    <span class="speed-tag"><span class="mid" >&nbsp;</span>&nbsp;小于60</span>
    <span class="speed-tag"><span class="high">&nbsp;</span>&nbsp;大于60</span>
</div>

<!-- history trace query panel -->
<div id="panel_history_query" class="well" style="display: none;" >
    <div >
        <h6 style="margin: -2px 0px 5px 8px; cursor: move;">
            <span>历史轨迹查询</span>
        </h6>
    </div>
    <div style="margin-bottom: 5px">
        <script type="text/javascript" src="<%= basePath %>js/main/s-00-base-calendar.js"></script>
        <script type="text/javascript">
            var lbs_date_calendar = new lbsDataCalendar({});
        </script>
        <input name="date" type="hidden" id="his_date" onclick="lbs_date_calendar.show(this);" />
    </div>
    <div id="p_hc_c_info">数据加载完成!</div>
    <div style="margin-bottom: 10px">
        <span>时间：从</span>
        <span id="sp_tm_st">00:00</span>
        <span>到</span>
        <span id="sp_tm_et">24:00</span>
    </div>
    <div style="margin-bottom: 10px">
        <div id="select_time">
            <div id="select_time_data_area"></div>
        </div>
    </div>
    <button id="btn_query_play" class="btn btn-small"><i class="icon-play"></i> 开始回放</button>
</div>

<!-- query panel -->
<div id="panel_history_control" class="btn-group" style="position: absolute; z-index: 101; margin-top: 3px; margin-left: 211px; display: none">
    <a id="btn_histrace_pause" class="btn  btn-small" title="暂停"><i class="icon-pause"></i> </a>
    <a id="btn_histrace_backward" class="btn  btn-small" title="减速"><i class="icon-backward"></i> </a>
    <a id="btn_histrace_forward" class="btn  btn-small" title="加速"><i class="icon-forward"></i> </a>
    <!-- 
    <a id="btn_histrace_stop" class="btn  btn-small" title="停止"><i class="icon-stop"></i> </a>
     -->
    <a id="btn_histrace_clear" class="btn  btn-small" title="清除"><i class="icon-trash"></i> </a>
</div>

<!-- console -->
<div id="console" style="display: none;" title="信息窗">
    <div id="console1" style="overflow: auto; height: 173px; z-index: 3"></div>
</div>

<!-- map tools -->
<c:if test="${mapCnf.type == 'baidu'}">
<div id="panel_map_toolbar" class="btn-group" style="position: absolute; margin-top: -1px; z-index: 101; display: none;">
	<a id="btn_menu_slide" class="btn btn-small btn-success"  >
		<i class="icon-arrow-up"></i><i class="icon-arrow-up"></i>
	</a>
	<a id="btn_map_measuredistance" class="btn btn-small" title="测量距离"><i class="icon-resize-horizontal"></i> 测距</a>
</div>
</c:if>

<!-- map panel -->
<div id="map_canvas" style="margin: 2px"></div>

<!-- logout confirm panel? -->
<div id="panel_logout" style="display: none;" title="退出系统">
    <span>您确定要退出系统吗？</span>
</div>

<!-- modify password -->
<div id="panel_modify_pwd" style="display: none;" title="修改密码">
    <div>
        <span >旧密码</span><input type="password" id="i_old_pwd" >
    </div>
    <div>
        <span >新密码</span><input type="password" id="i_new_pwd" >
    </div>
    <div>
        <span >再一遍</span><input type="password" id="i_new_pwd_again" >
    </div>
    <div id="m_p_msg" style="text-align: center;"></div>
</div>

<!-- modify name -->
<div id="panel_modify_name" style="display: none;" title="修改昵称">
    <div>
        <span >新昵称</span><input type="text" id="i_new_name">
    </div>
    <div id="n_p_msg" style="text-align: center;"></div>
</div>

</body>

<%@ include file="../global/footer.jsp" %>
<script src="assets/jqbootstrap/js/jquery-ui-1.10.0.custom.min.js" type="text/javascript"></script>
<script src="assets/jqbootstrap/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script type="text/javascript" src="js/lbs.ns.default.js"></script>
<script type="text/javascript" src="js/main/s-00-base.js"></script>
<script type="text/javascript">
vp.v.map_type = "${mapCnf.type}";
vp.v.engine = "${mapCnf.engine}";
vp.v.city_clng = "${city.clng}";
vp.v.city_clat = "${city.clat}";
vp.v.ctx_path = "<%= basePath %>";
vp.v.login_terminal_serial_no = "${terminal_serial_no}";
vp.v.svrtimer = new vp.t.clock("${server_time}");
vp.v.mode = "${mode}";
// hide block on manager mode
if (vp.v.mode == "manager") {
    $(".manager-hide").css({display: "none"});
}
</script>
<script type="text/javascript" src="js/main/s-01-main.js"></script>
<script type="text/javascript" src="js/map/map-${mapCnf.engine}-tools.js"></script>
<script type="text/javascript" src="js/map/map-${mapCnf.engine}.js"></script>
<script type="text/javascript" src="js/main/s-03-tracker.js"></script>
<script type="text/javascript" src="js/main/s-04-history-trace.js"></script>
<script type="text/javascript" src="js/main/s-05-single-track.js"></script>
<script type="text/javascript" src="js/main/s-06-protected-setting.js"></script>

<script type="text/javascript">

// when page loaded!
$(function() {

	// console
	vp.v.log = new vp.t.console({elmId: "console1", level: "debug"});

    // show calendar
    $("#his_date").trigger("click");

	// main
	vp.page.main.init();

	// map
	vp.m.init({
		panelId : "map_canvas",
		toolbarId: "panel_map_toolbar",
	    zoom: 13
	});
	
	// history trace
	vp.page.history.init(vp.v.login_terminal_serial_no);

	// after init.
	vp.page.main.afterInit();

	// initialized
	vp.v.log.info("页面初始化完成！");

});

</script>

</html>
