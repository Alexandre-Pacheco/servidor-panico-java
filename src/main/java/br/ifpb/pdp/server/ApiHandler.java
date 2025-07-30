package br.ifpb.pdp.server;

import br.ifpb.pdp.models.User;
import br.ifpb.pdp.patterns.adapter.AdminAuthService;
import br.ifpb.pdp.patterns.facade.PanicSystemFacade;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler implements HttpHandler {

    private final PanicSystemFacade facade = new PanicSystemFacade(new AdminAuthService());
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String path = exchange.getRequestURI().getPath().replace("/api", "");
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        try {
            if (path.equals("/users") && method.equals("GET")) {
                response = gson.toJson(facade.getRegisteredUsers());
            } else if (path.equals("/users") && method.equals("POST")) {
                JsonObject body = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), JsonObject.class);
                User newUser = facade.addUser(body.get("id").getAsString(), body.get("name").getAsString(), body.get("type").getAsString());
                if (newUser != null) {
                    response = gson.toJson(newUser);
                } else {
                    statusCode = 409;
                    response = createErrorResponse("Matrícula já existe.");
                }
            } else if (path.startsWith("/users/") && method.equals("DELETE")) {
                String userId = path.substring(path.lastIndexOf('/') + 1);
                facade.removeUserById(userId);
                response = createSuccessResponse("Usuário removido com sucesso.");
            } else if (path.equals("/login") && method.equals("POST")) {
                 JsonObject body = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), JsonObject.class);
                 boolean success = facade.loginAdmin(body.get("cpf").getAsString(), body.get("password").getAsString());
                 if(success) {
                    response = createSuccessResponse("Login bem-sucedido.");
                 } else {
                    statusCode = 401;
                    response = createErrorResponse("CPF ou senha inválidos.");
                 }
            } else if (path.equals("/panic") && method.equals("POST")) {
                JsonObject body = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), JsonObject.class);
                String userId = body.get("userId").getAsString();
                facade.getRegisteredUserById(userId).ifPresent(facade::triggerPanic);
                response = createSuccessResponse("Alerta de pânico recebido.");
            } else if (path.equals("/incidents") && method.equals("GET")) {
                response = facade.getActiveIncidentsAsJson();
            } else if (path.startsWith("/incidents/") && method.equals("POST")) {
                String[] parts = path.split("/");
                int incidentId = Integer.parseInt(parts[2]);
                String action = parts[3];
                facade.handleIncidentAction(incidentId, action);
                response = createSuccessResponse("Ação '" + action + "' executada no incidente #" + incidentId);
            } else {
                statusCode = 404;
                response = createErrorResponse("Endpoint não encontrado.");
            }
        } catch (Exception e) {
            statusCode = 500;
            response = createErrorResponse("Erro interno no servidor: " + e.getMessage());
            e.printStackTrace();
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String createErrorResponse(String message) {
        return gson.toJson(Map.of("error", message));
    }

    private String createSuccessResponse(String message) {
        return gson.toJson(Map.of("message", message));
    }
}
