package com.atalantoo.translator;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
	String GOOGLE_URL_PATTERN = "https://translate.google.fr/#%s/%s/%s";
	String GOOGLE_HTML_TARGET = ".translation";

	public String translate(String value, String srcLang, String destLang) {
		try (PhantomJSDriverClosable webDriver = initWebDriver()) {

			try {
				String urlValue = URLEncoder.encode(value, "UTF-8");
				String url = String.format(GOOGLE_URL_PATTERN, srcLang, destLang, urlValue);

				webDriver.get(url);
				WebDriverWait wait = new WebDriverWait(webDriver, 5);

				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(GOOGLE_HTML_TARGET)));
				WebElement target = webDriver.findElement(By.cssSelector(GOOGLE_HTML_TARGET));
				String newValue = target.getText();

				System.out.println(value+"="+newValue);
				
				return newValue;
			} catch (Exception e) {
				try {
					File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
					long ts = System.currentTimeMillis();
					FileUtils.copyFile(scrFile, new File("translator-error-"+ts+".png"));
				} catch (IOException e1) {
					throw new RuntimeException(e);
				}
				throw new RuntimeException(e);
			}
		}
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