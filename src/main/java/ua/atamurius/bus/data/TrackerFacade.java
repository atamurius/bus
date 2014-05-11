package ua.atamurius.bus.data;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ua.atamurius.bus.data.GpsTrackerApi.Car;
import ua.atamurius.bus.data.GpsTrackerApi.RoutePoint;
import ua.atamurius.bus.data.GpsTrackerApi.Stop;

@SuppressWarnings("serial")
public class TrackerFacade {

    private GpsTrackerApi api = new GpsTrackerApi();
    
	public static class Point implements Serializable {
    	public final BigDecimal lat;
    	public final BigDecimal lng;
    	public final String id;
		
    	public Point(BigDecimal lat, BigDecimal lng, String id) {
			this.lat = lat;
			this.lng = lng;
			this.id = id;
		}

    	public Point(BigDecimal lat, BigDecimal lng) {
			this.lat = lat;
			this.lng = lng;
			this.id = null;
		}
    	
    	public double latitude() {
    		return lat.doubleValue();
    	}
    	public double longitude() {
    		return lng.doubleValue();
    	}

		@Override
		public String toString() {
			return "[lat=" + lat + ", lng=" + lng +(id == null ? "]" : ", id=" + id + "]");
		}
    }
	
    public static class WayPoint extends Point {
    	public boolean forward;

		public WayPoint(BigDecimal lat, BigDecimal lng, String id,
				boolean forward) {
			super(lat, lng, id);
			this.forward = forward;
		}
		public WayPoint(BigDecimal lat, BigDecimal lng, String id) {
			super(lat, lng, id);
		}
		
		@Override
		public String toString() {
			return super.toString() +(forward ? "->" : "<-");
		}
    }
    
    public static class Route implements Serializable {
    	public final List<WayPoint> path  = new ArrayList<WayPoint>();
    	public final List<WayPoint> stops = new ArrayList<WayPoint>();
    }
    
    private Map<String, Route> routeCache = new ConcurrentHashMap<String, Route>();
    
    public Route getRoute(String id) throws IOException {
    	
    	Route route = routeCache.get(id);
    	if (route == null) {
    		route = new Route();
    		for (RoutePoint p : api.getRoute(id)) {
    			route.path.add(new WayPoint(p.lng, p.lat, null, p.isForward()));
    		}
            for (Stop p : api.getStops(id)) {
            	route.stops.add(new WayPoint(p.lng, p.lat, p.name, p.isForward()));
            }
            routeCache.put(id, route);
    	}
    	return route;
    }
    
    public List<Point> getCars(String id) throws IOException {
    	List<Point> res = new ArrayList<Point>();
        for (Car p : api.getCars(id)) {
            if (p.inZone()) {
            	res.add(new Point(p.X, p.Y, p.CarId));
            }
        }
        return res;
    }
}
