vp.v.isConsoleOpen = true;
vp.v.ConsoleHeight = 260;
vp.v._g_TMI_w = 230;
vp.v._g_TMI_h = 160;
vp.v.terminals = null;
vp.v.cur_terminal = null;
vp.page.main = {
	_timer_status : null,
	init : function() {
		vp.page.main._initHeader();
		vp.page.main._initToolbar();
		$("#panel_terminal_info").css(
				{
					"margin-left" : ($(window).innerWidth() - 1 * 2
							- vp.v._g_TMI_w - 25)
							+ "px"
				});
		$("#btn_menu_slide").click(function() {
			$("#my-menus-panel").toggle("fast", function() {
				vp.page.main._resize()
			});
			$(this).find("i").toggleClass("icon-arrow-down")
		});
		$("#console").dialog({
			autoOpen : true,
			maxHeight : vp.v.ConsoleHeight,
			height : "auto",
			width : 260,
			position : {
				my : "left top",
				at : "right bottom",
				of : window
			},
			modal : false,
			show : "clip",
			hide : "clip",
			resizable : false
		});
		$("#chk_auto_refresh").click(function() {
			var a = $(this)[0].checked;
			if (a) {
				vp.page.main._timer_status = window.setInterval(function() {
					var b = function(d) {
						var c = $.parseJSON(d);
						vp.v.cur_terminal.statusChanged({
							status : c.status,
							hbTime : c.hbTime
						})
					};
					lbs.common.ajax("portal/get_latest_status.shtm", {
						terminalSerialNo : vp.v.login_terminal_serial_no
					}, b)
				}, 1000 * 15)
			} else {
				if (vp.page.main._timer_status != null) {
					window.clearInterval(vp.page.main._timer_status);
					vp.page.main._timer_status = null
				}
			}
		});
		$("#btn_maptype_baidu,#btn_maptype_google").click(function() {
			var a = $(this).attr("id").replace("btn_maptype_", "");
			lbs.common.switchMap(a)
		})
	},
	_initHeader : function() {
		var b = new Date();
		var d = b.getFullYear() + "年";
		var c = b.getMonth() + 1;
		d += ((c < 10 ? "0" + c : c) + "月");
		c = b.getDate();
		d += ((c < 10 ? "0" + c : c) + "日");
		var a = [ "日", "一", "二", "三", "四", "五", "六" ];
		d += " 星期" + a[b.getDay()];
		$("#tip_now").html(d + "&nbsp;&nbsp;&nbsp;&nbsp;");
		$("#uname").html($.Cookie("uname"));
		$("#link-logout").click(function() {
			vp.page.main._logout()
		})
	},
	_initToolbar : function() {
		$("#btn_reminder").click(function() {
			if (vp.v.isConsoleOpen) {
				$("#console").dialog("close")
			} else {
				$("#console").dialog("open")
			}
			vp.v.isConsoleOpen = !vp.v.isConsoleOpen
		});
		$("#btn_whereisme").click(function() {
			vp.page.single_track.track({
				tid : vp.v.cur_terminal.terminal_serial_no,
				tname : vp.v.cur_terminal.terminal_name
			})
		});
		$("#btn_start_track").click(function() {
			vp.page.tracker_manager.startTrack({
				tid : vp.v.cur_terminal.terminal_serial_no,
				name : vp.v.cur_terminal.terminal_name
			})
		});
		$("#btn_stop_track").click(function() {
			vp.page.tracker_manager.stopTrack({
				tid : vp.v.cur_terminal.terminal_serial_no
			})
		});
		$("#btn_clear_track").click(function() {
			vp.page.tracker_manager.clearTrace({
				tid : vp.v.cur_terminal.terminal_serial_no
			})
		});
		$("#btn_protected")
				.click(
						function() {
							if (vp.v.cur_terminal.status.status == lbs.consts.codemap.status.PT) {
								vp.v.log.warn("终端已设防！");
								return
							}
							vp.page.ptsts.set(lbs.consts.codemap.status.PT)
						});
		$("#btn_unprotected")
				.click(
						function() {
							if (vp.v.cur_terminal.status.status == lbs.consts.codemap.status.UNPT) {
								vp.v.log.warn("终端已设置为未设防！");
								return
							}
							vp.page.ptsts.set(lbs.consts.codemap.status.UNPT)
						});
		$("#btn_modify_pwd").click(function() {
			vp.page.main._modifyPwd()
		});
		$("#btn_modify_name").click(function() {
			vp.page.main._modifyName()
		})
	},
	_modifyName : function() {
		$("#i_new_name").val("");
		$("#n_p_msg").html("");
		$("#panel_modify_name").dialog({
			autoOpen : true,
			modal : true,
			resizable : false,
			buttons : {
				"确定" : function() {
					vp.page.main._doModifyName($(this))
				},
				"取消" : function() {
					$(this).dialog("close")
				}
			}
		}).show()
	},
	_doModifyName : function(c) {
		var b = lbs.common.trim($("#i_new_name").val());
		if (lbs.common.isTrimEmpty(b)) {
			vp.v.log.warn("请输入新昵称！");
			return
		}
		var d = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
		if (!d.test(b)) {
			vp.v.log.warn("昵称格式不正确！（只能中文、英文、数字，不包含特殊字符空格等！）");
			return
		}
		var a = function(g) {
			vp.v.log.info("昵称修改成功！下次可以用新昵称登录！");
			var f = '<span style="color: green"><i class="icon-ok"></i> 昵称修改成功！</span>';
			$("#n_p_msg").html(f);
			setTimeout(function() {
				c.dialog("close")
			}, 1500)
		};
		var e = {
			terminalName : b
		};
		lbs.common.ajax("portal/modify_nickname.shtm", e, a);
		a = null;
		e = null
	},
	_modifyPwd : function() {
		$("#i_old_pwd,#i_new_pwd,#i_new_pwd_again").val("");
		$("#m_p_msg").html("");
		$("#panel_modify_pwd").dialog({
			autoOpen : true,
			modal : true,
			resizable : false,
			buttons : {
				"确定" : function() {
					vp.page.main._doModifyPwd($(this))
				},
				"取消" : function() {
					$(this).dialog("close")
				}
			}
		}).show()
	},
	_doModifyPwd : function(d) {
		var b = lbs.common.trim($("#i_old_pwd").val());
		var c = lbs.common.trim($("#i_new_pwd").val());
		var e = lbs.common.trim($("#i_new_pwd_again").val());
		if (lbs.common.isTrimEmpty(b)) {
			vp.v.log.warn("请输入旧密码！");
			return
		}
		if (lbs.common.isTrimEmpty(c) || lbs.common.isTrimEmpty(e)) {
			vp.v.log.warn("新密码不能为空！");
			return
		}
		if (c != e) {
			vp.v.log.warn("新密码两次输入不一致！");
			return
		}
		var a = function(h) {
			vp.v.log.info("密码修改成功！下次登录请用新密码！");
			var g = '<span style="color: green"><i class="icon-ok"></i> 密码修改成功！</span>';
			$("#m_p_msg").html(g);
			setTimeout(function() {
				d.dialog("close")
			}, 1500)
		};
		var f = {
			oldPwd : b,
			newPwd : c,
			newPwd2 : e
		};
		lbs.common.ajax("portal/modify_tmn_pwd.shtm", f, a);
		a = null;
		f = null
	},
	afterInit : function() {
		var a = "<span id='btn_console_clear' title='清空日志' class='ui-dialog-title' style='color: red;font-size:14px;cursor:pointer;'>&nbsp;&nbsp;<i class='icon-trash'></i>&nbsp;&nbsp;</span>";
		$("#console").prev("div").children("span").after(a);
		$("#btn_console_clear").click(function() {
			vp.v.log.clear()
		});
		$(window).resize(function() {
			vp.page.main._resize()
		});
		setTimeout(function() {
			vp.page.main._loadTerminalInfo()
		}, 1000)
	},
	_logout : function() {
		lbs.common.ajax("portal/logout.shtm", {}, function(a) {
			top.location.href = vp.v.ctx_path + $.parseJSON(a).forward
		})
	},
	_loadTerminalInfo : function() {
		var a = function(c) {
			var b = $.parseJSON(c);
			vp.v.cur_terminal = b.terminal;
			vp.v.cur_terminal.statusChanged = function(d) {
				if (d.status) {
					$("#c_status").html(lbs.consts.codemap.status[d.status]);
					vp.v.cur_terminal.status.status = d.status
				}
				if (d.trace) {
					$("#c_track_status")
							.html(lbs.consts.codemap.track[d.trace])
				}
				if (d.hbTime) {
					$("#c_t_hbtime").html(d.hbTime)
				}
			};
			vp.v.cur_terminal.posChanged = function(h, g) {
				var d = "mk";
				if (!lbs.common.isEmpty(h.lng)) {
					$("#c_t_p_lng").html(h.lng.substring(0, 8))
				} else {
					$("#c_t_p_lng").html("无数据")
				}
				if (!lbs.common.isEmpty(h.lat)) {
					$("#c_t_p_lat").html(h.lat.substring(0, 7))
				} else {
					$("#c_t_p_lat").html("无数据")
				}
				if (!lbs.common.isEmpty(h.speed)) {
					$("#c_t_p_speed").html(parseFloat(h.speed) + "&nbsp;km/h")
				} else {
					$("#c_t_p_speed").html("无数据")
				}
				if (!lbs.common.isEmpty(h.ggv)) {
					$("#c_t_p_gps").html(h.ggv.substring(0, 1))
				} else {
					$("#c_t_p_gps").html("无数据")
				}
				if (!lbs.common.isEmpty(h.loc_status)) {
					$("#c_t_p_locmode").html(
							lbs.consts.codemap.locmode.getDisp(h.loc_status))
				} else {
					$("#c_t_p_locmode").html("无数据")
				}
				if (!lbs.common.isEmpty(h.status)) {
					$("#c_t_p_status")
							.html(lbs.consts.codemap.status[h.status])
				} else {
					$("#c_t_p_status").html("无数据")
				}
				if (!lbs.common.isEmpty(h.ggv)) {
					var e = h.ggv.substring(2, 3);
					$("#c_t_p_vtg").html(lbs.CC.VTG[e])
				} else {
					$("#c_t_p_vtg").html("无数据")
				}
				if (!lbs.common.isEmpty(h.loc_time)) {
					$("#c_t_p_time").html(h.loc_time)
				} else {
					$("#c_t_p_time").html("无数据")
				}
				if (!lbs.common.isEmpty(h.lng)) {
					vp.m.getLocation(h.lng, h.lat, function(i) {
						$("#c_t_p_desc").html(i)
					})
				} else {
					$("#c_t_p_desc").html("未知区域")
				}
				if (g && !lbs.common.isEmpty(h.lng)) {
					var f = {
						mkid : d + vp.v.cur_terminal.terminal_serial_no,
						name : vp.v.cur_terminal.terminal_name,
						imgUrl : lbs.consts.PIMG.SINGLE,
						x : h.lng,
						y : h.lat,
						center : true
					};
					vp.m.showLoc(f)
				}
			};
			vp.page.main._drawTable(vp.v.cur_terminal);
			vp.v._g_TMI_h = vp.v._g_TMI_h;
			$("#panel_terminal_info").animate({
				width : vp.v._g_TMI_w,
				height : vp.v._g_TMI_h
			}, "fast", "swing");
			$("#panel_terminal_info").children("span").hide();
			$("#panel_terminal_info").children("div").show()
		};
		lbs.common.ajax("portal/load_terminals.shtm", {
			terminalSerialNo : vp.v.login_terminal_serial_no
		}, a);
		a = null
	},
	_drawTable : function(a) {
		$("#c_terminal_serial_no").html(a.terminal_serial_no);
		$("#c_terminal_name").html(a.terminal_name);
		$("#c_op_no").html(a.terminal_op_no);
		$("#c_status").html(lbs.consts.codemap.status[a.status.status]);
		$("#c_track_status").html(
				lbs.consts.codemap.track[lbs.consts.codemap.track.OFF]);
		$("#c_t_hbtime").html(a.status.hbTime);
		vp.v.cur_terminal.posChanged(a.pos, true)
	},
	_resize : function() {
		var a = $(window).innerWidth() - 1 * 2;
		var b = $(window).innerHeight() - $("#panel_header").height() - 2 * 2;
		if (a < 400 || b < 200) {
			return
		}
		vp.m.resize(a, b);
		$("#panel_terminal_info").css({
			"margin-left" : (a - vp.v._g_TMI_w - 25) + "px"
		});
		$("#console").dialog({
			position : {
				my : "left top",
				at : "right bottom",
				of : window
			}
		})
	}
};