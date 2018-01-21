package it.simostefi.wedding.servlet.task;

import com.google.gson.JsonObject;
import it.simostefi.wedding.manager.SenderManager;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class SendEmailsTaskServlet extends AbstractTaskServlet {

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SenderManager senderManager = new SenderManager();
        try {
            senderManager.sendEmails();
        } catch (MessagingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
