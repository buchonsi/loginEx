package com.yoon.basicspring.controller;

import com.yoon.basicspring.domain.Article;
import com.yoon.basicspring.dto.AddArticleRequest;
import com.yoon.basicspring.dto.ArticleResponse;
import com.yoon.basicspring.dto.UpdateArticleRequest;
import com.yoon.basicspring.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> save(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll().stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> delete(@PathVariable Long id) {
        blogService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);
        return ResponseEntity.ok(new ArticleResponse(updatedArticle));
    }
}
