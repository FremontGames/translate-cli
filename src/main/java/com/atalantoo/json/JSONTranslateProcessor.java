package com.atalantoo.json;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.atalantoo.translator.Translator;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

public class JSONTranslateProcessor implements ItemProcessor<LocaleJSONLine, LocaleJSONLine> {

	String input_lang;
	String output_lang;
	Translator translator;

	static final String LINE_BREAK = "\\n";
	static final String LINE_BREAK_REGEX = "\\\\n";
	static Joiner joiner = Joiner.on(LINE_BREAK).skipNulls();

	public JSONTranslateProcessor(String input_lang, String output_lang, Translator translator) {
		super();
		this.input_lang = input_lang;
		this.output_lang = output_lang;
		this.translator = translator;
	}

	@Override
	public LocaleJSONLine process(LocaleJSONLine item) throws Exception {
		String newValue;
		if (item.key == null)
			return null;
		Preconditions.checkArgument(StringUtils.isNotBlank(item.value));
		if (item.value.contains(LINE_BREAK)) {
			String[] lines = item.value.split(LINE_BREAK_REGEX);
			for (int i = 0; i < lines.length; i++) {
				lines[i] = translator.translate(lines[i], input_lang, output_lang);
			}
			newValue = joiner.join(lines);
		} else {
			newValue = translator.translate(item.value, input_lang, output_lang);
		}
		return new LocaleJSONLine(item.key, newValue);
	}

}
