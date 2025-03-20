package com.onebook.frontapi.controller.home;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.book.ProductDTO;
import com.onebook.frontapi.dto.category.CategoryDTO;
import com.onebook.frontapi.dto.image.ImageDTO;
import com.onebook.frontapi.feign.auth.AuthFeignClient;
import com.onebook.frontapi.service.book.BookService;
import com.onebook.frontapi.service.category.CategoryService;
import com.onebook.frontapi.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final BookService bookService;
    private final ImageService imageService;
    private final CategoryService categoryService;


    @Value("${server.port}")
    private String port;


    @GetMapping("/")
    public String home(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                       Model model, Pageable pageable) {
        log.info("BookClient: {}", bookService);

        List<BookDTO> bookList = bookService.newBooksTop4();
        List<ImageDTO> imageList = new ArrayList<>();
        List<Long> bookIdList = new ArrayList<>();
        for(BookDTO bookDTO : bookList) {
            bookIdList.add(bookDTO.getBookId());
        }
        for(long bookId : bookIdList) {
            imageList.add(imageService.getImage(bookId));
        }

//        for(ImageDTO imageDTO : imageList) {
//            imageList.add(imageService.getImage());
//        }

        List<ProductDTO> productDTOList = new ArrayList<>();

            for(int i = 0; i < bookList.size(); i++){
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setBookId(bookList.get(i).getBookId());
                    productDTO.setContent(bookList.get(i).getContent());
                    productDTO.setTitle(bookList.get(i).getTitle());
                    productDTO.setSalePrice(bookList.get(i).getSalePrice());
                    productDTO.setPrice(bookList.get(i).getPrice());
                    productDTO.setAmount(bookList.get(i).getAmount());
                    productDTO.setDescription(bookList.get(i).getDescription());
                    productDTO.setPubdate(bookList.get(i).getPubdate());
                    productDTO.setIsbn13(bookList.get(i).getIsbn13());
                    productDTO.setPublisher(bookList.get(i).getPublisher());
                    productDTO.setViews(bookList.get(i).getViews());
                    productDTO.setUrl(imageList.get(i).getUrl());
                    productDTOList.add(productDTO);
            }

        List<BookDTO> bestsellers = bookService.bestSellersTop4();
        List<ImageDTO> images = new ArrayList<>();
        List<Long> bookIds = new ArrayList<>();
        for(BookDTO bookDTO : bestsellers) {
            bookIds.add(bookDTO.getBookId());
        }
        for(long bookId : bookIds) {
            images.add(imageService.getImage(bookId));
        }


        List<ProductDTO> productDTOs = new ArrayList<>();

        for(int i = 0; i < bestsellers.size(); i++){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setBookId(bestsellers.get(i).getBookId());
            productDTO.setContent(bestsellers.get(i).getContent());
            productDTO.setTitle(bestsellers.get(i).getTitle());
            productDTO.setSalePrice(bestsellers.get(i).getSalePrice());
            productDTO.setPrice(bestsellers.get(i).getPrice());
            productDTO.setAmount(bestsellers.get(i).getAmount());
            productDTO.setDescription(bestsellers.get(i).getDescription());
            productDTO.setPubdate(bestsellers.get(i).getPubdate());
            productDTO.setIsbn13(bestsellers.get(i).getIsbn13());
            productDTO.setPublisher(bestsellers.get(i).getPublisher());
            productDTO.setViews(bestsellers.get(i).getViews());
            productDTO.setUrl(images.get(i).getUrl());
            productDTOs.add(productDTO);
        }

        model.addAttribute("imageList", imageList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("productList", productDTOList);
        model.addAttribute("bestsellers", productDTOs);


        return "index/index";
    }

}