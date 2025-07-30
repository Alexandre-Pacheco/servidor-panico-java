package br.ifpb.pdp.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", new ApiHandler());
        server.setExecutor(null);
        server.start();
        System.out.println(">>> Servidor Java iniciado na porta " + port);
        System.out.println(">>> O backend está pronto para receber requisições do frontend.");
    }
}
