package com.treename.board.repository;

import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.treename.board.domain.Article;
import com.treename.board.domain.QArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long>
                                         , QuerydslPredicateExecutor<Article> // Generic 타입 중요, 모든 필드의 기본 검색 기능을 자동 추가 (단, 완전 동일 데이터만)
                                         , QuerydslBinderCustomizer<QArticle> // Generic 타입 중요!! EnityPath! 검새기능을 커스텀할 수 있다.
{
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // 기본 검색 기능 제외
        bindings.excludeUnlistedProperties(true);

        // 검색 기능을 사용하 필드 추가.
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);

        // 바인딩된 값(검색어) 하나를 검색할 방법 설정
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);  // like '%{value}%'
        //bindings.bind(root.title).first(StringExpression::likeIgnoreCase);      // like '${value}'

        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
