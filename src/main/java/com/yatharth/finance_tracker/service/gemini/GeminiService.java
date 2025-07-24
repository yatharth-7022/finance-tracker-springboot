package com.yatharth.finance_tracker.service.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yatharth.finance_tracker.dto.ForecastResponse;
import com.yatharth.finance_tracker.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {
    private static final String GEMINI_API_KEY = "AIzaSyAakNMdTZMeqPJ1P4deLa1_uO0TXt-LzVI";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ForecastResponse getExpenseForecast(List<Transaction> expenses) {
        try {
            String geminiResponse = callGeminiAPI(expenses);
            return parseGeminiResponse(geminiResponse);
        } catch (Exception e) {
            log.error("Error getting expense forecast from Gemini: ", e);
            return createFallbackResponse();
        }
    }

    private String callGeminiAPI(List<Transaction> expenses) {
        String prompt = buildPrompt(expenses);
        
        JSONObject requestBody = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject textPart = new JSONObject();

        textPart.put("text", prompt);
        contents.put(new JSONObject().put("parts", new JSONArray().put(textPart)));
        requestBody.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);
        
        return response.getBody();
    }

    private ForecastResponse parseGeminiResponse(String geminiResponse) {
        try {
            // Parse the Gemini API response structure
            JSONObject responseJson = new JSONObject(geminiResponse);
            JSONArray candidates = responseJson.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            String text = parts.getJSONObject(0).getString("text");
            
            // Extract clean JSON from markdown wrapper
            String cleanJson = extractJsonFromMarkdown(text);
            
            // Convert JSON string to our DTO
            ForecastResponse forecastData = objectMapper.readValue(cleanJson, ForecastResponse.class);
            
            // Add current timestamp
            forecastData.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return forecastData;
            
        } catch (Exception e) {
            log.error("Error parsing Gemini response: ", e);
            return createFallbackResponse();
        }
    }

    private ForecastResponse createFallbackResponse() {
        return ForecastResponse.builder()
                .estimatedSpending(0.0)
                .averageDailySpend(0.0)
                .timeRange(ForecastResponse.TimeRange.builder()
                        .start(LocalDateTime.now().toLocalDate().toString())
                        .end(LocalDateTime.now().toLocalDate().toString())
                        .build())
                .totalSpentSoFar(0.0)
                .tipSummary(List.of("Unable to generate forecast at this time"))
                .warnings(List.of("Service temporarily unavailable"))
                .generatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }

    private String buildPrompt(List<Transaction> expenses) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI financial assistant. Based on the user's expenses for the current month:\n\n");

        for (Transaction transaction : expenses) {
            prompt.append("- ").append(transaction.getDate()).append(": ₹").append(transaction.getAmount()).append("\n");
        }

        prompt.append("\nBased on this list of expenses, return the following analysis:\n")
                .append("1. A short summary of total spent so far, average spending per day, and projected total for the full month.\n")
                .append("2. 3 to 5 budgeting tips (in plain language).\n")
                .append("3. Any financial warnings or alerts you observe.\n\n")
                .append("⚠️ IMPORTANT: Respond with only a raw JSON object, no explanations, no formatting, no markdown code block, no triple backticks.\n")
                .append("Start your response directly with '{' and end with '}'. Do NOT include ``` or any extra text.\n\n")
                .append("{\n")
                .append("  \"estimatedSpending\": number,\n")
                .append("  \"averageDailySpend\": number,\n")
                .append("  \"timeRange\": {\n")
                .append("    \"start\": \"YYYY-MM-DD\",\n")
                .append("    \"end\": \"YYYY-MM-DD\"\n")
                .append("  },\n")
                .append("  \"totalSpentSoFar\": number,\n")
                .append("  \"tipSummary\": [\"tip1\", \"tip2\", \"tip3\", \"tip4\", \"tip5\"],\n")
                .append("  \"warnings\": [\"warning1\", \"warning2\"]\n")
                .append("}");
        
        // Add example
        prompt.append("Here is an example:\n")
                .append("{\n")
                .append("  \"estimatedSpending\": 15000.0,\n")
                .append("  \"averageDailySpend\": 500.0,\n")
                .append("  \"timeRange\": {\n")
                .append("    \"start\": \"2025-07-01\",\n")
                .append("    \"end\": \"2025-07-31\"\n")
                .append("  },\n")
                .append("  \"totalSpentSoFar\": 10000.0,\n")
                .append("  \"tipSummary\": [\n")
                .append("    \"Track your daily spending.\",\n")
                .append("    \"Cut down on non-essential items.\",\n")
                .append("    \"Use budgeting apps to stay organized.\"\n")
                .append("  ],\n")
                .append("  \"warnings\": [\n")
                .append("    \"You're on track to overspend by ₹3000. Consider reducing your daily average.\"\n")
                .append("  ]\n")
                .append("}");
        return prompt.toString();
    }

    private String extractJsonFromMarkdown(String text) {
        // Remove markdown code blocks if present
        Pattern jsonPattern = Pattern.compile("```json\\s*\\n([\\s\\S]*?)\\n```", Pattern.MULTILINE);
        Matcher matcher = jsonPattern.matcher(text);
        
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        
        // If no markdown blocks found, try to extract JSON directly
        Pattern directJsonPattern = Pattern.compile("\\{[\\s\\S]*\\}");
        Matcher directMatcher = directJsonPattern.matcher(text);
        
        if (directMatcher.find()) {
            return directMatcher.group().trim();
        }
        
        // If all else fails, return the original text
        return text.trim();
    }
}