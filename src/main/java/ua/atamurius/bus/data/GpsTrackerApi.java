package ua.atamurius.bus.data;

import static java.net.URLEncoder.encode;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Key;

public class GpsTrackerApi {

	private static final String BASE_URL = "http://sumy.gps-tracker.com.ua/";
	private static final String ENC = "UTF-8";
	
	private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();
	private static final JsonFactory JSON_FACTORY = new GsonFactory();
	private final JsonObjectParser PARSER = new JsonObjectParser(JSON_FACTORY);
	  
	private static final String TRUE = "t";

	protected <T> T request(Class<T> resType, String act, String ... pairs) throws IOException {
		StringBuilder url = new StringBuilder(BASE_URL).append("mash.php?act=").append(encode(act, ENC));
		for (int i = 0; i < pairs.length/2; i++)
			url.append("&").append(encode(pairs[i*2], ENC)).append("=").append(encode(pairs[i*2 + 1], ENC));
		
		HttpRequest request = HTTP_TRANSPORT.createRequestFactory().buildGetRequest(new GenericUrl(url.toString()));
		request.setParser(PARSER);
		return request.execute().parseAs(resType);
	}

	// --- cars ---------------------------------------------------------------
	
	public static class Cars {
		@Key List<Car> rows;
		
		public List<Car> getCars() {
			return rows;
		}
	}
	
	public static class Car {
		@Key String CarId;
		@Key String route_id;
		@Key String inzone;
		@Key String CarName;
		@Key double X;
		@Key double Y;
		@Key double pX;
		@Key double pY;
		
		public boolean inZone() {
			return TRUE.equals(inzone);
		}
		
		public String getId() {
			return CarId;
		}
		
		@Override
		public String toString() {
			return "ResponseRow [CarId=" + CarId + ", route_id=" + route_id
					+ ", inzone=" + inzone + ", CarName=" + CarName + ", X="
					+ X + ", Y=" + Y + ", pX=" + pX + ", pY=" + pY + "]";
		}
	}
	
	public List<Car> getCars(String id) throws IOException {
		return request(Cars.class, "cars", "id", id).getCars();
	}

	// --- route --------------------------------------------------------------
	
	public static class RouteInfo {
		@Key String id;
	}
	
	public static class RoutePoint {
		@Key double lat;
		@Key double lng;
		@Key String direction;

		@Override
		public String toString() {
			return "RoutePoint [lat=" + lat + ", lng=" + lng + ", direction="
					+ direction + "]";
		}
	}
	
	private String getRouteId(String id) throws IOException {
		return request(RouteInfo[].class, "marw", "id", id)[0].id;
	}
	
	public List<RoutePoint> getRoute(String id) throws IOException {
		return asList(request(RoutePoint[].class, "path", "id", id, "mar", getRouteId(id)));
	}
	
	// --- stops --------------------------------------------------------------
	
	public static class Stop {
		@Key String name;
		@Key String direction;
		@Key double lat;
		@Key double lng;

		@Override
		public String toString() {
			return "Stop [name=" + name + ", direction=" + direction + ", lat="
					+ lat + ", lng=" + lng + "]";
		}
	}
	
	public List<Stop> getStops(String id) throws IOException {
		return asList(request(Stop[].class, "stops", "id", id, "mar", getRouteId(id)));
	}
}
