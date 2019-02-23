package com.atalantoo;

import org.openqa.selenium.WebDriverException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
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

import com.atalantoo.json.JSONTranslateProcessor;
import com.atalantoo.json.LocaleJSONFooter;
import com.atalantoo.json.LocaleJSONHeader;
import com.atalantoo.json.LocaleJSONLine;
import com.atalantoo.json.LocaleJSONLineAggregator;
import com.atalantoo.json.LocaleJSONLineMapper;
import com.atalantoo.text.TextLineAggregator;
import com.atalantoo.text.TextLineMapper;
import com.atalantoo.text.TextTranslateProcessor;
import com.atalantoo.translator.GoogleUIPhantomJS;

@SpringBootApplication
@EnableBatchProcessing
public class BatchConfig {

	// ARGS *********************************************************
	@Value("${mode}")
	String mode;
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

	@Bean
	Step translateStep() {
		return isModeJSON() ? jsonTranslateStep() : textTranslateStep();
	}

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	GoogleUIPhantomJS translator;

	private boolean isModeJSON() {
		return (mode != null) && (mode.toLowerCase().equals("json"));
	}

	// TEXT *********************************************************

	Step textTranslateStep() {
		System.out.println("using text mode");
		return steps.get("translate").<String, String>chunk(1) //
				.reader(textReader(input)) //
				.processor(textProcessor(input_lang, output_lang)) //
				.writer(textWriter(output)) //
				.faultTolerant().retry(WebDriverException.class).retryLimit(5) //
				.build();
	}

	public ItemProcessor<String, String> textProcessor(String input_lang, String output_lang) {
		return new TextTranslateProcessor(input_lang, output_lang, translator);
	}

	private ItemReader<String> textReader(String input) {
		FlatFileItemReader<String> reader = new FlatFileItemReader<>();
		LineMapper<String> mapper = new TextLineMapper();
		reader.setResource(new FileSystemResource(input));
		reader.setLineMapper(mapper);
		return reader;
	}

	private ItemWriter<String> textWriter(String output) {
		FlatFileItemWriter<String> w = new FlatFileItemWriter<>();
		LineAggregator<String> aggregator = new TextLineAggregator();
		w.setResource(new FileSystemResource(output));
		w.setLineAggregator(aggregator);
		return w;
	}

	// JSON *********************************************************

	Step jsonTranslateStep() {
		System.out.println("using json mode");
		return steps.get("translate").<LocaleJSONLine, LocaleJSONLine>chunk(1) //
				.reader(jsonReader(input)) //
				.processor(jsonProcessor(input_lang, output_lang)) //
				.writer(jsonWriter(output)) //
				.faultTolerant().retry(WebDriverException.class).retryLimit(5) //
				.build();
	}

	public ItemProcessor<LocaleJSONLine, LocaleJSONLine> jsonProcessor(String input_lang, String output_lang) {
		return new JSONTranslateProcessor(input_lang, output_lang, translator);
	}

	private ItemReader<LocaleJSONLine> jsonReader(String input) {
		LineMapper<LocaleJSONLine> mapper = new LocaleJSONLineMapper();
		FlatFileItemReader<LocaleJSONLine> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(input));
		reader.setStrict(false);
		reader.setLineMapper(mapper);
		reader.open(new ExecutionContext());
		reader.setLinesToSkip(1);
		return reader;
	}

	private ItemWriter<LocaleJSONLine> jsonWriter(String output) {
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
