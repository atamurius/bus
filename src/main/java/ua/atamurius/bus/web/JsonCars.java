package ua.atamurius.bus.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.atamurius.bus.data.TrackerFacade;

import com.google.api.client.json.Json;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.gson.GsonFactory;

public class JsonCars extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException 
    {
        resp.setContentType(Json.MEDIA_TYPE);
        JsonGenerator out = new GsonFactory().createJsonGenerator(resp.getWriter());
        TrackerFacade api = new TrackerFacade();
        api.getCars(req.getParameter("id"), out);
    }
}
