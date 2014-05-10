package ua.atamurius.bus.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.json.Json;
import com.google.gson.Gson;

import ua.atamurius.bus.data.TrackerFacade;

public abstract class JsonController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected TrackerFacade getApi() {
		TrackerFacade res = (TrackerFacade) getServletContext().getAttribute(TrackerFacade.class.getName());
		if (res == null) {
			getServletContext().setAttribute(TrackerFacade.class.getName(), res = new TrackerFacade());
		}
		return res;
	}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException 
    {
        resp.setContentType(Json.MEDIA_TYPE);
        Gson gson = new Gson();
        gson.toJson(process(getApi(), req), resp.getWriter());
    }

	protected abstract Object process(TrackerFacade api, HttpServletRequest req) throws IOException;
}
