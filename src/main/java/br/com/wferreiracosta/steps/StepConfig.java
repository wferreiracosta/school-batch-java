package br.com.wferreiracosta.steps;

import br.com.wferreiracosta.models.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class StepConfig {

    private static String STEP_NAME = "school_step";

    private final DataSource dataSource;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step(JsonItemReader<Student> reader, JdbcBatchItemWriter<Student> writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Student, Student>chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Student> reader(@Value("#{jobParameters['inputFileName']}") String inputFilenName) {
        return new JsonItemReaderBuilder<Student>()
                .name(STEP_NAME.concat("_reader"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Student.class))
                .resource(new ClassPathResource(inputFilenName))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Student> writer() {
        return new JdbcBatchItemWriterBuilder<Student>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into student(ra,nome) values (:ra,:nome)")
                .dataSource(dataSource)
                .build();
    }

}
