vp.page.tracker_manager = {
	_list : {},
	startTrack : function(b) {
		var a = vp.page.tracker_manager._list[b.tid];
		if (lbs.common.isEmpty(a)) {
			a = new vp.task.tracker(b);
			a.start(b.notCmd);
			vp.page.tracker_manager._list[b.tid] = a
		} else {
			if (!a.isRun()) {
				a.start(b.notCmd)
			}
		}
		vp.v.cur_terminal.statusChanged({
			trace : lbs.consts.codemap.track.ON
		})
	},
	stopTrack : function(b) {
		var a = vp.page.tracker_manager._list[b.tid];
		if (!lbs.common.isEmpty(a) && a.isRun()) {
			a.stop()
		}
		vp.v.cur_terminal.statusChanged({
			trace : lbs.consts.codemap.track.OFF
		})
	},
	clearTrace : function(c) {
		var b = vp.page.tracker_manager._list[c.tid];
		if (!lbs.common.isEmpty(b)) {
			for (var a = 0; a < b.getPoss().length; a++) {
				vp.m.removeLine(("tk_line_" + c.tid) + a)
			}
			vp.m.removeMark("tk_marker_" + c.tid)
		}
	}
};
vp.task.tracker = function(a) {
	var j = "mk";
	var i = 1000 * 14;
	var d = a.tid;
	var c = a.name;
	var b = null;
	var k = j + d;
	var h = "tk_line_" + d;
	var f = 0;
	var g = null;
	var e = null;
	var n = false;
	var m = [];
	var l = function() {
		var p = function(s) {
			try {
				var r = $.parseJSON(s).locs;
				if (!$("#chk_show_blogloc")[0].checked) {
					lbs.common.filterBlogLoc(r)
				}
				if (!lbs.common.isEmptyList(r.poss)) {
					b = r.lastTime;
					m = m.concat(r.poss);
					if (m.length > 1) {
						var t = null;
						if (g == null) {
							g = m[0];
							for (var q = 1; q < m.length; q++) {
								t = m[q];
								vp.m.drawLine({
									lid : h + (f++),
									xs : [ g.lng, t.lng ],
									ys : [ g.lat, t.lat ],
									color : lbs.common.getLineColor(t.speed)
								});
								g = t
							}
						} else {
							for (var q = 0; q < r.poss.length; q++) {
								t = r.poss[q];
								vp.m.drawLine({
									lid : h + (f++),
									xs : [ g.lng, t.lng ],
									ys : [ g.lat, t.lat ],
									color : lbs.common.getLineColor(t.speed)
								});
								g = t
							}
						}
					} else {
						if (m.length == 1) {
							g = m[0]
						}
					}
					vp.m.showLoc({
						mkid : k,
						name : c,
						imgUrl : lbs.consts.PIMG.TRACK,
						x : g.lng,
						y : g.lat,
						inView : true
					});
					vp.v.cur_terminal.posChanged(g)
				}
			} finally {
				if (n) {
					setTimeout(l, i)
				}
			}
		};
		var o = function(q) {
		};
		lbs.common.ajax("portal/get_latest_trace.shtm", {
			terminalId : d,
			lastTime : b
		}, p, o);
		p = null;
		o = null
	};
	this.start = function(o) {
		started = false;
		if (o) {
			n = true;
			e = window.setTimeout(l, 1000)
		} else {
			n = true;
			e = window.setTimeout(l, 1000)
		}
	};
	this.stop = function() {
		n = false;
		if (e != null) {
			window.clearTimeout(e);
			e = null
		}
	};
	this.isRun = function() {
		return n
	};
	this.getPoss = function() {
		return m
	}
};