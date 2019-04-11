<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>定位管理平台</title>
<%@ include file="../global/header_ext.jsp" %>


<script type="text/javascript" src="jquery.min.js"></script>
<script type="text/javascript"  src="nav.js"></script>



<style type="text/css">
#mheader {
    margin: 5px;
}

#t1 {
    width: 99.5%;
    margin: 0px;
    padding: 0px;
    font-size: 14px;
    color: #fff
}

<!-- a {
    color: #ed9200;
}
-->

a:hover {
    color: blue;
}

html,*,body, div, ul,ol, li,h1,h2,h3,h4,h5,h6,p,span{margin:0;padding:0;}
a{
    text-decoration: none;
    color: #201F35;
    font-weight: 100;
}
.nav_z{
    width:100%;
    position:relative;
    z-index:999 ;
    background: #E7EAEE url(../images/toolbar.png) repeat-x 0 0;
    border: solid 1px #909aa6;
    height: 29px;
    padding: 2px;
    margin: 1px;
    font-size: 12px;
    line-height: 22px;
}
#navul li {
    float: left;
    width: 103px;
    position: relative;
    text-align: center;
    font-family: Tahoma, Verdana, 宋体;
    font-size: 12px;
    line-height: 20px;
    color: #201F35;
    list-style: none;
    border-right: solid 1px #adb3b9;
    margin-top: 4px;
}
#navul li span.arrow{
    background: url(../images/menu_arrow.png) no-repeat right 50%;
    padding-right: 24px;
}
/* url(images/button/menu_arrow.png) no-repeat 0px 50%*/
#navul li:last-child{
    border-right:none;
}
#navul li a:link, #navul li a:visited{
}
#navul li ul{
    display:none;
    position:absolute;
    z-index:999;
    border: 1px solid #999999;
    background-color: #fff;
}
#navul li ul li{
    display:block;
    width:134px;
    float:none;
    position:relative;
    font-size:12px;
    overflow:hidden;
    border: none;
}

#navul li.navmoon{

}
#navul li.navmoon a{
}
#navul li.navhome a:hover{
}
#navul li.navmoon ul{
    display:block;
}
#navul li.navmoon ul a{
    display:block;
    width:78px;
}
#navul li.navmoon ul a:hover{
}

</style>

</head>
<body>
<!-- header -->
<div id="mheader" >
    <table id="t1" style="background-image: url(images/banner.jpg);" >


        <tr>

            <td >当前登录：&nbsp;<span id="uname" style="font-weight: bold;"></span>
           <!--  <input type="button" value="新增" onclick="sub()" class="btn btn-primary"/>  -->
            </td>
            <td style="text-align: right">
                <span id="calendar" ></span>&nbsp;|&nbsp;
                <a href="javascript:void(0);" id="link_logout" >退出系统</a>
            </td>
       <!--       <iframe id="mainFrame2" name="mainFrame2" src="org/open_company.shtm" frameborder="0" height="500px" width="100%" >absc</iframe>  -->
        </tr>
       <tr>

       </tr>
    </table>


     <!-- <div class="nav_z">
			<ul id="navul" class="cl">
				<li>
					<a href="terminal/open.shtm?m=query"><span><img src="http://www.beidouguard.com:6010/js/lib/miniui/themes/icons/7.png"/> </span><span>菜单1</span></a>
				</li>
				<li>
					<a href="#"><span><img src="http://www.beidouguard.com:6010/js/lib/miniui/themes/icons/7.png"/> </span><span class="arrow">菜单2</span></a>
					<ul>
						<li><a href="#">菜单2-1</a></li>
						<li><a href="#">菜单2-2</a></li>
						<li><a href="#">菜单2-3</a></li>
					</ul>
				</li>

			</ul>
		</div>
       -->
         <iframe id="mainFrame" name="mainFrame" src="terminal_distb/open.shtm" frameborder="0" height="500px" width="100%" ></iframe>
</div>

<script type="text/javascript" src="js/mgr/p01-mgr-main.js"></script>
<script  type="text/javascript">
function sub(){
	var cs = "query";
	document.getElementById("mainFrame").src="terminal/open.shtm?m=query" ;

}
		</script>


</body>
</html>
