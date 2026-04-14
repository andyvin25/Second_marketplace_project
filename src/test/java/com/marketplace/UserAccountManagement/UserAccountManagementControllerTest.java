package com.marketplace.UserAccountManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.Auth.domain.AuthTokenFilter;
import com.marketplace.Auth.domain.JwtUtil;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.RateLimiter.RateLimiterService;
import com.marketplace.UserAccountManagement.api.UserAccountController;
import com.marketplace.UserAccountManagement.api.UserAccountDTO;
import com.marketplace.UserAccountManagement.api.UserAddressCreationDto;
import com.marketplace.UserAccountManagement.api.UserMapper;
import com.marketplace.UserAccountManagement.domain.Address;
import com.marketplace.UserAccountManagement.domain.AddressService;
import com.marketplace.UserAccountManagement.domain.User;
import com.marketplace.UserAccountManagement.domain.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserAccountController.class)
@TestMethodOrder(MethodOrderer.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserAccountManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private RateLimiterService rateLimiterService;

    @MockitoBean
    AuthTokenFilter authTokenFilter;

    @MockitoBean
    private UserMapper mapper;

    private User user;

    @BeforeEach
    public void setup() {
        String accountId = "account_id";
        String accountNameTest = "account_name_test";
        String accountEmailTest = "account_email_test";
        String accountPassword = "account_password_test";

        user = User.builder()
                .id(accountId)
                .name(accountNameTest)
                .email(accountEmailTest)
                .password(accountPassword)
                .build();
    }

    @RepeatedTest(3)
    public void getUserByEmail_whenSuccess_returnUserAccountDto() throws Exception {
        String jwtToken = "your-jwt-token-here"; // Replace with a method to generate a valid token
        String accountEmailTest1 = "account_email_test";

        UserAccountDTO userAccountDTO = new UserAccountDTO(user.getId(), user.getEmail(), user.getName());

        given(userService.getUserDataByEmail(user.getEmail(), mapper)).willReturn(userAccountDTO);

        UserAccountDTO dto = userService.getUserDataByEmail(accountEmailTest1, mapper);

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/user/email")
                .param("email", accountEmailTest1)
                .header("Authorization", "Bearer " + jwtToken));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userAccountDTO.id()))
                .andExpect(jsonPath("$.email").value(userAccountDTO.email()))
                .andExpect(jsonPath("$.user_name").value(userAccountDTO.userName()));
//        mockMvc.perform(get("/user/email, "01K15M5SR2GXN64AEEBZ6AT0PR")
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk());
    }

    @RepeatedTest(3)
    public void getUserByEmail_whenEmailNotFound_throwNotFoundException() throws Exception {
        String jwtToken = "your-jwt-token-here"; // Replace with a method to generate a valid token
        String accountEmailTest1 = "unknown";

        given(userService.getUserDataByEmail(accountEmailTest1, mapper)).willThrow(new ResourceNotFoundException("User not found with email " + accountEmailTest1));

        ResultActions response = mockMvc.perform(get("http://localhost:8080/api/user/email")
                .param("email", accountEmailTest1)
                .header("Authorization", "Bearer " + jwtToken));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with email " + accountEmailTest1));
    }

    @RepeatedTest(3)
    public void addUserAddress_whenSuccess_returnCreatedStatus() throws Exception {
        String jwtToken = "your-jwt-token-here"; // Replace with a method to generate a valid token
        String userId = "account_id";

        given(userService.saveUser(user)).willReturn(user);
        String cityAndSub = "city_test";
        Address.AddressLabelEnum addressLabel = Address.AddressLabelEnum.RUMAH;
        String completeAddress = "complete_address_test";
        String recipientName = "recipient_name_test";
        String recipientNumber = "0432580238450328";

        UserAddressCreationDto userAddressDto = new UserAddressCreationDto(recipientName, recipientNumber, addressLabel, cityAndSub, completeAddress, false);

        given(userService.getUserById(userId)).willReturn(Optional.of(user));
        User user1 = userService.getUserById(userId).get();
        System.out.println("user1 = " + user1);
        willDoNothing().given(userService).addUserAddress(userId, userAddressDto);

        ResultActions response = mockMvc.perform(post("http://localhost:8080/api/users/{user_id}/addresses", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAddressDto))
                .header("Authorization", "Bearer " + jwtToken));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Address successfully added"));
    }

//    public void addUserAddress_whenSuccess_returnCreatedStatus() throws Exception {
//        String cityAndSub = "city_test";
//        Address.AddressLabelEnum addressLabel = Address.AddressLabelEnum.HOME;
//        String completeAddress = "complete_address_test";
//        String recipientName = "recipient_name_test";
//        String recipientNumber = "0432580238450328";
//
//        UserAddressCreationDto userAddressDto = new UserAddressCreationDto(
//                recipientName, recipientNumber, addressLabel, cityAndSub, completeAddress, false
//        );
//
//        // Mock userService behavior
//        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
//        willDoNothing().given(userService).addUserAddress(user.getId(), userAddressDto);
//
//        // Serialize UserAddressCreationDto to JSON
//        String json = objectMapper.writeValueAsString(userAddressDto);
//
//        // Perform the request using MockMvc
//        ResultActions response = mockMvc.perform(post("/users/{user_id}/addresses", user.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json));
//
//        // Verify the response
//        response.andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("Address successfully added"));
//    }



}
