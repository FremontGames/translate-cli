package com.atalantoo.translator;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class GoogleUIPhantomJS implements Translator {

    @NonNull
    String phantomJsAbsolutePath;

    String GOOGLE_URL_PATTERN = "https://translate.google.fr/#%s/%s/%s";
    String GOOGLE_HTML_TARGET = "#result_box span";

    public String translate(String value, String srcLang, String destLang) {
        WebDriver webDriver = null;
        try {
            webDriver = initWebDriver();

            String urlValue = URLEncoder.encode(value, "UTF-8");
            String url = String.format(GOOGLE_URL_PATTERN, srcLang, destLang, urlValue);

            webDriver.get(url);
            WebDriverWait wait = new WebDriverWait(webDriver, 5);

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(GOOGLE_HTML_TARGET)));
            WebElement target = webDriver.findElement(By.cssSelector(GOOGLE_HTML_TARGET));
            String newValue = target.getText();

            return newValue;

        } catch (Exception e) {
            if(null != webDriver) {
                takeScreenshot(webDriver);
            }
            throw Throwables.propagate(e);
        } finally {
            if(null != webDriver)
                webDriver.quit();
        }
    }

    private void takeScreenshot(WebDriver webDriver) {
        File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File(screenshot.getName()),true);
        } catch (Exception e2) {
            System.out.println("Error during error screenshot");
            e2.printStackTrace();
        }
    }

    private WebDriver initWebDriver() {
        WebDriver driver;
        driver = new PhantomJSDriver(
            new DesiredCapabilities(ImmutableMap.of( //
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, //
                    phantomJsAbsolutePath)));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }
}