package com.atalantoo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.LineMapper;

public class LocaleJSONLineMapper implements LineMapper<LocaleJSONLine> {

	private static final String SEPARATOR = "\"";

	@Override
	public LocaleJSONLine mapLine(String line, int lineNumber) throws Exception {
		String key = null, value = null;
		if (StringUtils.isNotBlank(line)) {
			String[] parts = line.split(SEPARATOR);
			if (parts.length == 5) {
				key = parts[1];
				value = parts[3];
			}
		}
		return new LocaleJSONLine(key, value);
	}

}
