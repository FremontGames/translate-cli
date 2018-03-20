package com.atalantoo.translator;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.google.common.base.Throwables;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.logging.Level;

@Deprecated
public class GoogleUI implements Translator {

   String GOOGLE_URL_PATTERN = "https://translate.google.fr/#%s/%s/%s";
   String GOOGLE_HTML_TARGET = "#result_box span";

    public String translate(String value, String srcLang, String destLang) {
        WebDriver webDriver = null;
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
            System.setProperty("jsse.enableSNIExtension", "false");
            DesiredCapabilities cap = new DesiredCapabilities();
            webDriver = new HtmlUnitDriver(cap);

            Field field = HtmlUnitDriver.class.getDeclaredField("webClient");
            field.setAccessible(true);
            WebClient webClient = (WebClient) field.get(webDriver);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.setCssErrorHandler(new SilentCssErrorHandler());

            String urlValue = URLEncoder.encode(value, "UTF-8");
            String url = String.format(GOOGLE_URL_PATTERN, srcLang, destLang, urlValue);

            webDriver.get(url);
            WebDriverWait wait = new WebDriverWait(webDriver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(GOOGLE_HTML_TARGET)));
            WebElement target = webDriver.findElement(By.cssSelector(GOOGLE_HTML_TARGET));
            String newValue = target.getText();

            return newValue;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        } finally {
            if(null != webDriver)
                webDriver.quit();
        }
    }
}
