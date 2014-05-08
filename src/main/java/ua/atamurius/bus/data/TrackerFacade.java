package ua.atamurius.bus.data;

import java.io.IOException;

import ua.atamurius.bus.data.GpsTrackerApi.Car;
import ua.atamurius.bus.data.GpsTrackerApi.RoutePoint;
import ua.atamurius.bus.data.GpsTrackerApi.Stop;

import com.google.api.client.json.JsonGenerator;

public class TrackerFacade {

    private GpsTrackerApi api = new GpsTrackerApi();
    
    public void getRoute(String id, JsonGenerator out) throws IOException {
        out.writeStartObject();
        
        out.writeFieldName("path");
        out.writeStartArray();
        for (RoutePoint p : api.getRoute(id)) {
            out.writeStartObject();
            out.writeFieldName("lat");
            out.writeNumber(p.lng);
            out.writeFieldName("lng");
            out.writeNumber(p.lat);
            out.writeFieldName("forward");
            out.writeBoolean(p.isForward());
            out.writeEndObject();
        }
        out.writeEndArray();
        
        out.writeFieldName("stops");
        out.writeStartArray();
        for (Stop p : api.getStops(id)) {
            out.writeStartObject();
            out.writeFieldName("name");
            out.writeString(p.name);
            out.writeFieldName("lat");
            out.writeNumber(p.lng);
            out.writeFieldName("lng");
            out.writeNumber(p.lat);
            out.writeFieldName("forward");
            out.writeBoolean(p.isForward());
            out.writeEndObject();
        }
        out.writeEndArray();
        
        out.writeEndObject();
    }
    
    public void getCars(String id, JsonGenerator out) throws IOException {
        out.writeStartArray();
        for (Car p : api.getCars(id)) {
            if (p.inZone()) {
                out.writeStartObject();
                out.writeFieldName("id");
                out.writeString(p.CarId);
                out.writeFieldName("lat");
                out.writeNumber(p.X);
                out.writeFieldName("lng");
                out.writeNumber(p.Y);
                out.writeEndObject();
            }
        }
        out.writeEndArray();
    }
}
