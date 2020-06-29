package com.damienfremont.html;

import org.springframework.batch.item.file.transform.LineAggregator;

public class HtmlLineAggregator implements LineAggregator<String> {

	@Override
	public String aggregate(String item) {
		return item;
	}

}
