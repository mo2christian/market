package com.mo2christian.market.function;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Launcher implements HttpFunction {

    private final Action action;
    private Logger logger = Logger.getLogger("Launcher");

    public Launcher() {
        action = new Action();
    }

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        String body = httpRequest.getReader().lines().collect(Collectors.joining());
        logger.info(String.format("doPost, body = %s", body));
        String response = action.handleRequest(body, httpRequest.getHeaders()).get();
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write(response);
    }
}
