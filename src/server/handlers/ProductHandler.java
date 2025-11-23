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

    // Lista simulando um banco em memória
    private static final List<Product> products = new ArrayList<>();

    // Instância do Gson para converter objeto <-> JSON
    private static final Gson gson = new Gson();

    // Bloco de inicialização com produtos de exemplo
    static {
        products.add(new Product(1, "Notebook", 3500.00));
        products.add(new Product(2, "Mouse Gamer", 120.00));
        products.add(new Product(3, "Teclado Mecânico", 450.00));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (!method.equalsIgnoreCase("GET")) {
            handleMethodNotAllowed(exchange);
            return;
        }

        if (path.equals("/products")) {
            handleListProducts(exchange);
            return;
        }

        if (path.startsWith("/products/")) {
            handleListProductById(exchange);
            return;
        }

        String json = "{\"error\":\"Rota não encontrada\"}";
        byte[] bytes = json.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(404, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }


    // Retorna todos os produtos em JSON
    private void handleListProducts(HttpExchange exchange) throws IOException {
        String json = gson.toJson(products); // Converte a lista para JSON
        byte[] responseBytes = json.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private void handleListProductById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        // Verifica se veio /products/{id}
        if (parts.length < 3) {
            String errorJson = "{\"error\":\"Requisição inválida\"}";
            byte[] errorBytes = errorJson.getBytes();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            exchange.getResponseBody().write(errorBytes);
            exchange.close();
            return;
        }

        // Pega o ID como texto
        String idStr = parts[2];

        int id;
        try {
            // Converte "3" -> 3
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            String errorJson = "{\"error\":\"ID inválido\"}";
            byte[] errorBytes = errorJson.getBytes();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, errorBytes.length);
            exchange.getResponseBody().write(errorBytes);
            exchange.close();
            return;
        }

        // Procura o produto na lista
        Product found = null;
        for (Product p : products) {
            if (p.id == id) {
                found = p;
                break;
            }
        }

        // Se não achou -> 404
        if (found == null) {
            String errorJson = "{\"error\":\"Produto não encontrado\"}";
            byte[] errorBytes = errorJson.getBytes();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, errorBytes.length);
            exchange.getResponseBody().write(errorBytes);
            exchange.close();
            return;
        }

        // Se achou -> retorna o produto em JSON
        String json = gson.toJson(found);
        byte[] responseBytes = json.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    // Retorna erro para métodos não suportados (POST, PUT, DELETE, etc)
    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "{\"error\": \"Método não permitido\"}";
        byte[] responseBytes = response.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(405, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
