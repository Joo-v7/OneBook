package com.onebook.frontapi.config.advice;

import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.service.category.CategoryService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.onebook.frontapi.service.cart.CartService.CART_ID;
import static com.onebook.frontapi.service.cart.CartService.CART_PREFIX;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final CategoryService categoryService;

//    // 모든 요청에서 topCategories를 자동으로 추가
//    @ModelAttribute("topCategories")
//    public List<CategoryDTO> getTopCategories() {
//        List<CategoryDTO> categories = categoryService.getTopCategories();
//        return categories;
//    }


    @ModelAttribute(name="cartProductCounting")
    public void setViewHeaderFrag(
            @CookieValue(required=false, value=CART_ID) Cookie cartCookie,
            Model model) {
        // 장바구니 아이템 개수.
        Long count = 0L;

        if(Objects.nonNull(cartCookie)) {
            String cartId = cartCookie.getValue();
            count = redisTemplate.opsForHash().size(CART_PREFIX +cartId);
        }

        model.addAttribute("cartCount", count);
    }

    @ModelAttribute
    public void checkRole(Model model){

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        String role = authentication.getAuthorities().stream().findFirst().get().toString();

        model.addAttribute("role",role);

    }
}
