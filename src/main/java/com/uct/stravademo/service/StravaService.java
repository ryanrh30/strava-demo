package com.uct.stravademo.service;

import com.uct.stravademo.model.StravaActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class StravaService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${strava.token}")
    private String accessToken;

    @Value("${strava.api.activities}")
    private String activitiesUrl;

    public List<StravaActivity> getActivities() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            System.out.println("Using Access Token: " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<StravaActivity[]> response = restTemplate.exchange(
                    activitiesUrl, HttpMethod.GET, entity, StravaActivity[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.asList(response.getBody());
            } else {
                // Log error details
                System.out.println("Error: " + response.getStatusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
