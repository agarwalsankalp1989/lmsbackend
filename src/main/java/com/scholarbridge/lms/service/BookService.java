package com.scholarbridge.lms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String searchOpenLibrary(String query) {
        String url = "http://openlibrary.org/search.json?q=" + query;
        return restTemplate.getForObject(url, String.class);
    }

    public String searchGoogleBooks(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        return restTemplate.getForObject(url, String.class);
    }
}