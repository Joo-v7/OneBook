package com.onebook.frontapi.dto.image;

import com.onebook.frontapi.dto.book.BookDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ImageDTO {

    private long imageId;


    private String url;

    // 책과 이미지는 다대일 관계 (하나의 책에 여러 이미지가 가능)

    private BookDTO book;

    // 책 이미지 이름 추가
    @Length(max = 255)
    private String name;
}
