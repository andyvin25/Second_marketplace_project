package com.marketplace.UserAccountManagement.domain;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.UserAccountManagement.api.UserAddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service("AddressService")
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Optional<Address> getAddressById(String id) {
        return addressRepository.findById(id);
    }

    public void saveAddress(Address address) {
        Objects.requireNonNull(address);
        addressRepository.save(address);
    }

    public UserAddressDto getUserAddress(String id) {
        Address address = getAddressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with this id" + id));
        if (address.getIsDeleted() == true) {
            throw new ResourceNotFoundException("Address not found with this id" + id);
        }
//        addressRepository.findById()
        return new UserAddressDto(address.getId(), address.getRecipientName(), address.getRecipientNumber(), address.getAddressLabel(), address.getCityAndSubsidiary(), address.getCompleteAddress(), address.getIsPicked());
    }

    public void deleteAddress(String id) {
        Address address = getAddressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with this id" + id));
        if (address.getIsDeleted() == true) {
            throw new ResourceNotFoundException("Address with this id" + id + "has already been deleted");
        }
        address.setIsDeleted(true);
        saveAddress(address);
    }
}
