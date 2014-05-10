package ua.atamurius.bus.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import ua.atamurius.bus.data.TrackerFacade;

public class JsonCars extends JsonController {

    private static final long serialVersionUID = 1L;

    @Override
    protected Object process(TrackerFacade api, HttpServletRequest req) throws IOException {
    	return api.getCars(req.getParameter("id"));
    }
}
