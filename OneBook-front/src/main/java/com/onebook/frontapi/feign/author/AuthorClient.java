package com.onebook.frontapi.feign.author;

import com.onebook.frontapi.dto.author.AuthorDTO;
import com.onebook.frontapi.dto.book.BookAuthorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authorClient", url = "${onebook.gatewayUrl}")
public interface AuthorClient {

    @GetMapping("/task/author/{authorId}")
    AuthorDTO getAuthor(@PathVariable("authorId") int authorId);

    @GetMapping("/task/author/list")
    Page<AuthorDTO> getAuthors(Pageable pageable);

}