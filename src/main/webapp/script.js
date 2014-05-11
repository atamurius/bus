(function($) {
	
	var cars = {};
	var stops = [];
	var map = false;
	
	var id = window.location.search;
	id = (id.length == 0) ? "17" : id.substr(1);

	var gm = google.maps;
	var ICON_STOP = {
		url: '/images/stop.png',
		size: new gm.Size(32, 37),
		anchor: new gm.Point(16, 35)
	};
	
	var ICON_CAR_STEADY = {
		path: "M16,0C7.164,0,0,7.164,0,16s7.164,16,16,16s16-7.164,16-16S24.836,0,16,0z M16.031,19.934 "+
			"c-2.188,0-3.965-1.773-3.965-3.965c0-2.195,1.777-3.969,3.965-3.969C18.227,12,20,13.773,20,15.969 "+
			"C20,18.16,18.227,19.934,16.031,19.934z",
		anchor: new google.maps.Point(16,17),
		scale: 0.5,
		fillColor: '#000055',
		fillOpacity: 0.7,
		strokeColor: 'white'
	};
	var ICON_CAR_MOVING = {
		path: "M16,0C7.164,0,0,7.164,0,16s7.164,16,16,16s16-7.164,16-16S24.836,0,16,0z M10,24V8l16.008,8L10,24z",
		anchor: new google.maps.Point(16,17),
		scale: 0.5,
		fillColor: '#0000CC',
		fillOpacity: 1,
		strokeColor: 'white'
	};
	
	function getMap() {
		if (! map) {
		    map = new gm.Map(
	    		$("#map_canvas").get(0), {
	    			mapTypeId: gm.MapTypeId.ROADMAP,
			        disableDefaultUI: false
			    });
		}
		return map;
	}
	
	function toPoint(point) {
		return new gm.LatLng(point.lat, point.lng);
	}
	
	$(function(){
		$.getJSON('/api/route', {id: id}, function(route) {
		    var map = getMap();
			var bounds = new gm.LatLngBounds();
			var pathPoints = $.map(route.path, function(point) {
				var coord = toPoint(point);
				bounds.extend(coord);
				return coord;
			});
			var path = new gm.Polyline({
			    path: pathPoints,
			    strokeColor: "#0000FF",
			    strokeOpacity: 0.5,
			    strokeWeight: 4
			});
			$.each(route.stops, function(i, stop) {
				stops.push(new gm.Marker({
				      position: toPoint(stop),
				      map: map,
				      title: stop.id,
				      icon: ICON_STOP,
				      zIndex: -1,
				      visible: false
			    }));
			});
			path.setMap(map);
			map.fitBounds(bounds);
			window.setInterval(updateCarPositions, 2000);
			gm.event.addListener(map, 'zoom_changed', function() {
				var visible = map.getZoom() > 14;
				$.each(stops, function(i, s) { s.setVisible(visible); });
			});
		});
	});
	
	var version = 1;
	
	function updateCarPositions() {
		var map = getMap();
		$.get('/api/cars', {id: id}, function(carPositions) {
			version++;
			$.each(carPositions, function(i,car) {
				var pos = toPoint(car);
				if (! cars[car.id]) {
					cars[car.id] = {
						version: version,
						marker: new gm.Marker({
					      position: pos,
					      map: map,
					      title: id,
					      icon: ICON_CAR_STEADY
				    	})
					};
				}
				updateCarPosition(pos, cars[car.id]);
			});
			$.each(cars, function(i,car) { 
				car.marker.setVisible(car.version == version);
			});
		});
	}

	function updateCarPosition(pos, car) {
		car.version = version;
		var last = car.marker.getPosition();
		if (car.lastPosition != last)
			car.lastPosition = last;
		car.marker.setPosition(pos);
		var icon = car.marker.getIcon();
		if (icon == ICON_CAR_STEADY)
			icon = $.extend({}, ICON_CAR_MOVING);
		
		car.marker.setIcon(icon);
	}

})(jQuery);