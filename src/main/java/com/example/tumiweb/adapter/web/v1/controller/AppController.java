package com.example.tumiweb.adapter.web.v1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
  @GetMapping("/")
  public String app() {
    return "App đã hoạt động";
  }

  @GetMapping("/author")
  public String getAuthor() {
    return "Nguyễn Đình Huân - chúc em một đời bình an <3 (H)";
  }

}
