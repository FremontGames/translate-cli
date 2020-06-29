package com.damienfremont.text;

import org.springframework.batch.item.file.transform.LineAggregator;

public class TextLineAggregator implements LineAggregator<String> {


	@Override
	public String aggregate(String item) {
		return item;
	}

}
