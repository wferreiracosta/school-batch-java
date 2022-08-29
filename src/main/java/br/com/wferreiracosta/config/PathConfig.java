package br.com.wferreiracosta.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class PathConfig {

    @Value("${tech_talk.path.in}")
    private String in;

}
