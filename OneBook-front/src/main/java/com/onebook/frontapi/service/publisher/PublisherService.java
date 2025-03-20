package com.onebook.frontapi.service.publisher;

import com.onebook.frontapi.dto.book.BookDTO;
import com.onebook.frontapi.dto.publisher.PublisherDTO;
import com.onebook.frontapi.feign.publisher.PublisherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherClient publisherClient;

    public Page<PublisherDTO> getList(Pageable pageable) {
        return publisherClient.getPublisherList(pageable);
    }

    public PublisherDTO getById(Long publisherId) {
        return publisherClient.getPublisherById(publisherId);
    }


}
