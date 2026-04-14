package com.marketplace.UserAccountManagement.domain;

import java.util.*;
import java.util.stream.Collectors;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.UserAccountManagement.api.*;
import com.marketplace.Exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class UserService {

//  check is user have the default address

    @Autowired
    private UserRepository sellerRepository;

    public List<User> getAllUser() {
        return sellerRepository.findAll();
    }

    public Optional<User> getUserById(String sellerId) {
        return sellerRepository.findById(sellerId);
    }

    // public Boolean isTheUserThere(String id) {
    //     return getAllUser().stream()
    //         .anyMatch(seller -> seller.getId().contains(id));
    // }

    public List<UserAccountDTO> getAllUserIsDeletedFalse() {
        return getAllUser().stream()
                .filter(user -> user.getIsDeleted() == null || !user.getIsDeleted())
                .map(user -> new UserMapper().toAccountDto(user))
                .toList();
    }

//    public List<AccountProjection> getAllUserIsDeletedFalse() {
//        return sellerRepository.getUsers();
//    }

    public User saveUser(User seller) {
        Objects.requireNonNull(seller);
        return sellerRepository.save(seller);
    }

    public boolean isEmailDuplicate(String email) {
        long count = sellerRepository.isDuplicate(email);
        return count > 0;
    }

    public User updateUser(String id, UserAccountUpdateDTO accountDto) {
        log.info("The email is: {} - the name is: {}", accountDto.email(), accountDto.name());

        if (isEmailDuplicate(accountDto.email())) {
            throw new ResourceDuplicationException("The email has been taken");
        }

        User foundUser = sellerRepository.findActiveById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("There is no user with this id " + id));
        foundUser.setEmail(accountDto.email());
        foundUser.setName(accountDto.name());
        sellerRepository.save(foundUser);
        return foundUser;
    }

    public void updateUserPassword(User foundUser, UserAccountUpdatePasswordDTO accountDto) {
        foundUser.setPassword(foundUser.doEncryptPassword(accountDto.password()));
        sellerRepository.save(foundUser);
    }

    public void deleteUserById(User foundUser) {
//        sellerRepository.delete(foundUser);
        foundUser.setIsDeleted(true);
        sellerRepository.save(foundUser);
    }

    public Boolean checkUserByEmail(String email) {
        return getAllUser().stream()
            .anyMatch(seller -> seller.getEmail().contains(email));
    }

    public Optional<User> getUserByEmail(String email) {
        return getAllUser().stream()
            .filter(seller -> seller.getEmail().equals(email))
            .findAny();
    }

    public UserAccountDTO getUserDataByEmail(String email, UserMapper mapper) {
        User foundUser = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        return mapper.toAccountDto(foundUser);
    }

    @Transactional
    public void addUserAddress(String id, UserAddressCreationDto addressDto) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+id));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }

        Address address = Address.builder()
                .recipientName(addressDto.recipientName())
                .recipientNumber(addressDto.recipientNumber())
                .addressLabel(addressDto.addressLabel())
                .isDeleted(false)
                .cityAndSubsidiary(addressDto.cityAndSubsidiary())
                .completeAddress(addressDto.completeAddress())
.isPicked(false)
                .user(foundUser)
                .build();
        foundUser.addAddress(address);
        sellerRepository.save(foundUser);

    }

    @Transactional(readOnly = true)
    public List<UserAddressDto> getAllUserAddresses(String id) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        List<Address> addresses = new ArrayList<>(foundUser.getAddresses().stream()
                .filter(address -> address.getIsDeleted() == null || address.getIsDeleted().equals(false))
                .toList());

        addresses.sort(new Comparator<Address>() {
            @Override
            public int compare(Address o1, Address o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });

        return addresses.stream()
                .map(address -> new UserAddressDto(address.getId(), address.getRecipientName(), address.getRecipientNumber(), address.getAddressLabel(), address.getCityAndSubsidiary(), address.getCompleteAddress(), address.getIsPicked()))
                .toList();
//                .map()
                // fix the comparator
//                .sorted(Comparator.comparing(address -> address.getCreatedAt().before(address.getCreatedAt())))
//                .collect(Collectors.toCollection(LinkedList::new));

    }

    @Transactional
    public void updateSelectedAddress(String userId, String addressId) {
//        if(indices.size() != 2) throw new IllegalArgumentException("You need to put the previous index and the current index");
        User foundUser = getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ userId));
        Set<Address> addresses = foundUser.getAddresses();
        Address foundOldAddress = addresses
                .stream()
                .filter(address -> address.getIsPicked().equals(true))
                .findAny()
                .orElse(null);
        Address foundAddress = addresses.stream()
                .filter(address -> address.getId().equals(addressId) && (address.getIsDeleted() == null || address.getIsDeleted().equals(false)))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("The address with this id is not found"));

        foundAddress.setIsPicked(true);

        if (foundOldAddress != null) {
            foundOldAddress.setIsPicked(false);
        }
        //        List<Address> addresses = new ArrayList<>(foundUser.getAddresses().stream()
//                .filter(address -> address.getId().equals(addressId))
//                .filter(address -> address.getIsDeleted() == null || address.getIsDeleted().equals(false))
//                .toList());
//
        foundUser.getAddresses().stream().sorted(new Comparator<Address>() {
            @Override
            public int compare(Address o1, Address o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });
        sellerRepository.save(foundUser);
////        System.out.println("indices = " + indices.get(0));
////        Integer prevIndex = indices.get(0) == null ? -1 : indices.get(0);
////        System.out.println("prevIndex = " + prevIndex);
//        if (indices.previousIndex() == -1) {
//            addresses.get(indices.currentIndex()).setIsPicked(true);
////            foundUser.setAddresses(new HashSet<>(addresses));
//            saveUser(foundUser);
//            addresses.stream()
//                    .map(address -> new UserAddressDto(address.getId(), address.getRecipientName(), address.getRecipientNumber(), address.getAddressLabel(), address.getCityAndSubsidiary(), address.getCompleteAddress(), address.getIsPicked()))
//                    .collect(Collectors.toCollection(LinkedList::new));
//
////            addresses.get(indices.get(0)).setIsPicked(false);
//        }
//        System.out.println("indices.previousIndex() = " + indices.previousIndex());
//        System.out.println("indices.currentIndex() = " + indices.currentIndex());
//        if (indices.previousIndex() > addresses.size() || indices.currentIndex() > addresses.size() || indices.currentIndex() < 0) {
//            throw new IllegalArgumentException("There is no index like that");
//        }
////        iterate address and isPickedList first or you can update all addresses
////        or just send previous index and current index to this server
////        List<Address> addresses = new ArrayList<>(foundUser.getAddresses());
//        System.out.println("Not prev index = -1");
//        addresses.get(indices.previousIndex()).setIsPicked(false);
//        addresses.get(indices.currentIndex()).setIsPicked(true);
////        for (int i = 0; i < addresses.size(); i++) {
////            addresses.get(i).setIsPicked(isPickedList.get(i));
////        }
//
////        foundUser.setAddresses(new HashSet<>(addresses));
//        saveUser(foundUser);
//        addresses.stream()
//                .map(address -> new UserAddressDto(address.getId(), address.getRecipientName(), address.getRecipientNumber(), address.getAddressLabel(), address.getCityAndSubsidiary(), address.getCompleteAddress(), address.getIsPicked()))
////                .toList();
//                .collect(Collectors.toCollection(LinkedList::new));

    }

    @Transactional
    public void updateUserAddress(String id, String addressId, UserAddressCreationDto addressDto) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        if (foundUser.getIsDeleted() != null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        Address address = foundUser.getAddresses().stream()
                .filter(existingAddress -> existingAddress.getId().equals(addressId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + id));
        address.setCompleteAddress(addressDto.completeAddress());
        address.setAddressLabel(addressDto.addressLabel());
        address.setRecipientName(addressDto.recipientName());
        address.setRecipientNumber(addressDto.recipientNumber());
        address.setCityAndSubsidiary(addressDto.cityAndSubsidiary());
        saveUser(foundUser);
    }

    public UserAccountDTO getUserDataById(String id, UserMapper mapper) {
        User foundSeller = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id ));
        if (foundSeller.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        return mapper.toAccountDto(foundSeller);
    }

    public void deleteAccountById(String id) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        deleteUserById(foundUser);
    }
    
}
