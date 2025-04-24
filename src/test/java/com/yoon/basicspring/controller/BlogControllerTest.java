package com.yoon.basicspring.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoon.basicspring.domain.Article;
import com.yoon.basicspring.dto.AddArticleRequest;
import com.yoon.basicspring.dto.UpdateArticleRequest;
import com.yoon.basicspring.repository.BlogRepository;
import com.yoon.basicspring.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    BlogRepository blogRepository;
    @Autowired
    private BlogService blogService;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        //given : 블로그 글에 추가할 요청 객체를 만든다
        String url = "/api/articles";
        String title = "Title_Test";
        String content = "Content_Test";
        AddArticleRequest request = new AddArticleRequest(title, content);

        //when : 블로그 글 추가 api 요청을 보낸다. 요청 타입은 JSON
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then : 응답코드가 201 인지 확인, 전체를 조회해 크기가 1인지 확인 실제 저장된 데이터와 요청값 확인
        result.andExpect(status().isCreated());

        List<Article> allArticles = blogRepository.findAll();
        assertThat(allArticles.size()).isEqualTo(1);
        assertThat(allArticles.get(0).getTitle()).isEqualTo(title);
        assertThat(allArticles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("모든 아티클을 조회한다")
    @Test
    public void findAllArticles() throws Exception {
        //given 블로그 글을 저장한다.
        String url = "/api/articles";
        String title = "제목1";
        String content = "본문1";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());


        //when 목록 api를 호출 한다.
        ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then 응답이 200, 반환 받은 값 중 첫번째가 title과 일치한다.
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("아티클 하나를 조회한다")
    @Test
    public void findArticleById() throws Exception {
        //given
        String url = "/api/articles/{id}";
        String title = "title";
        String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());



        //when
        ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId())
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("아티클을 삭제에 성공한다")
    @Test
    public void deleteArticle() throws Exception {
        String url = "/api/articles/{id}";
        String title = "title";
        String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());


        ResultActions resultActions = mockMvc.perform(delete(url, savedArticle.getId())
                .accept(MediaType.APPLICATION_JSON));

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }
    
    @DisplayName("아티클을 수정에 성공한다")
    @Test
    public void updateArticle() throws Exception {
        String url = "/api/articles/{id}";
        String title = "title";
        String content = "content";
        String modifiedTitle = "modifiedTitle";
        String modifiedContent = "modifiedContent";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest(modifiedTitle, modifiedContent);
        String request = objectMapper.writeValueAsString(updateArticleRequest);

        ResultActions resultActions = mockMvc.perform(put(url, savedArticle.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(modifiedTitle))
                .andExpect(jsonPath("$.content").value(modifiedContent));

        Article article = blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(modifiedTitle);
        assertThat(article.getContent()).isEqualTo(modifiedContent);
    }
    
}