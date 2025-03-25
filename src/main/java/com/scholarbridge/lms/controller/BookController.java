package com.scholarbridge.lms.controller;

import com.scholarbridge.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/openlibrary")
    public String searchOpenLibrary(@RequestParam String query) {
        return bookService.searchOpenLibrary(query);
    }

    @GetMapping("/googlebooks")
    public String searchGoogleBooks(@RequestParam String query) {
        return bookService.searchGoogleBooks(query);
    }
}