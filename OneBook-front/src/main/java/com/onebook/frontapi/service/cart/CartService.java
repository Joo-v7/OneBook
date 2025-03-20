package com.onebook.frontapi.service.cart;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.cart.*;
import com.onebook.frontapi.dto.image.ImageDTO;
import com.onebook.frontapi.dto.stock.StockDTO;
import com.onebook.frontapi.feign.cart.CartClient;
import com.onebook.frontapi.service.book.BookService;
import com.onebook.frontapi.service.image.ImageService;
import com.onebook.frontapi.service.stock.StockService;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartClient cartClient;
    private final RedisTemplate<Object, Object> redisTemplate;
    public final static String CART_ID = "CART_ID";
    public final static String CART_PREFIX = "CART:";

    private final BookService bookService;
    private final ImageService imageService;
    private final StockService stockService;

    // CART_ID 쿠키에서 cartId 가져오기.
    public String getCartIdFromCookie (HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = null;

        Cookie[] cookies = request.getCookies();
        // 쿠키가 하나도 없으면 '홈'으로 리다이렉트 하게 함.
        if(Objects.isNull(cookies)) {
            response.sendRedirect("/");
        }

        // 장바구니 쿠키가 있으면 쿠키 안의 cartId 값 return.
        for(Cookie c : cookies) {
            if(c.getName().equals(CART_ID)) {
                result = c.getValue();
                return result;
            }
        }

        // cartId가 없으면 새로 생성.
        String cartId = UUID.randomUUID().toString(); // cartId 생성.

        // CART_ID cookie 생성.
        HttpSession session = request.getSession(true);
        session.setAttribute("cartId", cartId);

        Cookie cookie = new Cookie(CART_ID, session.getId());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30*24*60*60); // 장바구니 쿠키 유효 기간: 30일
        cookie.setPath("/");
        response.addCookie(cookie);

        return result;
    }

    // cartId로 redis에 저장된 cartItems 가져오기.
    public List<CartItemViewResponse> getCartItemsFromRedisById(String cartId) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(CART_PREFIX+cartId);
        List<CartItemResponse> cartItemResponses = CartItemResponse.fromMapToList(map);

        if(cartItemResponses.isEmpty()) {
           return null;
        }

        List<CartItemViewResponse> result = new ArrayList<>();
        // bookId -> book 가져와서 return.
        for (int i = 0; i < cartItemResponses.size(); i++) {
            try {
                CartItemResponse cartItemResponse = cartItemResponses.get(i);

                BookDTO book = bookService.getBook(cartItemResponse.bookId()); // book
                ImageDTO image = imageService.getImage(cartItemResponse.bookId()); // image
                StockDTO stock = stockService.getStock(cartItemResponse.bookId()); // book_stock

                CartItemViewResponse cartItemViewResponse = new CartItemViewResponse(
                        book.getBookId(),
                        book.getTitle(),
                        book.getPrice(),
                        book.getSalePrice(),
                        image.getUrl(),
                        cartItemResponse.quantity(),
                        stock.getStock()
                );

                result.add(cartItemViewResponse);
            }catch(FeignException e) {
                // 도서를 불러오는 중 에러(FeignException) 발생하면 해당 도서는 불러오지 않는다.
                continue;
            }

        }

        return result;
    }

    // 장바구니에 물건 담기: redis에 cartItemResponse 저장.
    public boolean addCartItem(String cartId, CartRequest cartRequest) {
        // redis에 bookId가 존재하면 수량이 덮어씌워짐, 없으면 추가됨.
        redisTemplate.opsForHash().put(CART_PREFIX+cartId, cartRequest.bookId(), cartRequest.quantity());
        redisTemplate.expire(CART_PREFIX + cartId, 30, TimeUnit.DAYS); // 유효기간 30일

        return true;
    }

    // 장바구니에서 특정 도서 삭제
    public void removeCartItem(String cartId, Long bookId) {
        redisTemplate.opsForHash().delete(CART_PREFIX + cartId, bookId);
    }

    // 장바구니에서 도서 수량 변경
    public void modifyCartItem(String cartId, CartRequest cartRequest) {
        redisTemplate.opsForHash().put(CART_PREFIX+cartId, cartRequest.bookId(), cartRequest.quantity());
    }

    // 장바구니 비우기 (redis에 key는 남아있음)
    public void clearCart(String cartId) {
        redisTemplate.opsForHash().delete(CART_PREFIX+cartId);
    }

    // task(DB)에서 장바구니 가져와서 redis에 저장. by cartId
    public void saveCartFromDbToRedisByCartId(String cartId) {
        CartFeignResponse cartFeignResponse = cartClient.getRequestById(cartId);
        CartResponse cartResponse = CartResponse.from(cartFeignResponse);

        for(CartItemResponse ci : cartResponse.cartItemResponses()) {
            redisTemplate.opsForHash().put(CART_PREFIX + cartId, ci.bookId(), ci.quantity());
        }

    }

    // task(DB)에서 장바구니 가져와서 redis에 저장. by loginId
    public void saveCartFromDbToRedisByLoginId(String loginId,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        // CART_ID 쿠키가 있다면
        for(Cookie c : cookies) {
            if(c.getName().equals(CART_ID)) {
                return;
            }
        }

        try {
            // CART_ID 쿠키가 없으면
            CartFeignResponse cartFeignResponse = cartClient.getRequestByLoginId(loginId);
            CartResponse cartResponse = CartResponse.from(cartFeignResponse);

            // redis에 db에서 가져온 cart 저장.
            if (Objects.nonNull(cartResponse)) {
                for (CartItemResponse ci : cartResponse.cartItemResponses()) {
                    redisTemplate.opsForHash().put(CART_PREFIX + cartResponse.cartId(), ci.bookId(), ci.quantity());
                    redisTemplate.expire(CART_ID + cartFeignResponse.cartId(), 30, TimeUnit.DAYS);
                }
            }

            // CART_ID 쿠키 재발급
            HttpSession session = request.getSession(true);
            session.setAttribute("cartId", cartResponse.cartId());

            Cookie cookie = new Cookie(CART_ID, session.getId());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 24 * 60 * 60); // 장바구니 쿠키 유효 기간: 30일
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch(FeignException e) {
            return;
        }
    }

}
