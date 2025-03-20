package com.onebook.frontapi.controller.book;

import com.onebook.frontapi.dto.author.AuthorDTO;
import com.onebook.frontapi.dto.book.*;
import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.image.ImageDTO;
import com.onebook.frontapi.dto.publisher.PublisherDTO;
import com.onebook.frontapi.dto.stock.StockDTO;
import com.onebook.frontapi.dto.tag.TagDTO;
import com.onebook.frontapi.dto.tag.TagResponse;
import com.onebook.frontapi.service.author.AuthorService;
import com.onebook.frontapi.service.book.BookAuthorService;
import com.onebook.frontapi.service.book.BookCategoryService;
import com.onebook.frontapi.service.book.BookService;
import com.onebook.frontapi.service.book.BookTagService;
import com.onebook.frontapi.service.image.ImageService;
import com.onebook.frontapi.service.publisher.PublisherService;
import com.onebook.frontapi.service.stock.StockService;
import com.onebook.frontapi.service.tag.TagService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/book")
@RequiredArgsConstructor
@Slf4j
public class AdminBookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final TagService tagService;
    private final BookCategoryService bookCategoryService;
    private final BookAuthorService bookAuthorService;
    private final StockService stockService;



    @GetMapping("/create")
    public String registerForm(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Model model) {
        log.info("Form    categoryId: {}", categoryId);
        model.addAttribute("categoryId", categoryId);
        return "book/bookRegister";
    }


    @PostMapping("/create")
    public String createBook(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("pubdate") String pubdate,
                             @RequestParam("description") String description,
                             @RequestParam("isbn13") String isbn13,
                             @RequestParam("priceSales") Integer priceSales,
                             @RequestParam("price") Integer price,
                             @RequestParam("stock") Integer stock,
                             @RequestParam("authorId") int authorId,
                             @RequestParam("publisherId") Long publisherId,
                             @RequestParam("tagId") Long tagId,
                             @RequestParam("categoriesId") String categoriesId,
                             @RequestPart("image") MultipartFile image,
                             Model model) {
        AuthorDTO author = authorService.getAuthor(authorId);
        PublisherDTO publisher = publisherService.getById(publisherId);
        TagResponse tag = tagService.getTag(tagId);
        log.info("title: {}", title);
        log.info("content: {}", content);
        log.info("pubdate: {}", pubdate);
        log.info("description: {}", description);
        log.info("isbn13: {}", isbn13);
        log.info("priceSales: {}", priceSales);
        log.info("price: {}", price);
        log.info("stock: {}", stock);
        log.info("authorId: {}", author.getAuthorId());
        log.info("publisherId: {}", publisher.getPublisherId());
        log.info("tagId: {}", tag.tagId());
        log.info("categoriesId: {}", categoriesId);
        log.info("image: {}", image.getOriginalFilename());



        BookSaveDTO dto = new BookSaveDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setPubdate(pubdate);  // String을 LocalDate로 변환
        dto.setDescription(description);
        dto.setIsbn13(isbn13);
        dto.setPriceSales(priceSales);
        dto.setPrice(price);
        dto.setStock(stock);
        dto.setAuthor(author);
        dto.setPublisher(publisher);
        dto.setTag(tag);
        dto.setCategoryId(Integer.parseInt(categoriesId));
        log.info("imageName: {}", image.getOriginalFilename());
        bookService.createBook(dto, image);
        return "redirect:/admin/book/list";
    }

    @GetMapping("/list")
    public String bookList(@RequestParam(defaultValue = "0") int page,
                           Model model) {
        Page<BookDTO> bookList = bookService.getAllBooks(PageRequest.of(page, 20));

        model.addAttribute("bookList", bookList);
        return "book/bookAllList";
    }

    @GetMapping("/update")
    public String updateBookForm(@RequestParam("bookId") long bookId,
                                 Model model) {
        BookDTO book = bookService.getBook(bookId);
        BookAuthorDTO bookAuthor = bookAuthorService.getBookAuthor(bookId);
        BookCategoryDTO bookCategory = bookCategoryService.getBookCategoryByBookId(bookId);



        CategoryDTO category = bookCategory.getCategory();
        AuthorDTO author = bookAuthor.getAuthor();
        StockDTO stock = stockService.getStock(bookId);

        model.addAttribute("book", book);
        model.addAttribute("category", category);
        model.addAttribute("author", author);
        model.addAttribute("stock", stock);
        return "book/bookUpdateDelete";
    }

    @PostMapping("/update")
    public void updateBook(@RequestParam("bookId") String bookId,
                             @ModelAttribute BookUpdateDTO dto,
                             HttpServletResponse response) throws IOException {
        bookService.updateBook(Long.parseLong(bookId), dto);
//        return "redirect:/admin/";
        response.sendRedirect("/admin/book/list");
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("bookId") String bookId){
        log.info("delete bookId: {}", bookId);
        bookService.deleteBook(Long.parseLong(bookId));
        return "redirect:/admin/book/list";
    }

    @GetMapping("/aladin")
    public String saveAladinBook(){
        bookService.AladinBook();
        return "redirect:/admin";
    }


}
