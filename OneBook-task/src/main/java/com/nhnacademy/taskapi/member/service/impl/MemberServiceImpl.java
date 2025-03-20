package com.nhnacademy.taskapi.member.service.impl;

import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.exception.GradeNotFoundException;
import com.nhnacademy.taskapi.grade.repository.GradeRepository;
import com.nhnacademy.taskapi.grade.service.GradeService;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.repository.ImageRepository;
import com.nhnacademy.taskapi.like.domain.Like;
import com.nhnacademy.taskapi.like.repository.LikeRepository;
import com.nhnacademy.taskapi.member.dto.MemberResponse;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.dto.*;
import com.nhnacademy.taskapi.member.exception.MemberIllegalArgumentException;
import com.nhnacademy.taskapi.member.exception.MemberNotFoundException;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.member.service.MemberService;

import com.nhnacademy.taskapi.member.repository.MemberQueryDslRepository;
import com.nhnacademy.taskapi.point.service.PointService;
import com.nhnacademy.taskapi.roles.domain.Role;
import com.nhnacademy.taskapi.roles.service.RoleService;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final GradeService gradeService;
    private final RoleService roleService;
    private final PointService pointService;
    private final MemberQueryDslRepository memberQueryDslRepository;
    private final GradeRepository gradeRepository;
    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;
    private final ImageRepository imageRepository;

    /**
     * 로그인, 로그아웃 기능 보류.
     */

   // 관리자 - 전체 멤버 반환에 Pagenation 적용.
    @Transactional(readOnly = true)
    @Override
    public Page<MemberResponse> getAllMembers(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        Page<MemberResponse> result = memberPage.map(MemberResponse::from);

        return result;
    }

    // 인조키(id)로 멤버 조회
    @Transactional(readOnly = true)
    @Override
    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found by " + id));

        return MemberResponseDto.from(member);
    }

    /**
    * jwt 용 입니다. 멤버 조회하시려면 memberId로 하세요
    * 두레이 메시지에서 jwt에서 memberId 가져오는거 참고하세요
    */
    // 로그인 아이디로 멤버 조회
    @Transactional(readOnly = true)
    @Override
    public Member getMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("Member not found by " + loginId));
        return member;
    }

    // memberId로 loginId 조회.
    @Transactional(readOnly = true)
    @Override
    public String getLoginIdById(Long id) {
        return memberRepository.getLoginIdById(id).orElseThrow(() -> new MemberNotFoundException("Member not found by memberId"));
    }


    // 중복 확인 - 회원 인조키(id)가 존재하는지 확인
    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

    // 중복 확인 - 회원 로그인 ID(loginId)가 존재하는지 확인
    @Transactional(readOnly = true)
    @Override
    public boolean existsByLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    // 회원 정보 저장 - 회원 가입
    @Override
    public MemberResponseDto registerMember(MemberRegisterRequestDto memberRegisterRequestDto) {
        if(memberRepository.existsByLoginId(memberRegisterRequestDto.loginId())) {
            throw new MemberIllegalArgumentException("Already exists loginID");
        }

        // password 해시 암호화
        String HashedPassword = BCrypt.hashpw(memberRegisterRequestDto.password(), BCrypt.gensalt());

        Member member = Member.createNewMember(
                Grade.from(gradeService.getDefaultGrade()),
                memberRegisterRequestDto.name(),
                memberRegisterRequestDto.loginId(),
                HashedPassword,
                memberRegisterRequestDto.dateOfBirth(),
                memberRegisterRequestDto.gender(),
                memberRegisterRequestDto.email(),
                memberRegisterRequestDto.phoneNumber(),
                Role.from(roleService.getDefaultRole())
        );

        try {

            Member result = memberRepository.save(member);
            pointService.registerMemberPoints(result);
            return MemberResponseDto.from(result);

        }catch(DataIntegrityViolationException e) {
            throw new MemberIllegalArgumentException("Failed to save member in the database: Invalid format");
        }

    }

    // 회원 정보 수정
    @Override
    public MemberResponseDto modifyMember(Long memberId, MemberModifyRequestDto memberModifyRequestDto) {
       Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberNotFoundException("Not Found Member by " + memberId));

       // 비밀번호 암호화
       if(BCrypt.checkpw(memberModifyRequestDto.password(), member.getPassword())) { // 비교했는데 같으면 그냥 member password 넣음.
           member.modifyMember(
                   memberModifyRequestDto.name(),
                   member.getPassword(),
                   memberModifyRequestDto.email(),
                   memberModifyRequestDto.phoneNumber()
           );
       } else {
           String HashedPassword = BCrypt.hashpw(memberModifyRequestDto.password(), BCrypt.gensalt()); // 비교했는데 다르면 dto 것을 hash 암호화해서 넣음.
           member.modifyMember(
                   memberModifyRequestDto.name(),
                   HashedPassword,
                   memberModifyRequestDto.email(),
                   memberModifyRequestDto.phoneNumber()
           );
       }

       return MemberResponseDto.from(member);
    }

    // 회원 탈퇴 - 상태를 'DELETED' 로 변경.
    @Override
    public void removeMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberIllegalArgumentException("Member Not Found by " + memberId));
        member.setStatus(Member.Status.DELETED);
    }

    // 회원 상태 'ACTIVE' 로 변경.
    @Override
    public void changeStatusToActivation(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()-> new MemberNotFoundException("Member Not Found by " + loginId));
        member.setStatus(Member.Status.ACTIVE);
    }

    // 멤버 로그인 기록 업데이트
    @Override
    public void updateMemberLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()-> new MemberNotFoundException("Member Not Found by " + loginId));
        member.setLastLoginAt(LocalDateTime.now());
    }

    // 회원 여부 조회
    @Override
    public MembershipCheckResponseDto validateMembership(Long memberId, MembershipCheckRequestDto membershipCheckRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberNotFoundException("Member Not Found by " + memberId));

        // 비밀번호가 일치하면
        if(BCrypt.checkpw(membershipCheckRequestDto.password(), member.getPassword())) {
            return new MembershipCheckResponseDto(true);
        }

        return new MembershipCheckResponseDto(false);
    }

    // 멤버 순수 금액 조회 (+등급 업데이트)
    @Override
    public Integer memberNetPaymentAmount(Long memberId) {
        try {
            // memberId로 순수 결제 금액 조회.
            List<Integer> memberNetPaymentAmounts = memberQueryDslRepository.getMemberNetPaymentAmounts(memberId);

            // 순수 결제 금액 계산과 그에 따른 등급.
            Integer total = 0;
            String grade;

            for(Integer i : memberNetPaymentAmounts) {
                if(Objects.nonNull(i)) {
                    total += i;
                }
            }

            if(total >= 300000) {
                grade = "플래티넘";
            } else if (total >= 200000) {
                grade = "골드";
            } else if(total >= 100000) {
                grade = "로얄";
            } else {
                grade = "일반";
            }

            // 멤버 조회.
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("Member Not Found by " + memberId));

            // 멤버의 등급과 순수 결제 금액에 따른 등급 비교 후 다르면 grade update.
            if (!member.getGrade().getName().equals(grade)) {
                Grade newGrade = gradeRepository.findGradeByName(grade).orElseThrow(() -> new GradeNotFoundException("Grade Not Found by " + grade));
                member.setGrade(newGrade);
            }

            return total;

        }catch(Exception e) {
            throw new RuntimeException("회원 순수 구매 금액 조회 및 등급 업데이트 오류");
        }
    }

    // 멤버 좋아요 상품 불러오기
    @Override
    public List<MemberLikeViewDto> getLikeBooksByMemberId(Long memberId) {
        List<Like> allLikesByMemberId = likeRepository.findAllByMember_Id(memberId);
        List<MemberLikeViewDto> likeViewDtos = new ArrayList<>();
        for(Like l : allLikesByMemberId) {
            Image image = imageRepository.findByBook(l.getBook()).orElse(null);
            likeViewDtos.add(new MemberLikeViewDto( l.getBook(), image));
        }
        return likeViewDtos;
    }

    // 로그인
    @Override
    public MemberLoginDto validateLogin(MemberLoginDto memberLoginDto) {

        // TODO 테스트용 삭제 수정자 문영호
//        return new MemberLoginDto("hihi", "hoho");

        if(!existsByLoginId(memberLoginDto.loginId())) {
            throw new MemberIllegalArgumentException("Member id does not exist");
        }

        // 로그인 id로 member 정보 가져옴.
        Member member = memberRepository.findByLoginId(memberLoginDto.loginId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found by" + memberLoginDto.loginId()));

        // 회원 상태 검증
        if(member.getStatus().equals(Member.Status.DELETED)) {
            throw new MemberIllegalArgumentException("Member status is DELETED");
        }

        if(member.getStatus().equals(Member.Status.SUSPENDED)) {
            throw new MemberIllegalArgumentException("Member status is SUSPENDED");
        }

        // 휴면일 경우 인증해야 하므로 인증 후 front에서 다시 상태 변경 요청을 하도록 함.
//        if(member.getStatus().equals(Member.Status.INACTIVE)) {
//            member.setStatus(Member.Status.ACTIVE);
//        }

        // 최근 로그인 기록 업데이트
        member.setLastLoginAt(LocalDateTime.now());

        if(!BCrypt.checkpw(memberLoginDto.password(), member.getPassword())) {
            throw new MemberIllegalArgumentException("Wrong password");
        }

        return new MemberLoginDto(member.getLoginId(), member.getPassword());
    }

}

