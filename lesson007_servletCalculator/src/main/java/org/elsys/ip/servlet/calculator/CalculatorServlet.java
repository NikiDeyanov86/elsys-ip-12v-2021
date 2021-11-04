package org.elsys.ip.servlet.calculator;

import org.elsys.ip.calculator.CommandExecutor;
import org.elsys.ip.calculator.CommandFactory;
import org.elsys.ip.calculator.Memory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CalculatorServlet extends HttpServlet {
    private final Map<String, CalculatorCore> userCalculators = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> pathArgs = Arrays.stream(req.getPathInfo().split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());
        if (pathArgs.size() == 0) {
            resp.setStatus(400);
            return;
        }

        String command = pathArgs.get(0);
        List<String> args = pathArgs.stream().skip(1).collect(Collectors.toList());

        String username = "default";
        if (req.getQueryString() != null) {
            Optional<String> userQuery = Arrays.stream(req.getQueryString().split("&")).filter(x -> x.startsWith("user=")).findFirst();
            if (userQuery.isPresent()) {
                username = userQuery.get().replace("user=", "");
            }
        }

        if (!userCalculators.containsKey(username)) {
            userCalculators.put(username, new CalculatorCore());
        }

        String result = userCalculators.get(username).execute(command, args);
        resp.getWriter().println(result);
    }
}
