package com.nhnacademy.taskapi.order.repository;

import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.grade.domain.Grade;
import com.nhnacademy.taskapi.member.domain.Member;
import com.nhnacademy.taskapi.member.repository.MemberRepository;
import com.nhnacademy.taskapi.order.entity.Order;
import com.nhnacademy.taskapi.roles.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
@Import(QuerydslConfig.class)

class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setUp() {
        Grade grade = Grade.create("ROYAL", 10, "일반 등급");
        Role role = Role.createRole("MEMBER", "일반 회원");

        Grade saveGrade = entityManager.persist(grade);
        Role saveRole = entityManager.persist(role);
        this.member = Member.createNewMember(saveGrade, "김선준", "kimsj", "kimsj123", LocalDate.now(), Member.Gender.M, "kimsj@nhnacademy.com", "010-1111-2222", saveRole);
        memberRepository.save(this.member);
    }

    // create
    @Test
    @DisplayName("Save order")
    void save() {
        // given
        Order order = new Order(
                this.member,
                "김선준",
                "010-9999-9999",
                LocalDateTime.now(),
                3000,
                25000
        );

        // when: order 저장
        Order saveOrder = orderRepository.save(order);

        // then: 저장된 데이터 데이터 검증
        Optional<Order> foundOrder = orderRepository.findById(saveOrder.getOrderId());
        Assertions.assertTrue(foundOrder.isPresent());
        Assertions.assertEquals(saveOrder.getOrderId(), foundOrder.get().getOrderId());
        Assertions.assertEquals(saveOrder.getMember(), foundOrder.get().getMember());
    }

    // read
    @Test
    @DisplayName("Find Order By MemberId")
    void findOrderByMemberId() {
        // given
        Order order = new Order(this.member, "김선준", "010-9999-9999", LocalDateTime.now(), 3000, 25000);
        Order saveOrder = orderRepository.save(order);

        // when
        List<Order> allByMemberId = orderRepository.findByMemberId(this.member.getId());

        // then

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, allByMemberId.size()),
                () -> Assertions.assertEquals(saveOrder.getOrderId(), allByMemberId.get(0).getOrderId())
        );
    }

    // update
    @Test
    void updateOrder() {
        // given
        Order order = new Order(this.member, "김선준", "010-9999-9999", LocalDateTime.now(), 3000, 25000);
        Order saveOrder = orderRepository.save(order);
        List<Order> allByMemberId = orderRepository.findByMemberId(this.member.getId());
        Long orderId = allByMemberId.get(0).getOrderId();

        // when
        allByMemberId.get(0).setOrdererName("김수정");

        // then
        Order findOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertNotNull(findOrder);
        Assertions.assertEquals("김수정", findOrder.getOrdererName());
    }

    // delete
    @Test
    void deleteOrder() {
        // given
        Order order = new Order(this.member, "김선준", "010-9999-9999", LocalDateTime.now(), 3000, 25000);
        Order saveOrder = orderRepository.save(order);
        List<Order> allByMemberId = orderRepository.findByMemberId(this.member.getId());
        Long orderId = allByMemberId.get(0).getOrderId();

        // when
        orderRepository.deleteById(orderId);
        List<Order> allByMemberId1 = orderRepository.findByMemberId(this.member.getId());

        // then
        Assertions.assertEquals(0, allByMemberId1.size());
    }
}