package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        String response = "{\"message\": \"Olá, mundo do Java puro!\"}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
