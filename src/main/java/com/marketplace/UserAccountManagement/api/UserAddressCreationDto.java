package com.marketplace.UserAccountManagement.api;

import com.marketplace.UserAccountManagement.domain.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@RegisterReflectionForBinding
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAddressCreationDto(
        @NotBlank(message = "recipientName can't be empty")
        @Size(max= 30, message = "Max character is 30")
        String recipientName,
        @NotBlank(message = "recipientNumber can't be empty")
        @Size(max = 15, message = "the complete recipient number range is max is 15 characters")
        String recipientNumber,
        Address.AddressLabelEnum addressLabel,
        @NotBlank(message = "city And Subsidiary can't be empty")
        @Size(max = 100, message = "max Charachter is 100")
        String cityAndSubsidiary,
        @NotBlank(message = "completeAddress can't be empty")
        @Size(max = 100, message = "max Charachter is 100")
        String completeAddress,
        Boolean isPicked
) {
}
