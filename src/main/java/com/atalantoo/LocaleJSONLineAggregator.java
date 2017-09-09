package com.atalantoo;
import org.springframework.batch.item.file.transform.LineAggregator;

public class LocaleJSONLineAggregator implements LineAggregator<LocaleJSONLine> {

	private static final String PATTERN = "  \"%s\" : \"%s\",  ";

	@Override
	public String aggregate(LocaleJSONLine item) {
		return String.format(PATTERN, item.key, item.value);
	}

}
