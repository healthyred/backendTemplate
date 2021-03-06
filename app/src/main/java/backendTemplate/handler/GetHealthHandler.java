package backendTemplate.handler;

import io.undertow.server.HttpServerExchange;

import javax.inject.Singleton;

@Singleton
public class GetHealthHandler extends BasicHttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        respondJson(exchange, "{\"status\": \"UP\"}");
    }
}
