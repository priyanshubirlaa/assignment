package com.example.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateSummary(String blogContent) {
        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"Summarize this blog: " + blogContent + "\"}]}]}";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey
            ).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                String response = scanner.useDelimiter("\\A").next();
                return extractSummary(response);
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String extractSummary(String jsonResponse) {
        return JsonParser.parseString(jsonResponse)
                .getAsJsonObject()
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
    }
}
