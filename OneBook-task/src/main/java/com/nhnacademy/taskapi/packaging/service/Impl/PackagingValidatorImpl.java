package com.nhnacademy.taskapi.packaging.service.Impl;

import com.nhnacademy.taskapi.packaging.entity.Packaging;
import com.nhnacademy.taskapi.packaging.exception.PackagingNotAvailableException;
import com.nhnacademy.taskapi.packaging.exception.PackagingNotFoundException;
import com.nhnacademy.taskapi.packaging.repository.PackagingRepository;
import com.nhnacademy.taskapi.packaging.service.PackagingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class PackagingValidatorImpl implements PackagingValidator {
    public Packaging validatePackaging(int packagingId, int itemCount, PackagingRepository packagingRepository) {
        if (packagingId != 0 && itemCount > 10) {
            throw new PackagingNotAvailableException();
        }

        return packagingRepository.findById(packagingId)
                .orElseThrow(() -> new PackagingNotFoundException(packagingId + " is not found."));
    }
}
