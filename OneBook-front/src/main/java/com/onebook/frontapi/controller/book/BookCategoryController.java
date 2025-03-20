package com.onebook.frontapi.controller.book;

import com.onebook.frontapi.dto.author.AuthorDTO;
import com.onebook.frontapi.dto.book.BookCategoryDTO;
import com.onebook.frontapi.service.book.BookCategoryService;
import com.onebook.frontapi.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/book-categories")
@RequiredArgsConstructor
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;
    private final BookService bookService;


    @GetMapping
    public String getBooksByCategory(@RequestParam int categoryId,
                                     @RequestParam(defaultValue = "0")int page,
                                     Model model) {

        Page<BookCategoryDTO> books = bookCategoryService.getAllBookCategories(categoryId, PageRequest.of(page, 20));
        model.addAttribute("bookList", books);
        model.addAttribute("categoryId", categoryId);
        return "book/bookList";
    }



}
