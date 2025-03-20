package com.nhnacademy.taskapi.book.service;


import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.book.dto.BookCategorySaveDTO;
import com.nhnacademy.taskapi.book.exception.BookCategoryDuplicateException;
import com.nhnacademy.taskapi.book.exception.BookCategoryNotFoundException;
import com.nhnacademy.taskapi.book.repository.BookCategoryRepository;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.service.Impl.BookCategoryServiceImpl;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookCategoryServiceTest {

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookCategoryServiceImpl bookCategoryService;


    @Test
    @DisplayName("save_Success")
    public void save_Success() {
        Publisher publisher = new Publisher();
        publisher.setName("test");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(java.time.LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);

        Category category = new Category();
        category.setCategoryId(1);
        category.setName("test");
        category.setParentCategory(null);


        when(bookCategoryRepository.findByBook_bookIdAndCategory_categoryId(any(Long.class), any(Integer.class))).thenReturn(null);
        when(bookRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(book));
        when(categoryRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(category));

        BookCategorySaveDTO dto = new BookCategorySaveDTO();
        dto.setBookId(book.getBookId());
        dto.setCategoryId(category.getCategoryId());

        BookCategory bookCategory = new BookCategory();
        bookCategory.setBook(book);
        bookCategory.setCategory(category);

        when(bookCategoryRepository.save(any(BookCategory.class))).thenReturn(bookCategory);

        BookCategory result = bookCategoryService.save(dto);

        Assertions.assertNotNull(result);
        verify(bookCategoryRepository).save(any(BookCategory.class));
    }

    @Test
    @DisplayName("save_Fail_Duplicate")
    public void save_Fail_Duplicate() {
        Publisher publisher = new Publisher();
        publisher.setName("test");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("test");
        book.setPrice(100);
        book.setContent("test");
        book.setAmount(10);
        book.setDescription("test");
        book.setPubdate(java.time.LocalDate.now());
        book.setIsbn13("1111111111111");
        book.setSalePrice(500);
        book.setPublisher(publisher);

        Category category = new Category();
        category.setCategoryId(1);
        category.setName("test");
        category.setParentCategory(null);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setBook(book);
        bookCategory.setCategory(category);

        BookCategorySaveDTO dto = new BookCategorySaveDTO();
        dto.setBookId(book.getBookId());
        dto.setCategoryId(category.getCategoryId());

        when(bookCategoryRepository.findByBook_bookIdAndCategory_categoryId(any(Long.class), any(Integer.class))).thenReturn(bookCategory);

        Assertions.assertThrows(BookCategoryDuplicateException.class, () -> bookCategoryService.save(dto));
    }

//    @Test
//    @DisplayName("getBookByCategory_Success")
//    public void getBookByCategory_Success() {
//        // Arrange
//        int categoryId = 1;
//
//        // 가짜 데이터 준비
//        Book book1 = new Book();
//        book1.setBookId(1L);
//        book1.setTitle("Test Book 1");
//
//        Book book2 = new Book();
//        book2.setBookId(2L);
//        book2.setTitle("Test Book 2");
//
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//        category.setName("Test Category");
//
//        BookCategory bookCategory1 = new BookCategory();
//        bookCategory1.setBook(book1);
//        bookCategory1.setCategory(category);
//
//        BookCategory bookCategory2 = new BookCategory();
//        bookCategory2.setBook(book2);
//        bookCategory2.setCategory(category);
//
//        List<BookCategory> mockList = List.of(bookCategory1, bookCategory2);
//
//        when(bookCategoryRepository.findAllByCategory_CategoryId(categoryId)).thenReturn(mockList);
//
//        // Act
//        List<BookCategory> result = bookCategoryService.getBookByCategory(categoryId);
//
//        // Assert
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(2, result.size());
//        Assertions.assertEquals("Test Book 1", result.get(0).getBook().getTitle());
//        Assertions.assertEquals("Test Category", result.get(0).getCategory().getName());
//
//        verify(bookCategoryRepository, times(1)).findAllByCategory_CategoryId(categoryId);
//    }
//
//    @Test
//    @DisplayName("getBookByCategory_Fail_NotFound")
//    public void getBookByCategory_Fail_NotFound() {
//        when(bookCategoryRepository.findAllByCategory_CategoryId(any(Integer.class))).thenReturn(null);
//
//        Assertions.assertThrows(BookCategoryNotFoundException.class, () -> bookCategoryService.getBookByCategory(1));
//
//    }

}
