package com.treename.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class) // JPAConfig - Auditor를 사용 한다.
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Mysql의 AutoIncrement방식.!!
    private Long id;

    @Setter @Column(nullable = false) private String title;                   // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag;             // 해시태그

    // ArticeComment의 ToString에서도 Article을 참조한다.
    // 이로 인해 서로 계속 순환참조를 하게 되는 것을 방지!!!
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleCommentSet = new LinkedHashSet<>();


    // Meta-data
    // JPA Auditing기능으로 데이터 자동 세팅 (생성자 및 수정자는 JpaConfig-auditorAware 토대로 처리)
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;             // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy;        // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt;       // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;  // 수정자


    protected Article() { }

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Pattern variable 방식
        if (!(o instanceof Article article)) return false;
        // id != null 을 통해 key 값이 없을때느 다르도록.
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
