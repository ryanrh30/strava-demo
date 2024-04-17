package com.uct.stravademo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.uct.stravademo.OAuth.StravaTokenResponse;
import com.uct.stravademo.model.StravaActivity;
import com.uct.stravademo.service.StravaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;;import java.util.List;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/strava")
public class StravaController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${strava.clientId}")
    private String clientId;

    @Value("${strava.clientSecret}")
    private String clientSecret;

    @Value("${strava.redirectUri}")
    private String redirectUri;

    private final String authUrl = "http://www.strava.com/oauth/authorize";
    private final String tokenUrl = "https://www.strava.com/oauth/token";


    @GetMapping("/authorize")
    public String redirectToStrava() {
        String url = UriComponentsBuilder.fromUriString(authUrl)
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", redirectUri)
                .queryParam("approval_prompt", "force") // or "auto" if you don't want to force approval every time
                .queryParam("scope", "read,read_all,profile:read_all,profile:write,activity:read,activity:read_all,activity:write") // Adjust the scope based on your needs
                .toUriString();

        return "redirect:" + url;
    }


    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<StravaTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, StravaTokenResponse.class);

        // Log the access token
        System.out.println("Access Token: " + response.getBody().getAccess_token());

        // You can return a simple string response indicating success
        return ResponseEntity.ok("Token received, check the console for the access token.");
    }
    @Autowired
    private StravaService stravaService;

    @GetMapping("/activities")
    @ResponseBody
    public ResponseEntity<List<StravaActivity>> getActivities() {
        List<StravaActivity> activities = stravaService.getActivities();
        if(activities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activities);
    }

}

