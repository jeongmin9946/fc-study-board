package com.treename.board.repository;

import com.treename.board.domain.Article;
import config.JpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// 별도의 테스트 DB를 사용하기 위한 어노테이션
// @ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {


    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository
                           , @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }


    @DisplayName("SELECT 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articleList = articleRepository.findAll();

        // Then
        assertThat(articleList).isNotNull()
                               .hasSize(125);
    }

    @DisplayName("INSERT 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();

        // When
        Article savedArticle = articleRepository.save(Article.of("new Article", "new Content", "#spring"));

        // Then
        long afterCount = articleRepository.count();

        assertThat(afterCount).isEqualTo(previousCount + 1);
    }

    @DisplayName("UPDATE 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        String updatedHashtag = "#springBoot";

        Article article = articleRepository.findById(1L).orElseThrow();
        article.setHashtag(updatedHashtag);

        // When
        Article updatedArticle = articleRepository.saveAndFlush(article);

        // Then
        assertThat(updatedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("DELETE 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();

        Article article = articleRepository.findById(1L).orElseThrow();

        int deletedCommentCount = article.getArticleCommentSet().size();

        // When
        articleRepository.delete(article);

        // Then
        long afterArticleCount = articleRepository.count();
        long afterArticleCommentCount = articleCommentRepository.count();

        assertThat(afterArticleCount).isEqualTo(previousArticleCount - 1);
        assertThat(afterArticleCommentCount).isEqualTo(previousArticleCommentCount - deletedCommentCount);
    }
}
