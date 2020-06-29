package com.damienfremont.html;

import org.springframework.batch.item.file.LineMapper;

public class HtmlLineMapper implements LineMapper<String> {

	@Override
	public String mapLine(String line, int lineNumber) throws Exception {
		return line;
	}

}
