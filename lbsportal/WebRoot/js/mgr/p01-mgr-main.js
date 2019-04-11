Ext
		.onReady(function() {
			var b = null;
			
			
			
			var c = function(d, e) {
				if (e.raw.leaf == 0 || e.raw.leaf == "0") {
					return
				}
				
				var g = "tbp_" + e.raw.menu_id;
				var f = Ext.getCmp(g);
//				var g = "tbp_last";
//				var f = Ext.getCmp(g);
				if(f != null){
					console.log(f);
					f.close();
				}
				f = Ext.create("Ext.window.Window", {
					id : g,
					title : e.raw.text,
					icon : e.raw.icon,
					closable : true,
					width : 1004,
					height : 500,
					layout : "fit",
					loader : {
						baseParams : {
							menuId : g
						},
						autoLoad : true,
						scripts : true,
						url : e.raw.url
					}
				});
				f.show();
//				if (f == null) {
//					f = Ext.create("Ext.window.Window", {
//						id : g,
//						title : e.raw.text,
//						icon : e.raw.icon,
//						closable : true,
//						width : 1004,
//						height : 500,
//						layout : "fit",
//						loader : {
//							baseParams : {
//								menuId : g
//							},
//							autoLoad : true,
//							scripts : true,
//							url : e.raw.url
//						}
//					});
//					lbs.G.tabs.add(f).show()
//				} else {
//					//lbs.G.tabs.setActiveTab(f)
//				}
			};
			
			
			
//			var ttbar = Ext.create("Ext.Toolbar", {
//			    items: ['文字', "-", {
//			        xtype: "splitbutton",
//			        text: "按钮"
//			    }, {
//			        text: "菜单",
//			        menu:
//			        {
//			            items: [
//			                {
//			                    text: '选项1'
//			                }, {
//			                    text: '选项2'
//			                }, {
//			                    text: '选项3',
//			                    handler: function () {
//			                        Ext.Msg.alert("提示", "来自菜单的消息");
//			                    }
//			                }
//			            ]
//			        }
//			    }, "->", "关键字：", filed1, {
//			        text: "搜索",
//			        handler: function () {
//			            store.load({ params: { keyword: filed1.getValue()} });
//			        }
//			    }
//			    ]
//			});
			
			var a = function(m) {
				var o = document.cookie.split(";");
				var f = null;
				var p = "";
				for (var g = 0; g < o.length; g++) {
					f = o[g].split("=");
					if (Ext.String.trim(f[0]) == "uname") {
						p = decodeURI(f[1]);
						break
					}
				}
				Ext.getDom("uname").innerHTML = p;
				
				
				
				var j = Ext
						.create(
								"Ext.tab.Panel",{
									region : "center",
									plain : true,
									margin : "1 1 1 0",
									activeTab : 0,
									layout : "fit",
									frame: true,
									items : [ {
										icon : "images/icons/home.png",
										title : "欢迎使用定位管理平台!",
										autoScroll : true,
										items : [{
										    contentEl:"mainFrame",
										}]
//									
//										    contentEl:"mainFrame",
//						                      tabTip:"fengjian",
//						                      title:"加载页面的标签页",
//						                      closable: true
										    
//											   listeners: { activate: _showTerminalDistribution },
//											   html: "带事件的Tab。" ,
//												   id: "tab4",  
									 
									        
										}
								
										]
								
								});
				
				
				lbs.G.tabs = j;
				
				
				
				var h = function(t) {
					var i = Ext.create("Ext.data.TreeStore", {
						fields : [ {
							name : "id",
							type : "string"
						}, {
							name : "menu_id",
							type : "string"
						}, {
							name : "icon",
							type : "string"
						}, {
							name : "url",
							type : "string"
						}, {
							name : "text",
							type : "string"
						}, {
							name : "leaf",
							type : "boolean"
						} ],
						proxy : {
							type : "ajax",
							url : "common/loadmenus.shtm",
							reader : {
								root : "menus"
							}
						},
						nodeParam : "parentId",
						root : {
							expanded : true,
							id : t.id,
							menu_id : t.menu_id,
							text : t.text,
							icon : t.icon,
							url : t.url,
							leaf : t.leaf
						}
					});
					
					
					var s = Ext.create("Ext.tree.Panel", {
						store : i,
//						region : "center",
						rootVisible : false,
//						border : false,
						shadow: "sides",
						listeners : {
							itemclick : {
								fn : c
							}
						}
					});
					return s
				};
				
				
				var l = m.topMenus;
				var n = [];
				for (var g = 0; g < l.length; g++) {
					var r = h(l[g]);
					if (g == 0) {
						b = r
					}
					var d = Ext.create("Ext.panel.Panel", {
						title : l[g].text,
						icon : l[g].icon,
//						layout : "border",
						items : r
					});
					n.push(d)
				}
//			    var tb = Ext.create('Ext.toolbar.Toolbar', { 
//		            width : 300,  
//		            items : [{  
//		                text: '新建'
//		                	
//		            },{  
//		                text: '修改'  
//		            },{  
//		                text: '删除'  
//		            }]  
//		        });  
//			    
//			    tb.add({  
//		            text: '显示'  
//		        }); 
				
				var clj = function(d, e) {
					if (e.raw.leaf == 0 || e.raw.leaf == "0") {
						return
					}
					
					var glj = "tbp_" + e.raw.menu_id;
					var flj = Ext.getCmp(g);
//					var g = "tbp_last";
//					var f = Ext.getCmp(g);
					if(flj != null){
						console.log(flj);
						flj.close();
					}
					flj = Ext.create("Ext.window.Window", {
						id : g,
						title : e.raw.text,
						icon : e.raw.icon,
						closable : true,
						width : 1004,
						height : 500,
						layout : "fit",
						loader : {
							baseParams : {
								menuId : g
							},
							autoLoad : true,
							scripts : true,
							url : e.raw.url
						}
					});
				}
					
//					
//				var glj = "tbp_" + "M0101";
//				var lj = Ext.getCmp(glj);
//				lj = Ext.create("Ext.window.Window", {
//					id : 1,
//					title : "终端管理",
//					icon : 'images/icons/search.png',
//					closable : true,
//					width : 1004,
//					height : 500,
//					layout : "fit",
//					loader : {
//						
//						autoLoad : true,
//						scripts : true,
//						url :  e.raw.url
//					}
//				});
			
			var btn = Ext.create("Ext.Button",{
				
				    text: "终端管理",
				    listeners: { "click": function () {
				        clj
				    }
				    },
				    id: "bt2",
				    scale: 'medium',
				    	renderTo : Ext.getBody()
				});
			
			var btn2 = Ext.create("Ext.Button",{
			
				    text: "事件实现2",
				    listeners: { "click": function () {
				        window.location="terminal/open.shtm?m=query"
				    }
				    },
				    id: "bt3",
				    scale: 'medium',
					renderTo : Ext.getBody()
				});
				
				
				Ext.create("Ext.Viewport", {
					tltle: 'Border Layout',  
					layout : "border",
					items : [ 
						
						{
						xtype : "box",
						region : "north",
						contentEl : "mheader",
						border : true,
						height : 50,
//						 html: '这是北边区域 north'  
//						items:tbar
//						items:btn
						
						
					}, 
					{
						region : "west",
						icon : "",
						title : "菜单",
						   height:80,  
//						border : true,
						split : true,
						margins : "1 0 1 1",
						width : 260,
						minWidth : 200,
						maxWidth : 520,
						collapsible : true,
						animCollapse : true,
						layout : "accordion",
						items : n
//						items : tb
					}, 
					lbs.G.tabs 
					
				],
//				     items:[  
//				    	  
//				         {  
//				        	 bbar: lbs.G.tabs,
//				  
//				          title:{
//				        	  n
//				          },  
//				  
//				          html:"面板中的主体内容部分",  
				  
//				          bbar:[  
//				  
//				             {text:"按钮1"},  
//				  
//				             {text:"按钮2"}]  
//				  
//				         }] ,
					renderTo : Ext.getBody()
				});
				
				
				
				var k = new Date();
				var e = k.getFullYear() + "年";
				var q = k.getMonth() + 1;
				e += ((q < 10 ? "0" + q : q) + "月");
				q = k.getDate();
				e += ((q < 10 ? "0" + q : q) + "日");
				e += " 星期" + [ "日", "一", "二", "三", "四", "五", "六" ][k.getDay()];
				Ext.getDom("calendar").innerHTML = e
			};
			
			
			Ext.Ajax.request({
				url : "common/loadinitmenus.shtm",
				success : function(f, e) {
					var d = Ext.decode(f.responseText);
					a(d);
					if (b != null) {
						setTimeout(function() {
							b.getRootNode().expandChildren(true)
						}, 200)
					}
				}
			});
			
			Ext.get("link_logout").on("click", function(d) {
				Ext.Ajax.request({
					url : "portal/logout.shtm?type=2",
					success : function(e, g) {
						var f = Ext.decode(e.responseText);
						top.location.href = lbs.G.basePath + f.forward
					}
				})
			})
		});
////show mass terminal distribution.
//function _showTerminalDistribution (tab) {
//    lbs.U.createTopTabInIframe({
//    	id: _PID + "_map_mass_distribution",
//        icon: "images/icons/location.png",
//        name: "查看终端分布",
//        url: "terminal_distb/open.shtm",
//        qparams: {
//            m: "distribution"
//        }
//    });
//};

