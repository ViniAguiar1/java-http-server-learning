package server;

import com.sun.net.httpserver.HttpServer;
import server.handlers.HelloHandler;
import server.handlers.ProductHandler;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/hello", new HelloHandler());
        server.createContext("/products", new ProductHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor rodando na porta 8080");
    }
}
