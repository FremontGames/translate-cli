package com.atalantoo.translator;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
public class GoogleUIPhantomJS implements Translator {

	String phantomJsAbsolutePath = "phantomjs-2.1.1-windows/bin/phantomjs.exe";
	String URL_PATTERN = "https://translate.google.fr/#%s/%s/%s";
	String HTML_OUTPUT = ".translation";
	String GENDERED = ".gendered-translations";
	String ERROR_FILE = "translator-error-%s.png";

	public String translate(String text, String  inputLang, String outputLang) {
		try (PhantomJSDriverClosable webdriver = initWebDriver()) {
			try {
				goToPage(webdriver, text, inputLang, outputLang);
				List<WebElement> translations = getTranslations(webdriver);
				checkNotEmpty(translations);
				String outputText;
				outputText = defaultText(translations);
				if (hasMoreThanOne(translations) && hasGender(webdriver))
					outputText = secondText(translations);
				log(text, outputText);
				return outputText;
			} catch (Exception e) {
				takeScreenshot(webdriver, e);
				throw new RuntimeException(e);
			}
		}
	}

	private String secondText(List<WebElement> trans) {
		return trans.get(1).getText();
	}

	private String defaultText(List<WebElement> trans) {
		return trans.get(0).getText();
	}

	private boolean hasMoreThanOne(List<WebElement> trans) {
		return trans.size() > 1;
	}

	private boolean hasGender(PhantomJSDriverClosable webdriver) {
		WebElement gender = webdriver.findElement(By.cssSelector(GENDERED));
		return (gender != null);
	}

	private void checkNotEmpty(List<WebElement> trans) {
		if (trans.size() == 0)
			throw new RuntimeException("not found");
	}

	private List<WebElement> getTranslations(PhantomJSDriverClosable webDriver) {
		return webDriver.findElements(By.cssSelector(HTML_OUTPUT));
	}

	private void log(String value, String newValue) {
		System.out.println(value + "=" + newValue);
	}

	private void takeScreenshot(PhantomJSDriverClosable webDriver, Exception e) {
		try {
			File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
			long ts = System.currentTimeMillis();
			FileUtils.copyFile(scrFile, new File(String.format(ERROR_FILE, "" + ts)));
		} catch (IOException e1) {
			throw new RuntimeException(e);
		}
	}

	private void goToPage(PhantomJSDriverClosable webDriver, String value, String srcLang, String destLang)
			throws UnsupportedEncodingException {
		String urlValue = URLEncoder.encode(value, "UTF-8");
		String url = String.format(URL_PATTERN, srcLang, destLang, urlValue);
		webDriver.get(url);
		WebDriverWait wait = new WebDriverWait(webDriver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(HTML_OUTPUT)));
	}

	class PhantomJSDriverClosable extends PhantomJSDriver implements AutoCloseable {

		public PhantomJSDriverClosable() {
			super(new DesiredCapabilities(ImmutableMap.of( //
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, //
					phantomJsAbsolutePath)));
		}

		public void close() {
			quit();
		}
	}

	private PhantomJSDriverClosable initWebDriver() {
		PhantomJSDriverClosable webDriver = new PhantomJSDriverClosable();
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		return webDriver;
	}

}