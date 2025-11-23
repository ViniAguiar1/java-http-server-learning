package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.models.Product;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductHandler implements HttpHandler {
    private static final List<Product> products = new ArrayList<>();

    static {
        products.add(new Product(1, "Notebook", 3500.00));
        products.add(new Product(2, "Mouse Gamer", 120.00));
        products.add(new Product(3, "Teclado Mecânico", 450.00));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException{
        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            json.append("{")
                    .append("\"id\":").append(p.id).append(",")
                    .append("\"name\":\"").append(p.name).append("\",")
                    .append("\"price\":").append(p.price)
                    .append("}");

            if (i < products.size() - 1) json.append(",");
        }

        json.append("]");

        byte[] bytes = json.toString().getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}
