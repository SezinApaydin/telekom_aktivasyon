package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.Address;
import com.etiya.telekomaktivasyon.exception.BusinessException;
import com.etiya.telekomaktivasyon.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(Address address) {
        validate(address);
        return addressRepository.save(address);
    }

    private void validate(Address address) {
        if (isBlank(address.getCity())) {
            throw new BusinessException("Şehir alanı boş olamaz.");
        }
        if (isBlank(address.getDistrict())) {
            throw new BusinessException("İlçe alanı boş olamaz.");
        }
        if (isBlank(address.getAddressLine())) {
            throw new BusinessException("Açık adres alanı boş olamaz.");
        }
        if (address.getPostalCode() != null && !address.getPostalCode().isBlank()
                && !address.getPostalCode().matches("\\d{5}")) {
            throw new BusinessException("Posta kodu 5 haneli bir sayı olmalıdır.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}