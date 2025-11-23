package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.models.Product;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductHandler implements HttpHandler {

    private static final List<Product> products = new ArrayList<>();
    private static final Gson gson = new Gson();

    static {
        products.add(new Product(1, "Notebook", 3500.00));
        products.add(new Product(2, "Mouse Gamer", 120.00));
        products.add(new Product(3, "Teclado Mecânico", 450.00));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/products")) {
            if (method.equalsIgnoreCase("GET")) {
                handleListProducts(exchange);
                return;
            }

            if (method.equalsIgnoreCase("POST")) {
                handleCreateProduct(exchange);
                return;
            }

            handleMethodNotAllowed(exchange);
            return;
        }

        if (path.startsWith("/products/")) {
            if (method.equalsIgnoreCase("GET")) {
                handleListProductById(exchange);
                return;
            }

            if (method.equalsIgnoreCase("DELETE")) {
                handleDeleteProduct(exchange);
                return;
            }

            handleMethodNotAllowed(exchange);
            return;
        }

        String json = "{\"error\":\"Rota não encontrada\"}";
        byte[] bytes = json.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(404, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }

    private void handleListProducts(HttpExchange exchange) throws IOException {
        String json = gson.toJson(products);
        byte[] responseBytes = json.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
        exchange.close();
    }

    private void handleListProductById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts.length < 3) {
            String errorJson = "{\"error\":\"Requisição inválida\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        String idStr = parts[2];
        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            String errorJson = "{\"error\":\"ID inválido\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        Product found = null;
        for (Product p : products) {
            if (p.id == id) {
                found = p;
                break;
            }
        }

        if (found == null) {
            String errorJson = "{\"error\":\"Produto não encontrado\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        String json = gson.toJson(found);
        byte[] responseBytes = json.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
        exchange.close();
    }

    private void handleCreateProduct(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());

        if (requestBody == null || requestBody.isBlank()) {
            String errorJson = "{\"error\":\"Empty body\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        Product product = gson.fromJson(requestBody, Product.class);

        int nextId = 1;
        for (Product p : products) {
            if (p.id >= nextId) {
                nextId = p.id + 1;
            }
        }
        product.id = nextId;

        products.add(product);

        String json = gson.toJson(product);
        byte[] responseBytes = json.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
        exchange.close();
    }

    private void handleDeleteProduct(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts.length < 3) {
            String errorJson = "{\"error\":\"Requisição inválida\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        String idStr = parts[2];
        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            String errorJson = "{\"error\":\"ID inválido\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        boolean removed = products.removeIf(p -> p.id == id);

        if (!removed) {
            String errorJson = "{\"error\":\"Produto não encontrado\"}";
            byte[] errorBytes = errorJson.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            exchange.close();
            return;
        }

        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "{\"error\": \"Método não permitido\"}";
        byte[] responseBytes = response.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(405, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
        exchange.close();
    }
}
