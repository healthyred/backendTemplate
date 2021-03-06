package backendTemplate.handler;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * Basic Http Handler that uses Gson to parse a lot of requests. It just makes handler requests and responses
 * much easier.
 * */
public abstract class BasicHttpHandler implements HttpHandler {

    protected String getQueryParameter(HttpServerExchange exchange, String name) {
        return getQueryParameter(exchange.getQueryParameters(), name);
    }

    protected String getQueryParameter(Map<String, Deque<String>> parms, String name) {
        try {
            String raw = parms.getOrDefault(name, new LinkedList<>()).peekFirst();
            if (StringUtils.isEmpty(raw)) {
                return raw;
            }

            return URLDecoder.decode(raw, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return String.format("Exception: %s", e.getMessage());
        }
    }

    protected String getPathVariable(HttpServerExchange exchange, String name) {
        return getQueryParameter(exchange, name);
    }

    protected void writeBodyAsUtf8(HttpServerExchange exchange, String body) {
        exchange.getResponseSender().send(body, StandardCharsets.UTF_8);
    }

    protected String getBodyUtf8(HttpServerExchange exchange) {
        try {
            return IOUtils.toString(exchange.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected JsonObject parseJsonObject(HttpServerExchange exchange) {
        return parseObj(getBodyUtf8(exchange));
    }

    protected void respondJson(HttpServerExchange ex, int status, String body) {
        ex.setStatusCode(status);
        ex.getResponseHeaders().put(HttpString.tryFromString("Content-Type"), "application/json");
        writeBodyAsUtf8(ex, body);
    }

    protected void respondJson(HttpServerExchange ex, String body) {
        respondJson(ex, 200, body);
    }

    protected void respondJson(HttpServerExchange ex, Object body) {
        Gson instance = buildImpl().create();
        respondJson(ex, instance.toJson(body));
    }

    private static GsonBuilder buildImpl() {
        return (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).disableHtmlEscaping();
    }

    public static JsonElement parse(String s) {
        try {
            return JsonParser.parseString(s);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static JsonObject parseObj(String s) {
        try {
            return parse(s).getAsJsonObject();
        } catch (IllegalStateException var2) {
            throw new RuntimeException("Not an object");
        }
    }

}
