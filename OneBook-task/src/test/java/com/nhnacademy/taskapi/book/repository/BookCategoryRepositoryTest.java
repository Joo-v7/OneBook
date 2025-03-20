package com.nhnacademy.taskapi.book.repository;

import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.domain.BookCategory;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.repository.CategoryRepository;
import com.nhnacademy.taskapi.config.QuerydslConfig;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)

public class BookCategoryRepositoryTest {
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByBook_bookIdAndCategory_categoryIdTest() {
        Publisher publisher = new Publisher();
        publisher.setName("test");
        publisherRepository.save(publisher);
        // Given
        Book book = new Book();
        book.setTitle("Test Book");
        book.setContent("Test Content");
        book.setDescription("Test Description");
        book.setIsbn13("978-1-234-56789-0");
        book.setPrice(1000);
        book.setSalePrice(800);
        book.setAmount(100);
        book.setPublisher(publisher);
        book.setViews(0);
        book.setPubdate(java.time.LocalDate.of(2023, 1, 1));
        bookRepository.save(book);

        Category category = new Category();
        category.setName("Fiction");
        category.setParentCategory(null);

        categoryRepository.save(category);

        BookCategory bookCategory = new BookCategory();
        bookCategory.setBook(book);
        bookCategory.setCategory(category);

        // Save BookCategory
        bookCategoryRepository.save(bookCategory);

        // When
        BookCategory result = bookCategoryRepository.findByBook_bookIdAndCategory_categoryId(book.getBookId(), category.getCategoryId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBook().getTitle()).isEqualTo("Test Book");
        assertThat(result.getCategory().getName()).isEqualTo("Fiction");
    }

//    @Test
//    void deleteByBookTest() {
//        Publisher publisher = new Publisher();
//        publisher.setName("test");
//        publisherRepository.save(publisher);
//        // Given
//        Book book = new Book();
//        book.setTitle("Test Book for Deletion");
//        book.setContent("Test Content");
//        book.setDescription("Test Description");
//        book.setIsbn13("978-1-234-56789-0");
//        book.setPrice(1000);
//        book.setSalePrice(800);
//        book.setAmount(100);
//        book.setViews(0);
//        book.setPublisher(publisher);
//        book.setPubdate(java.time.LocalDate.of(2023, 1, 1));
//        bookRepository.save(book);
//
//        Category category = new Category();
//        category.setName("Non-Fiction");
//        category.setParentCategory(null);
//        categoryRepository.save(category);
//
//        BookCategory bookCategory = new BookCategory();
//        bookCategory.setBook(book);
//        bookCategory.setCategory(category);
//
//        // Save BookCategory
//        bookCategoryRepository.save(bookCategory);
//
//        // When
//        bookCategoryRepository.deleteByBook(book);
//
//        // Then
//        BookCategory result = bookCategoryRepository.findByBook_bookIdAndCategory_categoryId(book.getBookId(), category.getCategoryId());
//        assertThat(result).isNull();  // 삭제 후, 해당 BookCategory가 없음을 확인
//    }

//    @Test
//    void findByCategoryTest() {
//        // Given
//        Category category = new Category();
//        category.setName("Science");
//        category.setParentCategory(null);
//        categoryRepository.save(category);
//
//
//        Publisher publisher = new Publisher();
//        publisher.setName("test");
//        publisherRepository.save(publisher);
//
//        Book book1 = new Book();
//        book1.setTitle("Science Book 1");
//        book1.setContent("Content 1");
//        book1.setDescription("Description 1");
//        book1.setIsbn13("978-1-234-56789-0");
//        book1.setPrice(1200);
//        book1.setSalePrice(1000);
//        book1.setAmount(200);
//        book1.setViews(5);
//        book1.setPublisher(publisher);
//        book1.setPubdate(java.time.LocalDate.of(2023, 1, 1));
//
//        bookRepository.save(book1);
//
//        Book book2 = new Book();
//        book2.setTitle("Science Book 2");
//        book2.setContent("Content 2");
//        book2.setDescription("Description 2");
//        book2.setIsbn13("978-1-234-56789-1");
//        book2.setPrice(1500);
//        book2.setSalePrice(1200);
//        book2.setAmount(150);
//        book2.setViews(10);
//        book2.setPublisher(publisher);
//        book2.setPubdate(java.time.LocalDate.of(2023, 2, 1));
//
//        bookRepository.save(book2);
//
//        BookCategory bookCategory1 = new BookCategory();
//        bookCategory1.setBook(book1);
//        bookCategory1.setCategory(category);
//
//        BookCategory bookCategory2 = new BookCategory();
//        bookCategory2.setBook(book2);
//        bookCategory2.setCategory(category);
//
//        // Save BookCategories
//        bookCategoryRepository.save(bookCategory1);
//        bookCategoryRepository.save(bookCategory2);
//
//        // When
//        List<BookCategory> result = bookCategoryRepository.findByCategory(category);
//
//        // Then
//        assertThat(result).isNotEmpty();
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getCategory().getName()).isEqualTo("Science");
//        assertThat(result.get(1).getCategory().getName()).isEqualTo("Science");
//    }
}
