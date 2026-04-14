package com.marketplace.Auth.domain;

import com.marketplace.Auth.api.UserAccountCreationDTO;
import com.marketplace.Auth.api.UserMapper;
import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("CommonUserService")
public class UserService {

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public User getActiveUserByEmail(String email) {
        return userRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Please fix your email and password"));
    }

    public boolean isEmailDuplicate(String email) {
        long count = userRepository.isDuplicate(email);
        return count > 0;
    }

    @Transactional
    public User createUserAccount(Role role, UserMapper mapper, UserAccountCreationDTO accountDTO) {
        if(isEmailDuplicate(accountDTO.email())) {
            throw new ResourceDuplicationException("This value is duplicate");
        }
        User user = mapper.toUserAccount(accountDTO);
        user.addRole(role);

        Set<User> users = new HashSet<>();
        users.add(user);
        role.setUsers(users);

        userRepository.save(user);

        return user;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findActiveByEmail(email);
    }

//    private List<Role> getUserRoles(String email) {
//        User user;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            TypedQuery<User> query = session.createQuery("From User U WHERE U.email =:user_email", User.class);
//            query.setParameter("user_email", email);
//            user = query.getSingleResult();
//            System.out.println("user.getAccountRoles() = " + user.getAccountRoles());
//        }
//        System.out.println("user account roles is " + user.getAccountRoles().stream().toList());
//        return user.getAccountRoles().stream().toList();
//    }
//
//    public String authenticateUser(UserLoginDto userLoginDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userLoginDto.email(),
//                        userLoginDto.password()
//                )
//        );
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        System.out.println("user details get authorities is " + userDetails.getAuthorities());
//        return jwtUtil.generateToken(userDetails.getUsername(), getUserRoles(userDetails.getUsername()));
//    }

}
