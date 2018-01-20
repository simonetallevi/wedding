package it.simostefi.wedding.servlet.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.simostefi.wedding.manager.RegisterAuthManager;
import it.simostefi.wedding.servlet.AbstractServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class RegisterAuthServlet extends AbstractServlet {

    private static Gson gson = new Gson();

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegisterAuthManager handler = new RegisterAuthManager();
        final Object obj = handler.registerUser();
        resp.getWriter().print(gson.toJson(obj));
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
