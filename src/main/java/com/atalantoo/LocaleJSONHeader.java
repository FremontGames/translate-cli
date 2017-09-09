package com.atalantoo;
import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class LocaleJSONHeader implements FlatFileHeaderCallback {

	private static final String HEADER = "{";

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write(HEADER);
	}

}
