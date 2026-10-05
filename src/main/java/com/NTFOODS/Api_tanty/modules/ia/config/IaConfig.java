package com.NTFOODS.Api_tanty.modules.ia.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class IaConfig {

    @Value("${openai.api-key:}")
    private String apiKey;

    @Value("${openai.base-url:https://openrouter.ai/api/v1/chat/completions}")
    private String baseUrl;

    @Value("${openai.model:openai/gpt-oss-120b}")
    private String model;

    @Value("${openai.temperature:0.2}")
    private double defaultTemperature;

    @Value("${openai.max-tokens:20000}")
    private int maxTokens;

    @Value("${openai.timeout-seconds:60}")
    private int timeoutSeconds;

    // ── IA Prévision config ──
    @Value("${ia.prevision.horizon-defaut:30}")
    private int previsionHorizonDefaut;

    @Value("${ia.prevision.min-historique-jours:14}")
    private int previsionMinHistoriqueJours;

    @Value("${ia.prevision.max-historique-jours:730}")
    private int previsionMaxHistoriqueJours;

    @Value("${ia.alertes.auto-generer:true}")
    private boolean alertesAutoGenerer;

    @Bean(name = "iaRestTemplate")
    public RestTemplate iaRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(10).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(timeoutSeconds).toMillis());
        return new RestTemplate(factory);
    }

    @Bean(name = "iaWebClient")
    public WebClient iaWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(timeoutSeconds))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey != null && !apiKey.isBlank() ? "Bearer " + apiKey : "")
                .defaultHeader("HTTP-Referer", "https://tanty-erp.local")
                .defaultHeader("X-Title", "TANTY ERP")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public String getApiKey() { return apiKey; }
    public String getBaseUrl() { return baseUrl; }
    public String getModel() { return model; }
    public double getDefaultTemperature() { return defaultTemperature; }
    public int getMaxTokens() { return maxTokens; }
    public int getPrevisionHorizonDefaut() { return previsionHorizonDefaut; }
    public int getPrevisionMinHistoriqueJours() { return previsionMinHistoriqueJours; }
    public int getPrevisionMaxHistoriqueJours() { return previsionMaxHistoriqueJours; }
    public boolean isAlertesAutoGenerer() { return alertesAutoGenerer; }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    public HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }
        headers.set("HTTP-Referer", "https://tanty-erp.local");
        headers.set("X-Title", "TANTY ERP");
        return headers;
    }
}
