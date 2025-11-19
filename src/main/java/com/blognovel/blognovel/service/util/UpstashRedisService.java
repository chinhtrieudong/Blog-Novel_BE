package com.blognovel.blognovel.service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpstashRedisService {

    private final String baseUrl;
    private final String token;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UpstashRedisService(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public void set(String key, String value, long ttlSeconds) throws Exception {
        String url = baseUrl + "/setex/" + key;
        String body = String.format("{\"ex\": %d, \"data\": \"%s\"}", ttlSeconds, value);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to set Redis key: " + response.body());
        }
    }

    public String get(String key) throws Exception {
        String url = baseUrl + "/get/" + key;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            return null;
        }
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get Redis key: " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        return json.get("result").asText();
    }

    public void delete(String key) throws Exception {
        String url = baseUrl + "/del/" + key;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to delete Redis key: " + response.body());
        }
    }

    public boolean exists(String key) throws Exception {
        String url = baseUrl + "/exists/" + key;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to check Redis key exists: " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        return json.get("result").asInt() == 1;
    }

    public void ping() throws Exception {
        String url = baseUrl + "/ping";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Redis ping failed: " + response.body());
        }
    }
}
