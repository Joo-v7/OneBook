package com.nhnacademy.taskapi.packaging.service;

import com.nhnacademy.taskapi.packaging.dto.PackagingRequestDto;
import com.nhnacademy.taskapi.packaging.dto.PackagingResponseDto;
import com.nhnacademy.taskapi.packaging.entity.Packaging;

import java.util.List;

public interface PackagingService {
    // create
    void createPackaging(PackagingRequestDto packagingRequestDto);

    // read
    public List<PackagingResponseDto> getAllPackaging();
    public Packaging getPackagingById(int id);

    // update

    // delete
    public void deletePackagingById(int id);
}
