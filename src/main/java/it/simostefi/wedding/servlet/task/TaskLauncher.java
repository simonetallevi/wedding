package it.simostefi.wedding.servlet.task;

import com.google.gson.JsonObject;
import it.simostefi.wedding.servlet.AbstractServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;


@Slf4j
public class TaskLauncher extends AbstractServlet {

    public enum TasksName {
        SYNC_DATA("TBD", URI.create("TBD"));

        private String queue;

        private URI servlet;

        TasksName(String queue, URI servlet) {
            this.queue = queue;
            this.servlet = servlet;
        }

        public String getQueue() {
            return queue;
        }

        public URI getServlet() {
            return servlet;
        }
    }

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TasksName action = TasksName.valueOf(parameters.get("action"));

        switch (action) {
            case SYNC_DATA: {
//                TaskDef task = new TaskDef(Tasks.SPREADSHEET.name(), Tasks.SPREADSHEET.getServlet());
//                TaskQueueService.runTask(Tasks.SPREADSHEET.getQueue(),
//                        TaskQueueService.getTaskGet(task, ImmutableMap.of(Params.action.name(),"INIT_CONFIG", Params.firstRow.name(),firstRowString)));
                break;
            }
            default:
                throw new IllegalStateException("Action not supported");
        }
    }


    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Method not supported");
    }
}
