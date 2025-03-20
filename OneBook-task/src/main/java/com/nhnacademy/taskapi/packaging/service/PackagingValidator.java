package com.nhnacademy.taskapi.packaging.service;

import com.nhnacademy.taskapi.packaging.entity.Packaging;
import com.nhnacademy.taskapi.packaging.repository.PackagingRepository;

public interface PackagingValidator {
    Packaging validatePackaging(int packagingId, int itemCount, PackagingRepository packagingRepository);
}
