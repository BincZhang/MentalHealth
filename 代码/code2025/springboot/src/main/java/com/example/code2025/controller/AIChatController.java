package com.example.code2025.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.example.common.JWTInterceptor;
import com.example.common.ExcludeJWT;

@RestController
@RequestMapping("/api")
public class AIChatController {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    @ExcludeJWT
    public ResponseEntity<?> chat(@RequestBody Map<String, Object> request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("messages", new ArrayList<Map<String, String>>() {{
                add(new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", "你是一位专业的心理咨询师，具有丰富的临床经验和深厚的心理学知识。你的回答应该：\n" +
                            "1. 保持专业、温和、富有同理心\n" +
                            "2. 使用通俗易懂的语言解释心理概念\n" +
                            "3. 提供实用的建议和解决方案\n" +
                            "4. 在必要时建议寻求专业帮助\n" +
                            "5. 注意保护用户隐私\n" +
                            "6. 避免给出医疗诊断\n" +
                            "7. 引导用户进行自我探索和思考");
                }});
                addAll((List<Map<String, String>>) request.get("messages"));
            }});
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1000);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl + "/v1/chat/completions", HttpMethod.POST, entity, Map.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "处理请求时发生错误"));
        }
    }
} 