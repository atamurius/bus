package ua.atamurius.bus.data;

import static java.lang.Math.cos;

public class Geometry {

	public interface GeoPoint {
		double latitude();
		double longitude();
	}
	
	public static double squareEucludeanDistance(GeoPoint a, GeoPoint b) {
		double x = cos(a.longitude()) - cos(b.longitude());
		double y = a.latitude() - b.latitude();
		return x*x + y*y;
	}
	
	public static double scalar(GeoPoint a1, GeoPoint a2, GeoPoint b1, GeoPoint b2) {
		double aY = a2.latitude() - a1.latitude();
		double aX = cos(a2.longitude()) - cos(a1.longitude());
		double bY = b2.latitude() - b1.latitude();
		double bX = cos(b2.longitude()) - cos(b1.longitude());
		return aX*bX + aY*bY;
	}
}
