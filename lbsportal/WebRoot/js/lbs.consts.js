lbs.consts.ERROR_LEVEL_SYS = "0";
lbs.consts.ERROR_LEVEL_BIZ = "1";
lbs.consts.CALL_CB_Y = "1";
lbs.consts.BAIDU_KEY = "9c4c81899c09126709e74ac151ca86a0";
lbs.consts.PIMG = {
	TRACK : "http://code.mapabc.com/images/lan_1.png",
	SINGLE : "",
	HISTORY : "images/main/locblink.gif",
	START : "images/main/start.png",
	END : "images/main/end.png"
};
lbs.consts.COORD_TYPE = {
	WGS84 : "0",
	GPS : "0",
	GOOGLE : "2",
	MAPABC : "2",
	BAIDU : "4"
};
lbs.consts.codemap = {};
lbs.CC = lbs.consts.codemap;
lbs.consts.codemap.status = {
	id : "status",
	PT : "1",
	UNPT : "2",
	EOFF : "255",
	EOFFSLEEP : "511",
	"1" : "设防",
	"2" : "撤防",
	"255" : "断电",
	"511" : "断电休眠",
	"900" : "离线"
};
lbs.consts.codemap.track = {
	id : "track",
	OFF : "0",
	ON : "1",
	"0" : "停止",
	"1" : "开启"
};
lbs.consts.codemap.locmode = {
	getDisp : function(a) {
		if ("3" == a) {
			return "小区定位"
		} else {
			return "GPS定位"
		}
	}
};
lbs.CC.LOC_STATUS = {
	"" : "无",
	"0" : "失败",
	"1" : "成功",
	"2" : "上次结果",
	"3" : "小区定位",
	NODATA : "无数据",
	BLOG_LOC : "3"
};
lbs.CC.VTG = {
	"0" : "3.65V",
	"3" : "3.70V",
	"6" : "4.15V",
	"9" : "4.20V"
};