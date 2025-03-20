package com.nhnacademy.taskapi.book.service.Impl;


import com.nhnacademy.taskapi.Tag.domain.Tag;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookAuthor;
import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.domain.BookTag;
import com.nhnacademy.taskapi.book.dto.*;
import com.nhnacademy.taskapi.book.exception.*;
import com.nhnacademy.taskapi.book.repository.BookAuthorRepository;
import com.nhnacademy.taskapi.book.repository.BookCategoryRepository;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.repository.BookTagRepository;
import com.nhnacademy.taskapi.book.service.BookService;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.exception.CategoryNotFoundException;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.image.dto.ImageSaveDTO;
import com.nhnacademy.taskapi.image.exception.ImageUploadException;
import com.nhnacademy.taskapi.image.service.ImageService;
import com.nhnacademy.taskapi.stock.domain.Stock;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final StockService stockService;
    private final BookTagRepository bookTagRepository;
    private final ImageService imageService;

    // 책 등록 및 수정 (등록/수정 통합 메서드)
    @Transactional
    @Override
    public Book saveBook(BookSaveDTO bookSaveDTO, MultipartFile imageFile) {
        // 기존 책 조회
        Book book = null;
        if(Objects.nonNull(bookRepository.findByIsbn13(bookSaveDTO.getIsbn13()))) {
            throw new BookDuplicateException("Book already exists !");
        }else{
            book = new Book();
        }

        // 책 등록

        if(bookSaveDTO.getIsbn13().length() != 13){
            throw new InvalidIsbnException("The provided ISBN-13 format is incorrect !");
        }
        book.setPublisher(bookSaveDTO.getPublisher());
        book.setTitle(bookSaveDTO.getTitle());
        book.setContent(bookSaveDTO.getContent());
        book.setDescription(bookSaveDTO.getDescription());
        book.setIsbn13(bookSaveDTO.getIsbn13());
        book.setPrice(bookSaveDTO.getPrice());
        book.setSalePrice(bookSaveDTO.getPriceSales());
        book.setAmount(0);
        book.setViews(0);
        // 출판일 설정
        if (bookSaveDTO.getPubdate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            book.setPubdate(LocalDate.parse(bookSaveDTO.getPubdate(), formatter));
        }
        book = bookRepository.save(book);

        // 책-작가 연결
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(bookSaveDTO.getAuthor());
        if(Objects.nonNull(bookAuthorRepository.findByBook_bookIdAndAuthor_authorId(book.getBookId(), bookSaveDTO.getAuthor().getAuthorId()))){
            throw new BookAuthorAlreadyExistsException("Book - Author already exists !");
        }
        bookAuthorRepository.save(bookAuthor);

        // 책-태그 연결
        if(Objects.nonNull(bookSaveDTO.getTag())){
            BookTag bookTag = new BookTag();
            bookTag.setBook(book);
            Tag tag = bookSaveDTO.getTag();
            bookTag.setTag(bookSaveDTO.getTag());
            if(Objects.nonNull(bookTagRepository.findByBook_BookIdAndTag_TagId(book.getBookId(), tag.getTagId()))){
                throw new BookTagAlreadyExistException("Book - Tag already exists !");
            }
            bookTagRepository.save(bookTag);
        }

        //책 - 카테고리 등록
        Category category = categoryRepository.findById(bookSaveDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException("Category not found!"));
        BookCategory bookCategory = new BookCategory();
        bookCategory.setBook(book);
        bookCategory.setCategory(category);
        bookCategoryRepository.save(bookCategory);

        // 재고 등록
        StockCreateUpdateDTO stockCreateUpdateDTO = new StockCreateUpdateDTO();
        stockCreateUpdateDTO.setBookId(book.getBookId());

        stockCreateUpdateDTO.setAmount(bookSaveDTO.getStock());

        stockService.addStock(stockCreateUpdateDTO);

        // 이미지 등록
        if(Objects.nonNull(imageFile) && !imageFile.isEmpty()){
            ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
            imageSaveDTO.setBookId(book.getBookId());
            try {
                imageSaveDTO.setImageBytes(imageFile.getBytes());
                imageSaveDTO.setImageName(imageFile.getOriginalFilename());
                imageService.saveImage(imageSaveDTO);
            } catch (IOException e) {
                throw new ImageUploadException("Image upload failed!");
            }
        }
        return book;
    }

    @Override
    @Transactional
    public Book updateBook(Long bookId, BookUpdateDTO bookUpdateDTO) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found!"));

        // 책 정보 수정
        book.setTitle(bookUpdateDTO.getTitle());
        book.setContent(bookUpdateDTO.getContent());
        book.setDescription(bookUpdateDTO.getDescription());
        book.setPrice(bookUpdateDTO.getPrice());
        book.setSalePrice(bookUpdateDTO.getSalePrice());

        // 책 재고 수정
        StockCreateUpdateDTO dto = new StockCreateUpdateDTO();
        dto.setBookId(book.getBookId());
        dto.setAmount(bookUpdateDTO.getStock());
        Stock stock = stockService.updateStock(dto);

        return bookRepository.save(book);
    }


    // 책 삭제
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        // 책 조회
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));

        // 책 삭제
        book.setStatus(true);
    }
    // 베스트셀러 목록 조회
    @Override
    public Page<Book> bestSellerBooks(Pageable pageable) {
        return bookRepository.findAllByStatusFalseOrderByAmountDesc(pageable);
    }
    @Override
    public Page<Book> newBooks(Pageable pageable) {
        return bookRepository.findAllByStatusFalseOrderByPubdateDesc(pageable);
    }
    //책 조회
    @Override
    public Book getBook(long bookId){
        if(!bookRepository.existsById(bookId)){throw new BookNotFoundException("Book not exist !");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));
        return book;
    }

    @Override
    public Page<Book> findAllBooks(Pageable pageable) {
        return bookRepository.findAllByStatusFalseOrderByTitleAsc(pageable);
    }

    @Override
    public List<Book> newBooksTop4() {
        return bookRepository.findTop4ByStatusFalseOrderByPubdateDesc();
    }

    @Override
    public List<Book> bestSellersTop4(){
        return bookRepository.findTop4ByStatusFalseOrderByAmountDesc();
    }


    @Override
    public List<BookSearchAllResponse> searchBookAll(String searchString){
        return bookRepository.findBookByTitle(searchString);
    }

    @Override
    public List<BookRecommendDto> recommendBooks(){
        return bookRepository.reconmmendBooks();
    }


}