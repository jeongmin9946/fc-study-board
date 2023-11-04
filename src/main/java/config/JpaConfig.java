package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    /**
     * CreatedBy, LastModifiedBy 어노테이셔이 사용된 필드에 값을 설정해줌.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("TreeName"); // TODO: Spring Security 인증 기능을 붙이고 나서 수정!!
    }

}
