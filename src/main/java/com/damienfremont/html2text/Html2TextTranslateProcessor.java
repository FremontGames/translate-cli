package com.damienfremont.html2text;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.damienfremont.translator.Translator;

public class Html2TextTranslateProcessor implements ItemProcessor<String, String> {

	String input_lang;
	String output_lang;
	Translator translator;

	public Html2TextTranslateProcessor(String input_lang, String output_lang, Translator translator) {
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
				.replaceAll("<span class=\"notranslate\"> ", "") //
				.replaceAll("<span class = \"notranslate\"> ", "") //
				.replaceAll("<span class=\"notranslate\">", "") //
				.replaceAll("<span class = \"notranslate\">", "") //
				.replaceAll("<span class =“notranslate”>", "") //
				.replaceAll("<span class =“notranslate”> ", "") //
				.replaceAll("</ span> ", "") //
				.replaceAll("</ span>", "") //
				.replaceAll("</span>", "");
		return text;
	}

}
