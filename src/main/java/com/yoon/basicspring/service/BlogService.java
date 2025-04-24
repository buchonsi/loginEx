package com.yoon.basicspring.service;

import com.yoon.basicspring.domain.Article;
import com.yoon.basicspring.dto.AddArticleRequest;
import com.yoon.basicspring.dto.UpdateArticleRequest;
import com.yoon.basicspring.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found:  " + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }

}
