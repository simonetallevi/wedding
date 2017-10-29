package it.simostefi.wedding.servlet;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author tallesi001 on 29/10/17.
 */
@Slf4j
public class CommentsServlet extends AbstractServlet{

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws Throwable {
        log.info("test");
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("test");
    }
}
