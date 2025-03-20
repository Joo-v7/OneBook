package com.nhnacademy.taskapi.publisher.service.serviceImpl;

import com.nhnacademy.taskapi.publisher.domain.Publisher;
import com.nhnacademy.taskapi.publisher.dto.PublisherUpdateDTO;
import com.nhnacademy.taskapi.publisher.exception.InvalidPublisherNameException;
import com.nhnacademy.taskapi.publisher.exception.PublisherAlreadyExistException;
import com.nhnacademy.taskapi.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.taskapi.publisher.repository.PublisherRepository;
import com.nhnacademy.taskapi.publisher.service.PublisherService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;


    //출판사 등록
    @Transactional
    @Override
    public Publisher addPublisher(String name) {
        if(Objects.isNull(name) || name.trim().isEmpty()){
            throw new InvalidPublisherNameException("PublisherName is Null OR Empty !");
        }
        Publisher publisher = publisherRepository.findByName(name);

        if(Objects.nonNull(publisher)){
            throw new PublisherAlreadyExistException("This Publisher Already Exist !");
        }

        publisher = new Publisher();
        publisher.setName(name);
        return publisherRepository.save(publisher);
    }

    @Transactional
    @Override
    public Publisher addPublisherByAladin(String name){
        if(Objects.isNull(name) || name.trim().isEmpty()){
            throw new InvalidPublisherNameException("PublisherName is Null OR Empty !");
        }
        Publisher publisher = publisherRepository.findByName(name);
        if(Objects.isNull(publisher)){
            publisher = new Publisher();
            publisher.setName(name);
            return publisherRepository.save(publisher);
        }
        return publisher;
    }

    @Override
    @Transactional
    public Publisher updatePublisher(PublisherUpdateDTO dto){
        Publisher publisher = publisherRepository.findById(dto.getPublisherId()).orElseThrow(() -> new PublisherNotFoundException("This Publisher Not Exist !"));

        if(Objects.isNull(dto.getPublisherName()) || dto.getPublisherName().trim().isEmpty()){
            throw new InvalidPublisherNameException("This PublisherName is Null Or Empty");
        }

        publisher.setName(dto.getPublisherName());

        return publisherRepository.save(publisher);
    }

    @Override
    @Transactional
    public void deletePublisher(long publisherId){
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> new PublisherNotFoundException("This Publisher Not Exist !"));
        publisherRepository.delete(publisher);
    }

    @Override
    public Publisher getPublisher(String publisherName){
        Publisher publisher = publisherRepository.findByName(publisherName);

        if(Objects.isNull(publisher)){
            throw new PublisherNotFoundException("This Publisher Not Exist !");
        }
        return publisher;
    }

    @Override
    public List<Publisher> getPublisherList(String name){
        if(Objects.isNull(name) || name.trim().isEmpty()){
            throw new InvalidPublisherNameException("PublisherName is Null OR Empty !");
        }

        List<Publisher> publisherList = publisherRepository.findAllByName(name);
        return publisherList;

    }

    @Override
    public Page<Publisher> getList(Pageable pageable){
        return publisherRepository.findAllByOrderByPublisherIdAsc(pageable);
    }
    public Publisher getPublisherById(long publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new PublisherNotFoundException("Publisher not found with ID: " + publisherId));
    }


}
