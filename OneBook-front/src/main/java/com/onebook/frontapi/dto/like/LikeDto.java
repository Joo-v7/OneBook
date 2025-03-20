package com.onebook.frontapi.dto.like;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

    private long memberId;
    private long bookId;
}
