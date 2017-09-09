package com.atalantoo;
import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileFooterCallback;

public class LocaleJSONFooter implements FlatFileFooterCallback {

	private static final String FOOTER = "}";

	@Override
	public void writeFooter(Writer writer) throws IOException {
		writer.write(FOOTER);
	}

}
