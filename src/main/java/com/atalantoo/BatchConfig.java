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

import com.atalantoo.json.LocaleJSONFooter;
import com.atalantoo.json.LocaleJSONHeader;
import com.atalantoo.json.LocaleJSONLine;
import com.atalantoo.json.LocaleJSONLineAggregator;
import com.atalantoo.json.LocaleJSONLineMapper;
import com.atalantoo.translator.GoogleUIPhantomJS;

@SpringBootApplication
@EnableBatchProcessing
public class BatchConfig {

	// ARGS *********************************************************
	@Value("${input}")
	String input;
	@Value("${output}")
	String output;
	@Value("${input-lang}")
	String input_lang;
	@Value("${output-lang}")
	String output_lang;

	// IMPL *********************************************************

	@Autowired
	JobBuilderFactory jobs;

	@Bean
	Job job() {
		String jobId = String.valueOf(Math.random() * 999999);
		return jobs.get(jobId) //
				.start(translateStep()) //
				.build();
	}

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	Step translateStep() {
		return steps.get("translate").<LocaleJSONLine, LocaleJSONLine> chunk(1) //
				.reader(reader(input)) //
				.processor(processor(input_lang, output_lang)) //
				.writer(writer(output)) //
				.faultTolerant().retry(WebDriverException.class).retryLimit(5) //
				.build();
	}

	// CUSTOM *********************************************************

	@Autowired
	GoogleUIPhantomJS translator;

	public TranslateProcessor processor(String input_lang, String output_lang) {
		return new TranslateProcessor(input_lang, output_lang, translator);
	}

	// GENERIC *********************************************************

	private ItemReader<LocaleJSONLine> reader(String input) {
		LineMapper<LocaleJSONLine> mapper = new LocaleJSONLineMapper();
		FlatFileItemReader<LocaleJSONLine> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(input));
		reader.setStrict(false);
		reader.setLineMapper(mapper);
		reader.open(new ExecutionContext());
		reader.setLinesToSkip(1);
		return reader;
	}

	private ItemWriter<LocaleJSONLine> writer(String output) {
		LineAggregator<LocaleJSONLine> aggregator = new LocaleJSONLineAggregator();
		FlatFileItemWriter<LocaleJSONLine> w = new FlatFileItemWriter<>();
		w.setResource(new FileSystemResource(output));
		w.setShouldDeleteIfEmpty(true);
		w.setLineAggregator(aggregator);
		w.setHeaderCallback(new LocaleJSONHeader());
		w.setFooterCallback(new LocaleJSONFooter());
		return w;
	}

}
