package it.simostefi.wedding.servlet.registration;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.gson.JsonObject;
import it.simostefi.wedding.manager.SenderManager;
import it.simostefi.wedding.servlet.AbstractServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class WebSiteVisitedRegistrationServlet extends AbstractServlet{

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Preconditions.checkNotNull(parameters.get("id"), "missing parameter");
        SenderManager manager = new SenderManager();
        manager.registerWeb(parameters.get("id"));
        resp.sendRedirect("www.simostefi.it");
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
