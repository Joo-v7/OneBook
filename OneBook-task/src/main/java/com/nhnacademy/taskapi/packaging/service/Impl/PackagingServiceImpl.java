package com.nhnacademy.taskapi.packaging.service.Impl;

import com.nhnacademy.taskapi.packaging.dto.PackagingRequestDto;
import com.nhnacademy.taskapi.packaging.dto.PackagingResponseDto;
import com.nhnacademy.taskapi.packaging.entity.Packaging;
import com.nhnacademy.taskapi.packaging.exception.PackagingAlreadyExistException;
import com.nhnacademy.taskapi.packaging.exception.PackagingNotFoundException;
import com.nhnacademy.taskapi.packaging.repository.PackagingRepository;
import com.nhnacademy.taskapi.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PackagingServiceImpl implements PackagingService {

    private final PackagingRepository packagingRepository;

    // create
    public void createPackaging(PackagingRequestDto packagingRequestDto) {
        if (packagingRepository.existsByName((packagingRequestDto.getName()))) {
            throw new PackagingAlreadyExistException(packagingRequestDto.getName() + "is already exist name");
        }
        // TODO packaging 에러 반환
        if (packagingRequestDto.getName().isBlank()) {
//            throw new Packaging
        }

        Packaging packaging = new Packaging(packagingRequestDto.getName(), packagingRequestDto.getPrice());

        packagingRepository.save(packaging);
    }

    // read
    public List<PackagingResponseDto> getAllPackaging() {
        List<PackagingResponseDto> list = packagingRepository.findAll().stream().map(PackagingResponseDto::fromPackaging).toList();
        return list;
    };

    public Packaging getPackagingById(int id) {
        return packagingRepository.findById(id).orElseThrow(() -> new PackagingNotFoundException(id + "is not found."));
    }

    // update

    // delete
    public void deletePackagingById(int id) {
        if (packagingRepository.existsById(id)) {
            throw new PackagingNotFoundException(id + "is not found");
        }
        packagingRepository.deleteById(id);
    }

}
