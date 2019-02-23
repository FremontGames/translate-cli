package com.atalantoo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

public class TextTest {

	@Test
	public void text_from_en_to_fr() throws Exception {
		String[] args = new String[] { //
				"--mode=text", //
				"--input=" + "src/test/resources/test-text_from_en_to_fr-input.txt", //
				"--output=" + "target/test-text_from_en_to_fr-output.txt", //
				"--input-lang=" + "en", //
				"--output-lang=" + "fr" };
		int executionErrors = SpringApplication.exit(SpringApplication //
				.run(BatchConfig.class, args));
		assertThat(executionErrors) //
				.isEqualTo(0);
		assertThat(new File("target/test-text_from_en_to_fr-output.txt")) //
				.exists() //
				.isFile() //
				.hasSameContentAs(new File("src/test/resources/test-text_from_en_to_fr-expected.txt"));
	}
}
