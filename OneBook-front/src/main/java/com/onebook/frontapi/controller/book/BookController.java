package com.onebook.frontapi.controller.book;

import com.onebook.frontapi.dto.author.AuthorDTO;
import com.onebook.frontapi.dto.book.*;
import com.onebook.frontapi.dto.image.ImageDTO;
import com.onebook.frontapi.dto.publisher.PublisherDTO;
import com.onebook.frontapi.dto.review.ReviewPageResponseDto;
import com.onebook.frontapi.dto.stock.StockDTO;
import com.onebook.frontapi.dto.tag.TagDTO;
import com.onebook.frontapi.dto.tag.TagResponse;
import com.onebook.frontapi.feign.review.ReviewClient;
import com.onebook.frontapi.service.author.AuthorService;
import com.onebook.frontapi.service.book.BookAuthorService;
import com.onebook.frontapi.service.book.BookCategoryService;
import com.onebook.frontapi.service.book.BookService;
import com.onebook.frontapi.service.book.BookTagService;
import com.onebook.frontapi.service.image.ImageService;
import com.onebook.frontapi.service.like.LikeService;
import com.onebook.frontapi.service.publisher.PublisherService;
import com.onebook.frontapi.service.stock.StockService;
import com.onebook.frontapi.service.tag.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.HTML;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
@Slf4j
public class BookController {
    private final BookService bookService;
    private final BookAuthorService bookAuthorService;
    private final AuthorService authorService;
    private final BookCategoryService bookCategoryService;
    private final BookTagService bookTagService;
    private final ImageService imageService;
    private final StockService stockService;

    private final ReviewClient reviewClient;

    @GetMapping("/newbooks")
    public String newBooks(Model model, Pageable pageable) {
        Page<BookDTO> bookDTOPage = bookService.newBooks(pageable);
        log.info("page: {}", bookDTOPage);
        model.addAttribute("bookList", bookDTOPage.getContent());

        return "index";
    }

    @GetMapping("/bookDetail")
    public String bookDetail(@RequestParam("bookId") long bookId,
                             @RequestParam(value = "url", required = false) String url,
                             Model model) {
        // 기존 도서 및 저자 정보 로드
        BookDTO book = bookService.getBook(bookId);
        BookAuthorDTO bookAuthor = bookAuthorService.getBookAuthor(bookId);
        BookCategoryDTO category = bookCategoryService.getBookCategoryByBookId(bookId);
        AuthorDTO author = authorService.getAuthor(bookAuthor.getAuthor().getAuthorId());
        StockDTO stock = stockService.getStock(bookId);

        // 이미지 URL 처리
        if(Objects.isNull(url) || url.isEmpty()) {
            ImageDTO image = imageService.getImage(bookId);
            model.addAttribute("url", image.getUrl());
        } else {
            model.addAttribute("url", url);
        }

        model.addAttribute("category", category.getCategory());
        model.addAttribute("book", book);
        model.addAttribute("author", author);
        model.addAttribute("stock", stock);

        // 리뷰 데이터 로드 - 첫 페이지, 페이지 사이즈 5
        int initialPage = 0;
        int pageSize = 5;
        ReviewPageResponseDto reviewPage = reviewClient.getReviews(bookId, initialPage, pageSize);
        model.addAttribute("reviewPage", reviewPage);

        return "book/bookDetail";
    }


//    @GetMapping
//    public String registerForm(@RequestParam(value = "categoryId", required = false) Integer categoryId,
//                               Model model) {
//        log.info("Form    categoryId: {}", categoryId);
//        model.addAttribute("categoryId", categoryId);
//        return "book/bookRegister";
//    }
//
//
//    @PostMapping("/create")
//    public String createBook(@RequestParam("title") String title,
//                             @RequestParam("content") String content,
//                             @RequestParam("pubdate") String pubdate,
//                             @RequestParam("description") String description,
//                             @RequestParam("isbn13") String isbn13,
//                             @RequestParam("priceSales") Integer priceSales,
//                             @RequestParam("price") Integer price,
//                             @RequestParam("stock") Integer stock,
//                             @RequestParam("authorId") int authorId,
//                             @RequestParam("publisherId") Long publisherId,
//                             @RequestParam("tagId") Long tagId,
//                             @RequestParam("categoriesId") String categoriesId,
//                             @RequestPart("image") MultipartFile image,
//                             Model model) {
//        AuthorDTO author = authorService.getAuthor(authorId);
//        PublisherDTO publisher = publisherService.getById(publisherId);
//        TagResponse tag = tagService.getTag(tagId);
//        log.info("title: {}", title);
//        log.info("content: {}", content);
//        log.info("pubdate: {}", pubdate);
//        log.info("description: {}", description);
//        log.info("isbn13: {}", isbn13);
//        log.info("priceSales: {}", priceSales);
//        log.info("price: {}", price);
//        log.info("stock: {}", stock);
//        log.info("authorId: {}", author.getAuthorId());
//        log.info("publisherId: {}", publisher.getPublisherId());
//        log.info("tagId: {}", tag.tagId());
//        log.info("categoriesId: {}", categoriesId);
//        log.info("image: {}", image.getOriginalFilename());
//
//
//
//        BookSaveDTO dto = new BookSaveDTO();
//        dto.setTitle(title);
//        dto.setContent(content);
//        dto.setPubdate(pubdate);  // String을 LocalDate로 변환
//        dto.setDescription(description);
//        dto.setIsbn13(isbn13);
//        dto.setPriceSales(priceSales);
//        dto.setPrice(price);
//        dto.setStock(stock);
//        dto.setAuthor(author);
//        dto.setPublisher(publisher);
//        dto.setTag(tag);
//        dto.setCategoryId(Integer.parseInt(categoriesId));
//        log.info("imageName: {}", image.getOriginalFilename());
//        bookService.createBook(dto, image);
//        return "redirect:/";
//    }

    @GetMapping("/book-list")
    public String bookList(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "title") String sort,
                           Model model) {
        Page<BookDTO> bookList = null;
        if(sort.equals("popular")) {
            bookList = bookService.getBestSellerBooks(PageRequest.of(page, 20));
        }else if(sort.equals("newest")){
            bookList = bookService.newBooks(PageRequest.of(page, 20));
        }else{
            bookList = bookService.getAllBooks(PageRequest.of(page, 20));
        }

        model.addAttribute("sort", sort);
        model.addAttribute("bookList", bookList);
        return "book/AllBookList";
    }


    @GetMapping("/search")
    public String bookSearch(@RequestParam(value = "search") String searchString,
                             @RequestParam(defaultValue = "0") Integer page,
                             Model model) {
        Pageable pageable = PageRequest.of(page, 10);

        List<BookSearchAllResponse> bookList = bookService.searchBookAll(searchString);


        List<ProductSearchResponse> productList = new ArrayList<>();
        for (BookSearchAllResponse bookSearchAllResponse : bookList) {
            if (!bookSearchAllResponse.isStatus()) {
                ProductSearchResponse productSearchResponse = new ProductSearchResponse();
                productSearchResponse.setBookId(bookSearchAllResponse.getBookId());
                productSearchResponse.setTitle(bookSearchAllResponse.getTitle());
                productSearchResponse.setPublisherName(bookSearchAllResponse.getPublisherName());
                productSearchResponse.setPrice(bookSearchAllResponse.getPrice());
                productSearchResponse.setDescription(bookSearchAllResponse.getDescription());
                productSearchResponse.setSalePrice(bookSearchAllResponse.getSalePrice());
                productSearchResponse.setAmount(bookSearchAllResponse.getAmount());
                productSearchResponse.setPubdate(bookSearchAllResponse.getPubdate());
                productSearchResponse.setStatus(bookSearchAllResponse.isStatus());
                productSearchResponse.setAuthorName(bookAuthorService.getBookAuthor(bookSearchAllResponse.getBookId()).getAuthor().getName());
                productSearchResponse.setImageUrl(imageService.getImage(bookSearchAllResponse.getBookId()).getUrl());
                productList.add(productSearchResponse);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), productList.size());


        List<ProductSearchResponse> productPage = productList.subList(start, end);
        Page<ProductSearchResponse> products = new PageImpl<>(productPage, pageable, productList.size());

        model.addAttribute("productList", products);
        model.addAttribute("search", searchString);

        return "book/bookSearch";

    }

    @GetMapping("/recommend")
    public String bookRecommend(Model model) {
        List<BookRecommendDto> bookList = bookService.recommendBooks();
        List<String> tagNameList = new ArrayList<>();
        List<String> imageUrlList = new ArrayList<>();

        for(BookRecommendDto bookRecommendDto : bookList) {
            long bookId = bookRecommendDto.getBookId();
            imageUrlList.add(imageService.getImage(bookId).getUrl());
            if(Objects.isNull(bookTagService.getBookTagByBookId(bookId))) {
                tagNameList.add("");
            }else{
                TagDTO tag = bookTagService.getBookTagByBookId(bookRecommendDto.getBookId()).getTag();
                tagNameList.add(tag.getName());
            }
        }

        model.addAttribute("urlList", imageUrlList);
        model.addAttribute("tagList", tagNameList);
        model.addAttribute("bookList", bookList);

        return "book/bookRecommend";
    }
}
