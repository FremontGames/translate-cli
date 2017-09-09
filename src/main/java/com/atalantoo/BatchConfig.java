package com.atalantoo;

import org.openqa.selenium.WebDriverException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

@SpringBootApplication
@EnableBatchProcessing
public class BatchConfig {

	// ARGS *********************************************************
	@Value("${run.id}")
	String id;
	@Value("${src}")
	String src;
	@Value("${dest}")
	String dest;
	@Value("${src_lang}")
	String src_lang;
	@Value("${dest_lang}")
	String dest_lang;

	// IMPL *********************************************************

	@Autowired
	JobBuilderFactory jobs;

	@Bean
	Job job() {
		return jobs.get(id) //
				.start(translateStep()) //
				.build();
	}

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	Step translateStep() {
		return steps.get("translate").<LocaleJSONLine, LocaleJSONLine> chunk(1) //
				.reader(reader(src)) //
				.processor(processor(src_lang, dest_lang)) //
				.writer(writer(dest)) //
				.faultTolerant().retry(WebDriverException.class).retryLimit(5) //
				.build();
	}

	// CUSTOM *********************************************************

	public TranslateProcessor processor(String src_lang, String dest_lang) {
		return new TranslateProcessor(src_lang, dest_lang);
	}

	// GENERIC *********************************************************

	private ItemReader<LocaleJSONLine> reader(String src) {
		LineMapper<LocaleJSONLine> mapper = new LocaleJSONLineMapper();
		FlatFileItemReader<LocaleJSONLine> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(src));
		reader.setStrict(false);
		reader.setLineMapper(mapper);
		reader.open(new ExecutionContext());
		reader.setLinesToSkip(1);
		return reader;
	}

	private ItemWriter<LocaleJSONLine> writer(String dest) {
		LineAggregator<LocaleJSONLine> aggregator = new LocaleJSONLineAggregator();
		FlatFileItemWriter<LocaleJSONLine> w = new FlatFileItemWriter<>();
		w.setResource(new FileSystemResource(dest));
		w.setShouldDeleteIfEmpty(true);
		w.setLineAggregator(aggregator);
		w.setHeaderCallback(new LocaleJSONHeader());
		w.setFooterCallback(new LocaleJSONFooter());
		return w;
	}

}
