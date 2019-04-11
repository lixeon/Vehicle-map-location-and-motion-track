/**
 * Main
 */
(function() {
	
	/**
	 * Delay task Executor
	 */
	function _DelayTaskExecutor(opts) {
		
		var _DELAY = 1000;
		
		// task wrapper
		var _taskWrapper = {
		    task: null,
		    updtime: 0
		};
		
		// executor proxy
		var _executorProxy = opts.executor;

		// task checker
		var _checker = window.setInterval(function() {
			if (_taskWrapper.task != null
					&& (new Date().getTime() - _taskWrapper.updtime > _DELAY)) {
				var myTask = _taskWrapper.task;
				_taskWrapper.task = null;
				// execute task
				try {
					_executorProxy(myTask);
				} catch (e) {
					// ignore 
				}
			}
		}, opts.checkInterval || 10);

		
		// add a new task
		this.addTask = function(task) {
			_taskWrapper.task = task;
			_taskWrapper.updtime = new Date().getTime();
		};

		// destroy
		this.destroy = function() {
			if (_checker != null) {
				window.clearInterval(_checker);
				_checker = null;
			}
		};
	}
	
	

	// refresher
	var _refresher = null;
	var _refresh_cycle = 1000 * 20;
	
	// delay task executor
	var _taskExecutor = null;

	// group info cache
	var _GROUP_CACHE = {
	    "0000": [{"org_id":"all", "org_name": "--全选--"}]
	};


	
	/**
	 * initialize
	 *
	 */
	var _init = function() {

		// refresh group combo.
		var _refreshGrpCombo = function(groupList) {
			var html = "";
			for (var i = 0; i < groupList.length; i++) {
				html += "<option value='#org_id' >#org_name</option>"
					.replace(/#org_id/g, groupList[i].org_id)
					.replace(/#org_name/g, groupList[i].org_name);
			}
			$("#select_grpId").html(html);
		};
		
		$("#select_orgId").change(function() {
			var companyId = $(this).val();
			if (_GROUP_CACHE[companyId] != null) {
				_refreshGrpCombo(_GROUP_CACHE[companyId]);
			} else {
				lbs.common.ajax("terminal_distb/loadgrplist.shtm", {
                    companyId: companyId
                }, function(respText) {
                	var jsonObj = $.parseJSON(respText);
                	// hold
                	var groupList = jsonObj.groupList;
                	var all = [{"org_id":"all", "org_name": "--全选--"}].concat(groupList);
                	_GROUP_CACHE[companyId] = all;
                	_refreshGrpCombo(all);
                });
			}
		});
		
		
		// switch map
		$("#btn_maptype_baidu,#btn_maptype_google").click(function() {
	    	var mapType = $(this).attr("id").replace("btn_maptype_", "");
	    	// switch map
	    	lbs.common.switchMap(mapType);
		});
		
		
		// show distribution button
		$("#btn_show").click(function() {
			_doShowTerminalsDistribution();
		});
		
		// clear all
		$("#btn_clear").click(function() {
			vp.m.clearAll();
		});
		
		// refresh switch
		$("#chk_autoRefresh").click(function() {
			var chk = $(this)[0];
			var checked = chk.checked;
			if (checked) {
				if (_refresher == null) {
					_refresher = window.setInterval(function() {
						_doShowTerminalsDistribution();
					}, _refresh_cycle);
				}
			} else {
				if (_refresher != null) {
					window.clearInterval(_refresher);
					_refresher = null;
				}
			}
		});
		
		
		// action change first time
		$("#select_orgId").change();
		
		// initialize task executor
		_taskExecutor = new _DelayTaskExecutor({
			executor : function() {
				_doShowTerminalsDistribution();
			}
		});
		
		/// register map event
		var _handler = function() {
			_taskExecutor.addTask({});
		};
		vp.m.registerEvent("dragend", _handler);
		vp.m.registerEvent("zoomend", _handler);
		
		// auto show
		window.setTimeout(function() {
			_doShowTerminalsDistribution();
		}, 500);
	};

	/**
	 * show
	 */
	var _doShowTerminalsDistribution = function() {
		vp.m.clearAll();
		_showTerminals();
	};
	
	/**
	 * display terminals
	 */
	var _dispalyTerminals = function(jsonObj) {
		var terminals = jsonObj.terminals;
		for (var i = 0; i < terminals.length; i++) {
			var htmlc = "<div style='font-size: 12px; padding: 5px'>"
				+ "<div>编号：" + terminals[i].terminal_serial_no + "</div>"
				+ "<div>经度：" + terminals[i].lng + "</div>"
				+ "<div>纬度：" + terminals[i].lat + "</div>"
				+ "<div>速度：" + terminals[i].speed + "km/h</div>"
				+ "<div>卫星：" + terminals[i].gps + "</div>"
				+ "<div>状态：" + lbs.consts.codemap.status[terminals[i].tmn_status] + "</div>"
				+ "<a href=" + "#" + ">实时跟踪</a>"
				+ "<a href=" + "#" + ">历史轨迹</a>"
				+ "</div>";
			var colors = terminals[i].color.split(",");
			
			var params = {
                mkid: "mk-" + terminals[i].terminal_serial_no,
                name: terminals[i].terminal_serial_no,
                imgUrl: "images/terminal/t_blk_0.gif",
                x: terminals[i].lng,
                y: terminals[i].lat,
                // center: true,
                infoWin: {
                	action: "click",
                	content: htmlc,
                	title: "运行详情（" + terminals[i].terminal_name + "）"
                },
                label: {
                	content: "<span style='background-color:#" + colors[0] + ";color:#" + colors[1] + "' >" + terminals[i].terminal_name + "</span>"
                }
            };

            // show on map
            vp.m.showLoc(params);
		}
	};

	/**
	 * show terminals
	 */
	var _showTerminals = function() {
		var orgId = $("#select_grpId").val();
		if ("all" == orgId) {
			orgId = $("#select_orgId").val();
		}
		
		var mapBounds = vp.m.getMapBounds();
		var params = {
			orgId : orgId,
		    leftBottomLat : mapBounds.swy,
		    leftBottomLng : mapBounds.swx,
		    rightTopLat : mapBounds.ney,
		    rightTopLng : mapBounds.nex
		};

		// execute load
		lbs.common.ajax("terminal/map_distribution.shtm", params, function(respText) {
			var jsonObj = $.parseJSON(respText);
			if (jsonObj.terminals && jsonObj.terminals.length > 0) {
				_dispalyTerminals(jsonObj);
				$("#sp_error_info").html("");
				//判断是否在线
				
			} else {
				$("#sp_error_info").html("该范围内没有在线终端！");
			}
		});
	};

	/**
	 * resize page
	 */
	var _resize = function() {
		var mapW = $(window).innerWidth() - 1 * 2;
		var mapH = $(window).innerHeight() - $("#panel_header").height() - 2 * 2;
		if (mapW < 400 || mapH < 200) {
			return;
		}

		// resize map
		vp.m.resize(mapW, mapH);
	};

	
	// attach
	$(window).resize(function() {
		_resize();
	});

	// export Main Page
	vp.page.main = {};
	vp.page.main.init = _init;
	vp.page.main.resize = _resize;

})();