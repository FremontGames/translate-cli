package com.atalantoo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

public class HtmlTest {

	@Test
	public void html_from_en_to_fr() throws Exception {
		String[] args = new String[] { //
				"--mode=html", //
				"--input=" + "src/test/resources/test-html_from_en_to_fr-input.html", //
				"--output=" + "target/test-html_from_en_to_fr-output.html", //
				"--input-lang=" + "en", //
				"--output-lang=" + "fr" };
		int executionErrors = SpringApplication.exit(SpringApplication //
				.run(BatchConfig.class, args));
		assertThat(executionErrors) //
				.isEqualTo(0);
		assertThat(new File("target/test-html_from_en_to_fr-output.html")) //
				.exists() //
				.isFile() //
				.hasSameContentAs(new File("src/test/resources/test-html_from_en_to_fr-expected.html"));
	}
}
