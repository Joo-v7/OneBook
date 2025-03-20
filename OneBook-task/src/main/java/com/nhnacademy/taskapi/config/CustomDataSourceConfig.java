package com.nhnacademy.taskapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.taskapi.keyManager.dto.KeyResponseDto;
import com.nhnacademy.taskapi.keyManager.exception.KeyManagerException;
import com.nhnacademy.taskapi.keyManager.service.KeyFactoryManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
//@Configuration
public class CustomDataSourceConfig {

    public CustomDataSourceConfig(KeyFactoryManager keyFactoryManager) {
//        this.keyFactoryManager = keyFactoryManager;
    }

//    private KeyFactoryManager keyFactoryManager;
    private static final String URL = "jdbc:mysql://%s:%s/%s";

    @Value("${nhnKey.keyId}")
    private String keyId;

    @Value("${db.ip}")
    private String ip;

    @Value("${db.port}")
    private int port;

    @Value("${db.dbName}")
    private String dbName;

    @Value("${db.userName}")
    private String userName;

    @Value("${db.password}")
    private String password;


//    @Bean
    public DataSource ServerDataSource() {
        /* key 매니저 지원중단으로 인한 주석
        Map<String, String> secretMap = new HashMap<>();
        try {
            KeyResponseDto body = keyFactoryManager.keyInit(keyId);
            if (Objects.isNull(body)) {
                throw new KeyManagerException("Key init body failed");
            }
            secretMap = body.getBody().getSecretAsMap();
        } catch (JsonProcessingException | KeyManagerException e) {
            log.debug("Key init failed", e);
        }

        // 데이터 베이스 연결 정보 추출
        String ip = secretMap.get("ip");
        String port = secretMap.get("port");
        String dbName = secretMap.get("dbName");
        String userName = secretMap.get("userName");
        String password = secretMap.get("password");
        */

        // DataSource 설정 (Apache Commons DBCP2 사용)
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(URL.formatted(ip, port, dbName));
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        /*
        dataSource.setInitialSize(5); // 초기 커넥션 수; default=0
        dataSource.setMaxTotal(10); // 최대 커넥션 수; default=8
        dataSource.setMaxIdle(5); // 최대 유휴 커넥션 수; default=8
        dataSource.setMinIdle(2); // 최소 유휴 커넥션 수; default=0
         */

        log.info(URL.formatted(ip, port, dbName));

        return dataSource;
    }
}