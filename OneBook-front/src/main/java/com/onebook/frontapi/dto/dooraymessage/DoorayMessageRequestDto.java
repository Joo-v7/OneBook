package com.onebook.frontapi.dto.dooraymessage;

public record DoorayMessageRequestDto(
        String botName,
        String botIconImage,
        String text
) { }
