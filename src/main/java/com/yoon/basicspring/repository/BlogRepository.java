package com.yoon.basicspring.repository;

import com.yoon.basicspring.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
