lbs.common.ajax = function(b, d, f, e, a) {
	if (lbs.common.isTrimEmpty(d)) {
		d = {}
	}
	var c = function(m) {
		if (a) {
			$("#ajax-loading,#ajax-overlay").hide()
		}
		var g = true;
		if (!lbs.common.isEmpty(m)) {
			var k = $.parseJSON(m);
			var h = k.errorLevel;
			if (lbs.consts.ERROR_LEVEL_SYS == h) {
				if (!lbs.common.isTrimEmpty(k.errorMsgs)) {
					alert(k.errorMsgs)
				}
				g = false
			} else {
				if (lbs.consts.ERROR_LEVEL_BIZ == h) {
					if (e != null && e != undefined && k.errCallback == "1") {
						e.call(this, m)
					} else {
						var l = "";
						for (var j = 0; j < k.errorMsgs.length; j++) {
							l += k.errorMsgs[j] + "\n"
						}
						alert(l)
					}
					g = false
				}
			}
		}
		if (g && f != null && f != undefined) {
			f.call(this, m)
		}
	};
	if (typeof (d) == "string") {
		d += "&ajaxSubmit=1"
	} else {
		d.ajaxSubmit = "1"
	}
	$.post(b, d, c, "text");
	if (a) {
		$("#ajax-loading,#ajax-overlay").show()
	}
	c = null;
	d = null
};
lbs.common.isEmpty = function(a) {
	return (a == undefined || a == null || "" == a) ? true : false
};
lbs.common.trim = function(a) {
	if (lbs.common.isEmpty(a)) {
		return a
	}
	if (typeof a == "string") {
		return a.replace(/(^\s*)|(\s*$)/g, "")
	} else {
		return a
	}
};
lbs.common.isTrimEmpty = function(a) {
	if (a == null || "" == a) {
		return true
	}
	return lbs.common.isEmpty(lbs.common.trim(a))
};
lbs.common.isEmptyList = function(a) {
	return (!a || a == null || a == undefined || a.length == 0) ? true : false
};
lbs.common.filterBlogLoc = function(d) {
	var c = d.poss;
	if (c != null && c.length > 0) {
		var a = [];
		for (var b = 0; b < c.length; b++) {
			if (c[b].loc_status == lbs.CC.LOC_STATUS.BLOG_LOC) {
				continue
			}
			a.push(c[b])
		}
		d.poss = a
	}
};
lbs.common._LINE_COLORS = [ "#007500", "#0000E3", "#600000" ];
lbs.common.getLineColor = function(b) {
	var a = Math.floor(b);
	if (a < 20) {
		return lbs.common._LINE_COLORS[0]
	} else {
		if (a < 60) {
			return lbs.common._LINE_COLORS[1]
		} else {
			return lbs.common._LINE_COLORS[2]
		}
	}
};
lbs.common.switchMap = function(g) {
	if (vp.v.engine == g) {
		return
	}
	var c = window.location.href;
	var h = c;
	var b = c.indexOf("?");
	if (b != -1) {
		h = c.substring(0, b);
		var e = "";
		var a = c.substring(b + 1).split("&"), f;
		for (var d = 0; d < a.length; d++) {
			f = a[d].split("=");
			if (f[0] == "mapType" || f[0] == "timestamp") {
				continue
			}
			e += a[d] + "&"
		}
		e += "mapType=" + g + "&timestamp=" + new Date().getTime();
		h += "?" + e
	} else {
		h += "?mapType=" + g + "&timestamp=" + new Date().getTime()
	}
	window.location.replace(h)
};