package com.atalantoo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

public class JsonTest {

	@Test
	public void json_from_en_to_it() throws Exception {
		String[] args = new String[] { //
				"--run.id=" + String.valueOf(Math.random() * 999999), //
				"--src=" + "src/test/resources/test-json_from_en_to_it-input.json", //
				"--dest=" + "target/test-json_from_en_to_it-output.json", //
				"--src_lang=" + "en", //
				"--dest_lang=" + "it" };
		int executionErrors = SpringApplication.exit(SpringApplication //
				.run(BatchConfig.class, args));
		assertThat(executionErrors) //
				.isEqualTo(0);
		assertThat(new File("target/test-json_from_en_to_it-output.json")) //
				.exists() //
				.isFile() //
				.hasSameContentAs(new File("src/test/resources/test-json_from_en_to_it-expected.json"));
	}
}
