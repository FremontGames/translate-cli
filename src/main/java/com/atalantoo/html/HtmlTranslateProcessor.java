package com.atalantoo.html;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.atalantoo.translator.Translator;

public class HtmlTranslateProcessor implements ItemProcessor<String, String> {

	String input_lang;
	String output_lang;
	Translator translator;

	public HtmlTranslateProcessor(String input_lang, String output_lang, Translator translator) {
		super();
		this.input_lang = input_lang;
		this.output_lang = output_lang;
		this.translator = translator;
	}

	@Override
	public String process(String item) throws Exception {
		if (StringUtils.isBlank(item))
			return "";
		String html = translator.translate(item, input_lang, output_lang);
		String text = html //
				.replaceAll("> ", ">") //
				.replaceAll(" <", " <");
		return text;
	}

}
