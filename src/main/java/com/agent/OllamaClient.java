package com.agent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class OllamaClient {
    private final String endpoint;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaClient(String endpoint) {
        this.endpoint = endpoint;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String chat(String model, String userMessage) throws Exception {
        URI uri = new URI(endpoint + "/api/chat");
        Map<String,Object> bodyMap = Map.of(
            "model", model,
            "messages", new Object[] {
                Map.of("role", "user", "content", userMessage)
            },
            "stream", false
        );
        String bodyJson = objectMapper.writeValueAsString(bodyMap);
        HttpRequest req = HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
            .build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new RuntimeException("Ollama API returned status " + resp.statusCode() + ": " + resp.body());
        }
        Map<?,?> respMap = objectMapper.readValue(resp.body(), Map.class);
        Object messageObj = respMap.get("message");
        if (messageObj instanceof Map) {
            Map<?,?> messageMap = (Map<?,?>) messageObj;
            Object content = messageMap.get("content");
            return content != null ? content.toString() : "";
        }
        // alternative / fallback
        return resp.body();
    }

    public String generate(String model, String prompt) throws Exception {
        URI uri = new URI(endpoint + "/api/generate");
        Map<String,Object> bodyMap = Map.of(
            "model", model,
            "prompt", prompt,
            "stream", false
        );
        String bodyJson = objectMapper.writeValueAsString(bodyMap);
        HttpRequest req = HttpRequest.newBuilder()
            .uri(uri)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
            .build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new RuntimeException("Ollama API returned status " + resp.statusCode() + ": " + resp.body());
        }
        Map<?,?> respMap = objectMapper.readValue(resp.body(), Map.class);
        Object response = respMap.get("response");
        return response != null ? response.toString() : "";
    }
}
