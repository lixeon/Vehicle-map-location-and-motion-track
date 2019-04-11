vp.t.console = function(d) {
	var c = {
		debug : 0,
		info : 1,
		warn : 2,
		error : 3
	};
	var g = [ "#000000", "#0000ff", "#ff6600", "#ff0000" ];
	var e = $("#" + d.elmId);
	var h = d.maxLen || 200;
	var b = c[d.level] || c.debug;
	var a = function() {
		var j = new Date();
		var k = j.getHours();
		var n = (k < 10) ? ("0" + k) : "" + k;
		k = j.getMinutes();
		var i = (k < 10) ? ("0" + k) : "" + k;
		k = j.getSeconds();
		var l = (k < 10) ? ("0" + k) : "" + k;
		k = "[ " + n + ":" + i + ":" + l + " ]";
		return k
	};
	var f = function(i, m) {
		var j = $(e).children();
		var l = j.length;
		if (l == h) {
			j.last().remove()
		}
		var k = '<div style="color:' + g[m] + '">' + a() + "&nbsp;:&nbsp;" + i
				+ "</div>";
		if (l == 0) {
			$(e).append(k)
		} else {
			$(j.first()).before(k)
		}
	};
	this.clear = function() {
		$(e).empty()
	};
	this.debug = function(i) {
		if (c.debug == b) {
			f(i, c.debug)
		}
	};
	this.info = function(i) {
		if (c.info >= b) {
			f(i, c.info)
		}
	};
	this.warn = function(i) {
		if (c.warn >= b) {
			f(i, c.warn)
		}
	};
	this.error = function(i) {
		if (c.error >= b) {
			f(i, c.error)
		}
	}
};
vp.t.clock = function(c) {
	var b = (new Date(c.substring(0, 4), parseInt(c.substring(4, 6)) - 1, c
			.substring(6, 8), c.substring(8, 10), c.substring(10, 12), c
			.substring(12))).getTime();
	var a = b - (new Date()).getTime();
	this.getCurHHmm = function() {
		var e = new Date((new Date()).getTime() + a);
		var f = e.getHours();
		var g = (f < 10) ? ("0" + f) : "" + f;
		f = e.getMinutes();
		var d = (f < 10) ? ("0" + f) : "" + f;
		return g + ":" + d
	}
};