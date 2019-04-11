/**
 * Google Map
 */
(function() {
    
    // constant
    var _C_DISP_LEVEL = 13;

    var _MAP_EVENT = {
    	"dragend" : "dragend",
    	"zoomend" : "zoom_changed"
    };
    
    // map id
    var _map_id = "map_canvas";
    var _map_toolbar_id = "panel_map_toolbar";

    // map object
    var _map_obj = null;
    var _map_ruler = null;
    
    // map overlays
    var _map_overlay_holder = {};
    
    
    /**
     * google label
     */
    GLabel.prototype = new google.maps.OverlayView(); 
    function GLabel(opts) {
    	this._opts = opts;
    	this._div = null;

    	this.setMap(opts.map);
    }
    GLabel.prototype.onAdd = function() {
    	var div = document.createElement('div');
    	//div.style.border = '0px solid #ccc';
    	// div.style.backgroundColor = this._opts.bgColor;
    	div.style.fontSize = '12px';
	    div.style.position = 'absolute';
	    div.innerHTML = this._opts.content;
	    
	    this._div = div;
    	
    	var panes = this.getPanes();
        panes.overlayLayer.appendChild(div);
    };
    GLabel.prototype.draw = function() {
    	var overlayProjection = this.getProjection();
    	var point = overlayProjection.fromLatLngToDivPixel(this._opts.position);
    	var div = this._div;
    	div.style.left = (point.x + this._opts.offset.x) + 'px';
    	div.style.top = (point.y + this._opts.offset.y) + 'px';
    };
    GLabel.prototype.onRemove = function() {
    	this._div.parentNode.removeChild(this._div);
    	this._div = null;
    };
    
    
    /**
     * initialize
     */
    function init(opts) {

    	// map panel id, map toolbar panel id
        _map_id = opts.panelId;
        _map_toolbar_id = opts.toolbarId;
        
        var mapW = $(window).width() - 1 * 2;
        var mapH = $(window).height() - $("#panel_header").height() - 2 * 2;
        
        // reset map view size
        $("#" + _map_id).css({"height": mapH + "px", "width": mapW + "px"});
        $("#" + _map_toolbar_id).css({"margin-left": (mapW * 0.618) - 40 * 4 + "px"});
        $("#" + _map_toolbar_id).show();

        var gpos = vp.m.adjustXY(vp.v.city_clng, vp.v.city_clat);
        
        // load map
        var mapOptions = {
    		mapTypeControl: true,
    	    mapTypeControlOptions: {
    	    	mapTypeIds: [google.maps.MapTypeId.ROADMAP,google.maps.MapTypeId.SATELLITE],
    	    	position: google.maps.ControlPosition.TOP_LEFT,
    	    	style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR
    	    },
    		center : new google.maps.LatLng(gpos.y, gpos.x),
    		zoom : opts.zoom || _C_DISP_LEVEL
    	};

    	_map_obj = new google.maps.Map(document.getElementById(_map_id), mapOptions);

        // reset the speed legend
        $("#" + "panel_map_legend").css({"margin-top": "40px", "margin-left": "95px"});
    }

    
    /**
     * add Marker
     * 
     * @param opts {mkid:string, imgUrl:string, name:string, x:number, y:number, center:boolean, inView: boolean}
     */
    function addMarker(opts) {

        // remove old marker
        removeMarker(opts.mkid);
        
        // adjust XY
        var gpos = vp.m.adjustXY(opts.x, opts.y);
        var pos = new google.maps.LatLng(gpos.y, gpos.x);
        
        var marker = new google.maps.Marker({
            id: '#MM' + opts.mkid,
            map: _map_obj,
            position: pos,
            title: opts.name ,
            icon: (opts.imgUrl) ? opts.imgUrl : ""
        });
        
        var lbContent = opts.name;
        if (opts.label) {
        	lbContent = opts.label.content;
        }
        var label = new GLabel({
        	map: _map_obj,
        	content:  lbContent,
        	position: pos,
        	offset: {x:7, y:7}
        });
        
        if (opts.center) {
            _map_obj.panTo(pos);
        } else if (opts.inView) {
            var bounds = _map_obj.getBounds();
            if (!bounds.contains(pos)) {
                _map_obj.panTo(pos);
            }
        }
        
        if (opts.infoWin) {
        	var infowindow = new google.maps.InfoWindow({
        		content: opts.infoWin.content
        	});
        	google.maps.event.addListener(marker, 'click', function() {
        		infowindow.open(_map_obj, marker);
        	});
        }
        

        // hold
        _map_overlay_holder['#MMLB' + opts.mkid] = label;
        _map_overlay_holder['#MM' + opts.mkid] = marker;
    }


    /**
     * remove marker
     */
    function removeMarker(id) {
        _removeOverlay("#MM" + id);
        _removeOverlay("#MMLB" + id);
    }
    
    
    /**
     * Add Line
     *
     * @param opts {lid:string, xs:array, ys:array, color:string}
     */
    function addLine(opts) {
        
        var points = [];
        var gpos = null;
        for (var i = 0; i < opts.xs.length; i++) {
            // adjust XY
            gpos = vp.m.adjustXY(opts.xs[i], opts.ys[i]);
            points.push(new google.maps.LatLng(gpos.y, gpos.x));
        }

        var line = new google.maps.Polyline({
            id: "#ML" + opts.lid,
            map: _map_obj,
            path: points,
            strokeColor: opts.color || "#0000ff",
            strokeOpacity: 0.75,
            strokeWeight: 4
        });

        // hold
        _map_overlay_holder['#ML' + opts.lid] = line;
    }


    /**
     * remove line
     *
     * @param id line id
     */
    function removeLine(id) {
        _removeOverlay("#ML" + id);
    }
    
    
    /**
     * Remote Overlay
     * @param oid overlay id
     */
    function _removeOverlay(oid) {
    	var ol = _map_overlay_holder[oid];
    	if (ol != null) {
    		ol.setMap(null);
    		_map_overlay_holder[oid] = null;;
    	}
    }


    /**
     * resize
     */
    function resize(mapW, mapH) {
        // reset map view size
        $("#" + _map_id).css({"height": mapH + "px", "width": mapW + "px"});
        $("#" + _map_toolbar_id).css({"margin-left": (mapW * 0.618) - 40 * 4 + "px"});
    }
    
    
    /**
     * Map configuration
     */
    function config(key, value) {
        if (key == 'zoom') {
            if (value == undefined) {
                return _map_obj.getZoom();
            } else {
                _map_obj.setZoom(value);
            }
        }
    }
    
    
    /**
     * get map size
     */
    function getMapBounds() {
    	var bounds = _map_obj.getBounds();
    	var myBounds = {
    		swx : bounds.getSouthWest().lng(),
    		swy : bounds.getSouthWest().lat(),
    		nex : bounds.getNorthEast().lng(),
    		ney : bounds.getNorthEast().lat()
    	};
    	return myBounds;
    }

    
    /**
     * clear alll
     */
    function clearAll() {
    	for (var k in _map_overlay_holder) {
    		if (_map_overlay_holder[k] != null) {
    			_map_overlay_holder[k].setMap(null);
    			_map_overlay_holder[k] = null;
    		}
    	}
    }
    
    
    
    /**
     * register event (Map)
     */
    function registerEvent(event, handler) {
    	var myEvent = _MAP_EVENT[event];
    	if (myEvent != null) {
    		google.maps.event.addListener(_map_obj, myEvent, handler);
    	}
    }
    
    /**
     * unregister event (Map)
     */
    function unregisterEvent(event, handler) {
    	// google.maps.event.removeListener(_map_obj, event, callback);
    }
    

    // export
    vp.m.init = init;
    vp.m.resize = resize;
    vp.m.showLoc = addMarker;
    vp.m.removeMark = removeMarker;
    vp.m.drawLine = addLine;
    vp.m.removeLine = removeLine;
    vp.m.mapConfig = config;
    vp.m.getMapBounds = getMapBounds;
    vp.m.clearAll = clearAll;
    vp.m.registerEvent = registerEvent;
    vp.m.unregisterEvent = unregisterEvent;

})();