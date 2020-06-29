package com.damienfremont;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

import com.damienfremont.BatchConfig;

public class JsonTest {

	@Test
	public void json_from_en_to_fr() throws Exception {
		String[] args = new String[] { //
				"--mode=json", //
				"--input=" + "src/test/resources/test-json_from_en_to_fr-input.json", //
				"--output=" + "target/test-json_from_en_to_fr-output.json", //
				"--input-lang=" + "en", //
				"--output-lang=" + "fr" };
		int executionErrors = SpringApplication.exit(SpringApplication //
				.run(BatchConfig.class, args));
		assertThat(executionErrors) //
				.isEqualTo(0);
		assertThat(new File("target/test-json_from_en_to_fr-output.json")) //
				.exists() //
				.isFile() //
				.hasSameContentAs(new File("src/test/resources/test-json_from_en_to_fr-expected.json"));
	}
}
