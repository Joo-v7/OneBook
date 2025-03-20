package com.nhnacademy.taskapi.address.domain.dto.req;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class UpdateMemberAddressRequest
{
    @NotNull(message = "id가 비어있습니다")
    @PositiveOrZero(message = "id는 0보다 작을 수 없습니다")
    private Long id;

    @NotBlank(message = "수신인의 이름을 입력해주세요.")
    @Size(max = 10, message = "수신인의 이름은 10자를 넘을 수 없습니다.")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01[0-9]\\d{7,8}$", message = "올바른 휴대폰 번호가 아닙니다.")
    private String phoneNumber;

    @Size(max = 20, message = "별칭은 20자 까지만 가능합니다.")
    private String alias;

    @Size(max = 100, message = "요청사항은 100자까지만 가능합니다")
    private String requestedTerm;

    @NotBlank(message = "우편번호를 입력해주세요")
    private String zipCode;

    @NotBlank(message = "도로명주소를 입력해주세요")
    @Size(max = 100,message = "도로명 주소는 100자 이내만 가능합니다.")
    private String roadNameAddress;

    @Size(max = 100,message = "지번주소는 100자 이내만 가능합니다.")
    private String numberAddress;

    @Size(max = 100)
    private String notes;

    @NotBlank(message = "상세주소를 입력해주세요.")
    @Size(max = 100,message = "상세주소는 100자 이내만 가능합니다")
    private String detailAddress;

    @NotNull(message = "기본배송지 설정 여부를 정해주세요.")
    private boolean defaultLocation;
}
