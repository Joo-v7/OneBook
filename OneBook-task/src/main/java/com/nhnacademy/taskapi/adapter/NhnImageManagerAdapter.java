package com.nhnacademy.taskapi.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nhnacademy.taskapi.adapter.properties.ImageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class NhnImageManagerAdapter {
    private final RestTemplate restTemplate;
    private static final String URL = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/{appkey}/images";
    private final ImageProperties imageProperties;

    public String uploadImage(byte[] imageBytes, String fileName) throws IOException{

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", imageProperties.getSecretkey());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        HttpEntity<byte[]> entity = new HttpEntity<>(imageBytes, headers);
        String url = URL.replace("{appkey}", imageProperties.getAppkey()) + "?path=/onebook/" + fileName + "&overwrite=true";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);


        if(response.getStatusCode() == HttpStatus.OK){
            return extractUrl(response.getBody());

        }else{
            return null;
        }
    }

    public String uploadReviewImage(byte[] imageBytes, String fileName, long bookId, String loginId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", imageProperties.getSecretkey());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> entity = new HttpEntity<>(imageBytes, headers);

        // 리뷰 이미지 업로드 경로 구성
        String path = "/onebook/review/images/" + bookId + "/" + loginId + "/" + fileName;
        String url = URL.replace("{appkey}", imageProperties.getAppkey()) + "?path=" + path + "&overwrite=true";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            return extractUrl(response.getBody());
        } else {
            return null;
        }
    }

    private String extractUrl(String responseBody) {
        if (responseBody.contains("url")) {
            String prefix = "\"url\":\"";
            int startIdx = responseBody.indexOf(prefix) + prefix.length();
            int endIdx = responseBody.indexOf("\"", startIdx);
            return responseBody.substring(startIdx, endIdx);
        }
        return null;
    }


    //폴더 내 파일 목록 조회
    public String listFilesInFolder(String fileName) {
        // 조회하고자 하는 폴더 경로
        String url = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + imageProperties.getAppkey() + "/folders?basepath=" + "/onebook";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", imageProperties.getSecretkey());

        HttpEntity<String> entity = new HttpEntity<>(headers);



        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return extractFileName(response.getBody(), fileName);

            }
        } catch (Exception e) {
            log.error("Error while listing files in folder", e);
        }
        return null;
    }


    //폴더 내 파일ID 추출
    private String extractFileName(String responseBody, String fileName) {


        try{
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray files = jsonResponse.getAsJsonArray("files");

            for(int i = 0; i < files.size(); i++){
                JsonObject file = files.get(i).getAsJsonObject();
                String name = file.get("name").getAsString();

                if(name.equals(fileName)){
                    return file.get("id").getAsString();
                }
            }

        }catch(Exception e){
            log.error("Error while getting file name", e);
        }
        return null;


    }


    public void deleteImage(String fileName) {
        String fileId = listFilesInFolder(fileName);
        String url = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + imageProperties.getAppkey() + "/images/sync?fileId=" + fileId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", imageProperties.getSecretkey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("File deleted successfully.");
            } else {
                log.error("Failed to delete file.");
            }
        } catch (Exception e) {
            log.error("Error while deleting file", e);
        }
    }





}