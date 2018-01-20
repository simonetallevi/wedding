package it.simostefi.wedding.servlet.task;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Slf4j
public class SendEmailsTaskServlet extends AbstractTaskServlet{

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader file = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream("inviti/inviti.csv")));
        List<String[]> allLines =  file.readAll();
        for (String[] tokens : allLines){
            log.info(tokens[0]);
        }
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
