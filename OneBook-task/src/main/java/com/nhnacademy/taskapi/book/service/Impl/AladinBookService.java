package com.nhnacademy.taskapi.book.service.Impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskapi.adapter.AladinApiAdapter;
import com.nhnacademy.taskapi.author.domain.Author;
import com.nhnacademy.taskapi.author.service.AuthorService;
import com.nhnacademy.taskapi.book.domain.Book;
import com.nhnacademy.taskapi.book.dto.BookAladinDTO;
import com.nhnacademy.taskapi.book.dto.BookAuthorCreateDTO;
import com.nhnacademy.taskapi.book.dto.BookCategorySaveDTO;
import com.nhnacademy.taskapi.book.exception.BookAlreadyExistException;
import com.nhnacademy.taskapi.book.repository.BookRepository;
import com.nhnacademy.taskapi.book.service.BookAuthorService;
import com.nhnacademy.taskapi.book.service.BookCategoryService;
import com.nhnacademy.taskapi.book.service.BookService;
import com.nhnacademy.taskapi.category.domain.Category;
import com.nhnacademy.taskapi.category.service.CategoryService;
import com.nhnacademy.taskapi.image.domain.Image;
import com.nhnacademy.taskapi.image.dto.ImageSaveDTO;
import com.nhnacademy.taskapi.image.repository.ImageRepository;
import com.nhnacademy.taskapi.image.service.ImageService;
import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import com.nhnacademy.taskapi.publisher.service.PublisherService;
import com.nhnacademy.taskapi.stock.dto.StockCreateUpdateDTO;
import com.nhnacademy.taskapi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AladinBookService {
    private final AladinApiAdapter aladinApiAdapter;
    private final BookRepository bookRepository;
    private final PublisherService publisherService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookAuthorService bookAuthorService;
    private final ImageService imageService;
    private final BookCategoryService bookCategoryService;
    private final StockService stockService;

    // 알라딘 API 데이터 파싱
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<BookAladinDTO> saveAladin() {
        List<BookAladinDTO> dtoList = new ArrayList<>();
        String url = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbtjswns12211534001&QueryType=ItemNewAll&MaxResults=50&start=3&SearchTarget=Book&output=js&Version=20131101";
        String response = aladinApiAdapter.fetchAladinData(url);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.info("response: {}", response);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemNode = rootNode.path("item");

            for (JsonNode item : itemNode) {
                BookAladinDTO dto = new BookAladinDTO();
                dto.setTitle(item.path("title").asText());
                dto.setAuthorName(item.path("author").asText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dto.setPubdate(LocalDate.parse(item.path("pubDate").asText(), formatter));
                dto.setDescription(item.path("description").asText());
                dto.setIsbn13(item.path("isbn13").asText());
                dto.setPriceSales(item.path("priceSales").asInt());
                dto.setPriceStandard(item.path("priceStandard").asInt());
                dto.setCategoryNames(item.path("categoryName").asText());
                dto.setPublisherName(item.path("publisher").asText());
                dto.setSalesPoint(item.path("salesPoint").asLong());
                dto.setCover(item.path("cover").asText());
                dtoList.add(dto);
            }
            return dtoList;

        } catch (JsonProcessingException e) {
            // JsonProcessingException 처리
            throw new RuntimeException("Error processing JSON", e);
        }
    }


    //알라딘 API 베스트셀러 50개 가져오기
    @Transactional
    public void saveBookFromAladin() throws IOException {
        List<BookAladinDTO> dtoList = saveAladin();
        for (BookAladinDTO dto : dtoList) {
            Publisher publisher = publisherService.addPublisherByAladin(dto.getPublisherName());
            Author author = authorService.addAuthorByAladin(dto.getAuthorName());
            Category category = categoryService.addCategoryByAladin(dto.getCategoryNames());

            //책 등록
            Book book = new Book();
            book.setPublisher(publisher);
            book.setTitle(dto.getTitle());
            book.setContent("목차 없음");
            book.setPubdate(dto.getPubdate());
            book.setDescription(dto.getDescription());
            book.setIsbn13(dto.getIsbn13());
            book.setSalePrice(dto.getPriceSales());
            book.setPrice(dto.getPriceStandard());
            book.setAmount(dto.getSalesPoint());
            book.setViews(0);
            if(bookRepository.findByIsbn13(dto.getIsbn13()) != null){
                throw new BookAlreadyExistException("book already exist");
            }else{
                bookRepository.save(book);
            }



            //책-작가 등록
            BookAuthorCreateDTO bookAuthorCreateDTO = new BookAuthorCreateDTO();
            bookAuthorCreateDTO.setBookId(book.getBookId());
            bookAuthorCreateDTO.setAuthorId(author.getAuthorId());
            bookAuthorService.createBookAuthor(bookAuthorCreateDTO);
            //책-카테고리
            BookCategorySaveDTO bookCategorySaveDTO = new BookCategorySaveDTO();
            bookCategorySaveDTO.setBookId(book.getBookId());
            bookCategorySaveDTO.setCategoryId(category.getCategoryId());
            bookCategoryService.save(bookCategorySaveDTO);

            //재고 등록
            StockCreateUpdateDTO stockCreateUpdateDTO = new StockCreateUpdateDTO();
            stockCreateUpdateDTO.setBookId(book.getBookId());
            stockCreateUpdateDTO.setAmount(100);
            stockService.addStock(stockCreateUpdateDTO);

            // 이미지 등록
            String imageUrlString = dto.getCover();
            imageUrlString = imageUrlString.replace("coversum", "cover500");
            URL imageUrl = new URL(imageUrlString);
            String imageName = imageUrlString.substring(imageUrlString.lastIndexOf("/") + 1);
            log.info("imageName: {}", imageName);

            BufferedImage image = ImageIO.read(imageUrl);
            byte[] imageInByte = null;
            if(image != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                baos.flush();
                imageInByte = baos.toByteArray();
                baos.close();
            }else{
                log.info("이미지 읽을수없다");
            }

            ImageSaveDTO imageSaveDTO = new ImageSaveDTO();
            imageSaveDTO.setBookId(book.getBookId());
            imageSaveDTO.setImageBytes(imageInByte);
            imageSaveDTO.setImageName(imageName);
            imageService.saveImage(imageSaveDTO);
        }
    }
}
