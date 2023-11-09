package com.treename.board.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.treename.board.domain.ArticleComment;
import com.treename.board.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>
                                                , QuerydslPredicateExecutor<ArticleComment>
                                                , QuerydslBinderCustomizer<QArticleComment>
{
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 기본 검색 기능 제외
        bindings.excludeUnlistedProperties(true);

        // 검색 기능을 사용하 필드 추가.
        bindings.including(root.content, root.createdAt, root.createdBy);

        // 바인딩된 값(검색어) 하나를 검색할 방법 설정
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
