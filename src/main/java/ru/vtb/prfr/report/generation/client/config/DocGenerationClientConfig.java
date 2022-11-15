package ru.vtb.prfr.report.generation.client.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

/**
 * Doc generation rest template configurations
 *
 * @author idurdyev
 */
@Configuration
public class DocGenerationClientConfig {
    @Bean("docGenerationRestTemplate")
    public RestTemplate getDocGenerationRestTemplate(
            @Qualifier("docGenerationRestTemplateBuilder") RestTemplateBuilder builder,
            @Value("${report.config.doc-generation.connect-timeout:15}") Long connectTimeout,
            @Value("${report.config.doc-generation.read-timeout:30}") Long readTimeout
    ) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLContext.setDefault(ctx);
        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(ctx,
                NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(scsf)
                .build();
        return builder
                .requestFactory(() -> {
                    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                    requestFactory.setHttpClient(httpClient);
                    return requestFactory;
                })
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    @Bean("docGenerationRestTemplateBuilder")
    public RestTemplateBuilder getDocGenerationRestTemplateBuilder() {
        return new RestTemplateBuilder();
    }

}
