package com.nhnacademy.taskapi.keyManager.service;

import com.nhnacademy.taskapi.keyManager.dto.KeyResponseDto;
import com.nhnacademy.taskapi.keyManager.exception.KeyManagerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class KeyFactoryManager {
    @Value(value = "${nhnKey.url}")
    private String url;

    @Value("${nhnKey.appKey}")
    private String appKey;

    @Value("${nhnKey.keyPath}")
    private String keyPath;

    @Value("${nhnKey.password}")
    private String password;


    public KeyResponseDto keyInit(String keyId) throws KeyManagerException {
        final String apiPath = String.format("/keymanager/v1.0/appkey/%s/secrets/%s", appKey, keyId);
        log.info(apiPath);

        try {
            // keyStore
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

            /*
            FileInputStream fis = new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(keyPath)).getFile());
            clientStore.load(fis, password.toCharArray());
            */

            InputStream keyStoreInputStream = getClass().getClassLoader().getResourceAsStream(keyPath);
            if (keyStoreInputStream == null) {
                throw new KeyManagerException("1nebook.p12 not found in classpath at " + keyPath);
            }
            clientStore.load(keyStoreInputStream, password.toCharArray());

            // ssl 연결 설정

            /*
            Apache HttpClient에서 제공하는 SSLContextBuilder를 사용한 방법
            SSLContextBuilder : 키 스토어와 트러스트 스토어를 자동으로 처리해주는 도우미 클래스
             */
            SSLContext sslContext = SSLContextBuilder.create() // SSLContext 생성
                    .setProtocol("TLS") // 사용할 보안 프로토콜 설정 (예: TLS)
                    .loadKeyMaterial(clientStore, password.toCharArray()) // (인증서, 개인키)를 SSLContext에 로드
                    .loadTrustMaterial(new TrustSelfSignedStrategy()) // 신뢰할 수 있는 인증서를 로드, 여기에서는 자체 서명(Self-Signed Certificate)만 신뢰
                    .build(); // 최종적으로 구성된 SSL Context 객체 생성

            // SSLConnectionSocketFactory : SSLContext를 사용해 HTTP 클라이언트가 TLS 연결을 사용할 수 있도록 연결 팩토리를 생성.
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
            // httpclient : 생성된 소켓 팩토리를 사용하여 HTTPS 요청을 처리
            /*
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();
             */

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionSocketFactory) // https 등록
                    .register("http", new PlainConnectionSocketFactory()) // http 등록
                    .build();

            BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            // url 통신
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            URI uri = UriComponentsBuilder
                    .fromUriString(url)
                    .path(apiPath)
                    .encode()
                    .build()
                    .expand(appKey, keyId)
                    .toUri();
//            ResponseEntity<KeyResponseDto> exchange =

            return Objects.requireNonNull(restTemplate.exchange(uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    KeyResponseDto.class)).getBody();

        } catch (KeyStoreException | IOException | CertificateException
                 | NoSuchAlgorithmException
                 | UnrecoverableKeyException
                 | KeyManagementException e) {
            throw new KeyManagerException(e.getMessage());
        }
    }
}
