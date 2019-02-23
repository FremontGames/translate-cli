package com.atalantoo.text;

import org.springframework.batch.item.file.LineMapper;

public class TextLineMapper implements LineMapper<String> {

	@Override
	public String mapLine(String line, int lineNumber) throws Exception {
		return line;
	}

}
