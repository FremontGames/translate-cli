package com.atalantoo;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

public class One {
	@Test
	public void generate() throws Exception {
		String ts = String.valueOf(Math.random() * 999999);
		String path = System.getProperty("user.dir");
		String file = path + "\\..\\Project 2048 Cars\\Assets\\Project 2048\\Resources\\i18n\\locale-%s.json";
		String src_lang = "en";
		String dest_lang= "de";
		assertThat(SpringApplication.exit(SpringApplication.run(BatchConfig.class,
				new String[] { //
						"--run.id=" + ts, //
						"--src=" + format(file, src_lang), //
						"--dest=" + format(file, dest_lang), //
						"--src_lang=" + src_lang, //
						"--dest_lang=" + dest_lang }))).isEqualTo(0);
	}
}
