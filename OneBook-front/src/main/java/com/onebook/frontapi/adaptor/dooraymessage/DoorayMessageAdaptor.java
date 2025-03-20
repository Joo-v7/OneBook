package com.onebook.frontapi.adaptor.dooraymessage;

import com.onebook.frontapi.dto.dooraymessage.DoorayMessageRequestDto;
import com.onebook.frontapi.feign.dooraymessage.DoorayMessageClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DoorayMessageAdaptor {

    private final DoorayMessageClient doorayMessageClient;

    public void send(String code) {

        try {
            doorayMessageClient.send(new DoorayMessageRequestDto(
                    "[Web발신]",
                    "https://static.dooray.com/static_images/dooray-bot.png",
                    "[1nebook.shop] " + "인증 번호는 [" + code + "] 입니다."
            )).getBody();

        }catch(FeignException e) {
            throw new RuntimeException("두레이 메시지 인증번호 전송에 실패하였습니다");
        }
    }
}
