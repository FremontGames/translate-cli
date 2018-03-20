package com.atalantoo;

import com.atalantoo.translator.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TranslateProcessor implements ItemProcessor<LocaleJSONLine, LocaleJSONLine> {

	@NonNull
	public String src_lang;
	@NonNull
	public String dest_lang;
	@NonNull
	public Translator translator;

	private static final String LINE_BREAK = "\\n";
	private static final String LINE_BREAK_REGEX = "\\\\n";
	private static Joiner joiner = Joiner.on(LINE_BREAK).skipNulls();

	@Override
	public LocaleJSONLine process(LocaleJSONLine item) throws Exception {
		String newValue;
		if (item.key == null)
			return null;
		Preconditions.checkArgument(StringUtils.isNotBlank(item.value));
		if (item.value.contains(LINE_BREAK)) {
			String[] lines = item.value.split(LINE_BREAK_REGEX);
			for (int i = 0; i < lines.length; i++) {
				lines[i] = translator.translate(lines[i], src_lang, dest_lang);
			}
			newValue = joiner.join(lines);
		} else {
			newValue = translator.translate(item.value, src_lang, dest_lang);
		}

		return new LocaleJSONLine(item.key, newValue);
	}

}
