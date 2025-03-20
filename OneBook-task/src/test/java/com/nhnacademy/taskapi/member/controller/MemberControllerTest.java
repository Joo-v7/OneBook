package com.nhnacademy.taskapi.member.controller;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.grade.dto.GradeResponseDto;
import com.nhnacademy.taskapi.grade.service.GradeService;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.dto.MemberLikeViewDto;
import com.nhnacademy.taskapi.member.dto.MemberResponseDto;
import com.nhnacademy.taskapi.member.dto.MembershipCheckRequestDto;
import com.nhnacademy.taskapi.member.dto.MembershipCheckResponseDto;
import com.nhnacademy.taskapi.member.service.MemberService;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.roles.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private GradeService gradeService;

//    @Test
//    @DisplayName("GET All Members")
//    void getMembersTest() throws Exception {
//        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
//        Role role = Role.createRole("MEMBER", "일반 회원");
//        Member member = Member.createNewMember(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role);
//
//        List<Member> memberList = List.of(member);
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Member> memberPage = new PageImpl<>(memberList, pageable, memberList.size());
//        Page<MemberResponse> result = memberPage.map(MemberResponse::from);
//
//        Mockito.when(memberService.getAllMembers(pageable)).thenReturn(result);
//
//        mockMvc.perform(get("/task/admin/members/list?page=0&size=10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalPages").value(1))
//                .andExpect(jsonPath("$.size").value(pageable.getPageSize()))
//                .andExpect(jsonPath("$.content[0].name").value(member.getName()))
//                .andExpect(jsonPath("$.content[0].loginId").value(member.getLoginId()))
//                .andExpect(jsonPath("$.content[0].gender").value("M"))
//                .andExpect(jsonPath("$.content[0].email").value(member.getEmail()))
//                .andExpect(jsonPath("$.content[0].phoneNumber").value(member.getPhoneNumber()))
//                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"));
//    }

    @Test
    @DisplayName("GET Member by memberId using RequestHeader")
    void getMemberByIdTest() throws Exception {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.reconstruct(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role, Member.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        MemberResponseDto result = MemberResponseDto.from(member);

        Mockito.when(memberService.getMemberById(Mockito.anyLong())).thenReturn(result);

        mockMvc.perform(get("/task/members")
                        .header("X-MEMBER-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(grade.getName()))
                .andExpect(jsonPath("$.role").value(role.getName()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.dateOfBirth").value(String.valueOf(member.getDateOfBirth())))
                .andExpect(jsonPath("$.gender").value(String.valueOf(member.getGender())))
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(member.getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(String.valueOf(member.getStatus())))
                .andExpect(jsonPath("$.createdAt").value(containsString(member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.lastLoginAt").value(containsString(member.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    @DisplayName("GET Member by memberId using PathVariable")
    void getMemberByParameterId() throws Exception {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.reconstruct(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role, Member.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        MemberResponseDto result = MemberResponseDto.from(member);

        Mockito.when(memberService.getMemberById(Mockito.anyLong())).thenReturn(result);

        mockMvc.perform(get("/task/members/{memberId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(grade.getName()))
                .andExpect(jsonPath("$.role").value(role.getName()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.dateOfBirth").value(String.valueOf(member.getDateOfBirth())))
                .andExpect(jsonPath("$.gender").value(String.valueOf(member.getGender())))
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(member.getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(String.valueOf(member.getStatus())))
                .andExpect(jsonPath("$.createdAt").value(containsString(member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.lastLoginAt").value(containsString(member.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    @DisplayName("POST Member")
    void createMemberTest() throws Exception {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.reconstruct(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role, Member.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        MemberResponseDto result = MemberResponseDto.from(member);

        Mockito.when(memberService.registerMember(Mockito.any())).thenReturn(result);

        mockMvc.perform(post("/task/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"김주혁\",\n" +
                                "  \"loginId\": \"joo\",\n" +
                                "  \"password\": \"jjjjjjjjjj\",\n" +
                                "  \"dateOfBirth\": \"2024-12-20\",\n" +
                                "  \"gender\": \"M\",\n" +
                                "  \"email\": \"helloworld@gmail.com\",\n" +
                                "  \"phoneNumber\": \"01011111111\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(grade.getName()))
                .andExpect(jsonPath("$.role").value(role.getName()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.dateOfBirth").value(String.valueOf(member.getDateOfBirth())))
                .andExpect(jsonPath("$.gender").value(String.valueOf(member.getGender())))
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(member.getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(String.valueOf(member.getStatus())))
                .andExpect(jsonPath("$.createdAt").value(containsString(member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.lastLoginAt").value(containsString(member.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    @DisplayName("PUT Member")
    void updateMemberTest() throws Exception {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.reconstruct(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role, Member.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        MemberResponseDto result = MemberResponseDto.from(member);

        Mockito.when(memberService.modifyMember(Mockito.any(), Mockito.any())).thenReturn(result);

        mockMvc.perform(put("/task/members")
                        .header("X-MEMBER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"김주혁\",\n" +
                                "  \"password\": \"jjjjjjjjjj\",\n" +
                                "  \"email\": \"helloworld@gmail.com\",\n" +
                                "  \"phoneNumber\": \"01011111111\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(grade.getName()))
                .andExpect(jsonPath("$.role").value(role.getName()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.dateOfBirth").value(String.valueOf(member.getDateOfBirth())))
                .andExpect(jsonPath("$.gender").value(String.valueOf(member.getGender())))
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(member.getPhoneNumber()))
                .andExpect(jsonPath("$.status").value(String.valueOf(member.getStatus())))
                .andExpect(jsonPath("$.createdAt").value(containsString(member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.lastLoginAt").value(containsString(member.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    @DisplayName("DELETE Member")
    void deleteMemberTest() throws Exception {
        mockMvc.perform(delete("/task/members")
                .header("X-MEMBER-ID", "1"))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(memberService, Mockito.times(1)).removeMember(1L);
    }

    @Test
    @DisplayName("Change Member Status To ACTIVE")
    void modifyMemberStatus() throws Exception {
        mockMvc.perform(get("/task/members/status/{loginId}", "joo"))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(memberService, Mockito.times(1)).changeStatusToActivation("joo");
    }

    @Test
    @DisplayName("GET Member Grade")
    void getGradeTest() throws  Exception {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.reconstruct(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role, Member.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        MemberResponseDto memberResponseDto = MemberResponseDto.from(member);
        GradeResponseDto gradeResponseDto = GradeResponseDto.from(grade);

        Mockito.when(memberService.getMemberById(Mockito.anyLong())).thenReturn(memberResponseDto);
        Mockito.when(gradeService.getGradeByName(grade.getName())).thenReturn(gradeResponseDto);

        mockMvc.perform(get("/task/members/grade")
                .header("X-MEMBER-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gradeResponseDto.id()))
                .andExpect(jsonPath("$.name").value(gradeResponseDto.name()))
                .andExpect(jsonPath("$.accumulationRate").value(gradeResponseDto.accumulationRate()))
                .andExpect(jsonPath("$.description").value(gradeResponseDto.description()));
    }

    @Test
    @DisplayName("Check Membership")
    void checkMembershipTest() throws Exception {
        MembershipCheckRequestDto membershipCheckRequestDto = new MembershipCheckRequestDto("password");
        MembershipCheckResponseDto membershipCheckResponseDto = new MembershipCheckResponseDto(true);

        Mockito.when(memberService.validateMembership(1L, membershipCheckRequestDto)).thenReturn(membershipCheckResponseDto);

        mockMvc.perform(post("/task/members/membership")
                .header("X-MEMBER-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"password\": \"password\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMember").value(membershipCheckResponseDto.isMember()));
    }

    @Test
    @DisplayName("Member NetAmountPayments")
    void memberNetAmountPaymentsTest() throws Exception {
        Integer totalAmount = 1000;
        Mockito.when(memberService.memberNetPaymentAmount(Mockito.anyLong())).thenReturn(totalAmount);

        mockMvc.perform(get("/task/members/payments/net-amount")
                .header("X-MEMBER-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(totalAmount));
    }

    @Test
    @DisplayName("GET Books for Member")
    void getListBooksForMemberTest() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setName("joo");

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("test");
        book1.setPrice(100);
        book1.setContent("test");
        book1.setAmount(10);
        book1.setDescription("test");
        book1.setPubdate(LocalDate.now());
        book1.setIsbn13("1111111111111");
        book1.setSalePrice(500);
        book1.setPublisher(publisher);

        Image image = new Image();
        image.setImageId(1L);
        image.setBook(new Book());
        image.setName("imageName");
        image.setUrl("http://test-image-url.com");

        MemberLikeViewDto memberLikeViewDto = new MemberLikeViewDto(book1, image);
        List<MemberLikeViewDto> memberLikeViewDtoList = List.of(memberLikeViewDto);

        Mockito.when(memberService.getLikeBooksByMemberId(Mockito.anyLong())).thenReturn(memberLikeViewDtoList);

        mockMvc.perform(get("/task/members/likes/books")
                        .header("X-MEMBER-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].book.bookId").value(book1.getBookId()))
                .andExpect(jsonPath("$[0].book.title").value(book1.getTitle()))
                .andExpect(jsonPath("$[0].book.price").value(book1.getPrice()))
                .andExpect(jsonPath("$[0].book.content").value(book1.getContent()))
                .andExpect(jsonPath("$[0].book.amount").value(book1.getAmount()))
                .andExpect(jsonPath("$[0].book.description").value(book1.getDescription()))
                .andExpect(jsonPath("$[0].book.pubdate").value(book1.getPubdate().toString()))
                .andExpect(jsonPath("$[0].book.isbn13").value(book1.getIsbn13()))
                .andExpect(jsonPath("$[0].book.salePrice").value(book1.getSalePrice()))
                .andExpect(jsonPath("$[0].book.publisher.name").value(book1.getPublisher().getName()))
                .andExpect(jsonPath("$[0].image.imageId").value(image.getImageId()))
                .andExpect(jsonPath("$[0].image.name").value(image.getName()))
                .andExpect(jsonPath("$[0].image.url").value(image.getUrl()));
    }


    @Test
    @DisplayName("GET loginId by memberId")
    void getMemberIdByLoginIdTest() throws Exception {
        Mockito.when(memberService.getLoginIdById(Mockito.anyLong())).thenReturn("joo");

        mockMvc.perform(get("/task/members/loginId")
                        .header("X-MEMBER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("joo"));
    }

    @Test
    @DisplayName("GET Member For JWT")
    void getMemberForJwtTest() throws Exception {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");
        Member member = Member.reconstruct(grade, "김주혁", "joo", "jjjjjjjjjj", LocalDate.now(), Member.Gender.M, "helloworld@gmail.com", "01011111111", role, Member.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(memberService.getMemberByLoginId(Mockito.anyString())).thenReturn(member);

        mockMvc.perform(get("/task/members/jwt/{loginId}", "joo")
                .header("X-MEMBER-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.loginId").value(member.getLoginId()))
                .andExpect(jsonPath("$.role").value(member.getRole().getName()));
    }

}
