package com.onebook.frontapi.dto.point.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MemberPointResponse {
    private Long memberId;
    private int points;
    private String description;
}
