package com.onebook.frontapi.service.book;

import com.onebook.frontapi.dto.book.*;
import com.onebook.frontapi.feign.book.BookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookClient bookClient;

    //getList
    public Page<BookDTO> newBooks(Pageable pageable) {
        return bookClient.getNewBooks(pageable);
    }

    //getBook
    public BookDTO getBook(Long id) {
        return bookClient.getBookById(id);
    }

    //RegisterBook
    public BookDTO createBook(BookSaveDTO dto, MultipartFile image) {
        return bookClient.createBook(dto, image);
    }

    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookClient.getAllBooks(pageable);
    }

    public BookDTO updateBook(Long bookId, BookUpdateDTO dto) {
        return bookClient.updateBook(bookId, dto);
    }

    public void deleteBook(Long bookId) {
        bookClient.deleteBook(bookId);
    }

    public void AladinBook(){
        bookClient.aladinBook();
    }

    public List<BookDTO> newBooksTop4(){
        return bookClient.getTop4Books();
    }

    public List<BookDTO> bestSellersTop4(){ return bookClient.getBestSellersTop4();}

    public Page<BookDTO> getBestSellerBooks(Pageable pageable) {
        return bookClient.getBestSellers(pageable);
    }

    //통합검색
    public List<BookSearchAllResponse> searchBookAll(String searchString) {
        return bookClient.searchBookAll(searchString);
    }

    public List<BookRecommendDto> recommendBooks() {
        return bookClient.recommendBook();
    }
 }
