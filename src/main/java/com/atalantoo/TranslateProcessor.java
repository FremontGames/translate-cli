package com.atalantoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.item.ItemProcessor;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
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

	String google_token = "67442.485316";

	private static final String LINE_BREAK = "\\n";
	private static final String LINE_BREAK_REGEX = "\\\\n";
	private static Joiner joiner = Joiner.on(LINE_BREAK).skipNulls();

	@Override
	public LocaleJSONLine process(LocaleJSONLine item) throws Exception {
		if (item.key == null)
			return null;
		Preconditions.checkArgument(StringUtils.isNotBlank(item.value));

		String newValue;
		if (item.value.contains(LINE_BREAK)) {
			String[] lines = item.value.split(LINE_BREAK_REGEX);
			for (int i = 0; i < lines.length; i++) {
				lines[i] = translate(lines[i]);
			}
			newValue = joiner.join(lines);
		} else {
			newValue = translate(item.value);
		}

		return new LocaleJSONLine(item.key, newValue);
	}

	private static final String UI_PATTERN = "https://translate.google.fr/#%s/%s/%s";
	private static final String UI_TARGET = "#result_box span";

	private String translate(String value) throws UnsupportedEncodingException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String urlValue = URLEncoder.encode(value, "UTF-8");
		String url = String.format(UI_PATTERN, src_lang, dest_lang, urlValue);
		WebDriver webDriver;

		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

		System.setProperty("jsse.enableSNIExtension", "false");
		DesiredCapabilities cap = new DesiredCapabilities();
		webDriver = new HtmlUnitDriver(cap);

		Field field = HtmlUnitDriver.class.getDeclaredField("webClient");
		field.setAccessible(true);
		WebClient webClient = (WebClient) field.get(webDriver);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.getOptions().setCssEnabled(false);

		webDriver.get(url);
		WebDriverWait wait = new WebDriverWait(webDriver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UI_TARGET)));
		WebElement target = webDriver.findElement(By.cssSelector(UI_TARGET));
		String newValue = target.getText();

		webDriver.quit();
		return newValue;
	}

	private static final String API_PATTERN = "https://translate.google.fr/translate_a/single?client=t&sl=%s&tl=%s&hl=fr&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&kc=0&tk=%s&q=%s";

	private String translate2(String value) throws ClientProtocolException, IOException {
		String urlValue = URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
		String url = String.format(API_PATTERN, src_lang, dest_lang, google_token, urlValue);

		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			@Override
			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					if (entity == null)
						return null;
					return EntityUtils.toString(entity).split("\"")[1];
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		System.out.println("Executing request " + httpget.getRequestLine());
		String responseBody = httpclient.execute(httpget, responseHandler);
		System.out.println(responseBody);

		return responseBody;
	}
}
