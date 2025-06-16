package com.example.tdd1.post.controller.view;

import com.example.tdd1.post.dto.PostRequestDto;
import com.example.tdd1.post.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @GetMapping("/page")
    public String getPage(Model model) {
        List<Post> postList = new ArrayList<>();
        model.addAttribute("POSTLIST", postList);

        return "post/page";
    }

    @PostMapping("/page")
    public String postPage(PostRequestDto postRequestDto) {

        return "redirect:/page";
    }
}
