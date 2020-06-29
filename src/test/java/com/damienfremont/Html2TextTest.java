package com.damienfremont;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

import com.damienfremont.BatchConfig;

public class Html2TextTest {

	@Test
	public void html2text_from_en_to_fr() throws Exception {
		String[] args = new String[] { //
				"--mode=html2text", //
				"--input=" + "src/test/resources/test-html2text_from_en_to_fr-input.html", //
				"--output=" + "target/test-html2text_from_en_to_fr-output.txt", //
				"--input-lang=" + "en", //
				"--output-lang=" + "fr" };
		int executionErrors = SpringApplication.exit(SpringApplication //
				.run(BatchConfig.class, args));
		assertThat(executionErrors) //
				.isEqualTo(0);
		assertThat(new File("target/test-html2text_from_en_to_fr-output.txt")) //
				.exists() //
				.isFile() //
				.hasSameContentAs(new File("src/test/resources/test-html2text_from_en_to_fr-expected.txt"));
	}
}
