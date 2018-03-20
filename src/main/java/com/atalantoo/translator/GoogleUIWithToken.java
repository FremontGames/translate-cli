package com.atalantoo.translator;

import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

@Deprecated
@RequiredArgsConstructor
public class GoogleUIWithToken implements Translator {

    @NonNull String google_token;

    String API_PATTERN = "https://translate.google.fr/translate_a/single?client=t&sl=%s&tl=%s&hl=fr&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&kc=0&tk=%s&q=%s";

    public String translate(String value, String srcLang, String destLang) {
        try {
            String urlValue = URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
            String url = String.format(API_PATTERN, srcLang, destLang, google_token, urlValue);
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
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
