package com.nhnacademy.taskapi.Tag.domain;

import com.nhnacademy.taskapi.book.domain.Book;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/** @EntityListeners(AuditingEntityListener.class) 삭제
 * 수정자 : 김선준
 * 수정일 : 2022.01.22(수)
 * 수정내용 : JPA엔티티에서 자동으로 생성 및 수정 시간을 관리하는 기능을 수행하기에 삭제해야된다고 판단되어
 *          주석처리함을 알립니다.
 */


@Entity
@Getter
@Setter
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name")
    private String name;


    public Tag() {}

    @Builder
    public Tag(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }
}