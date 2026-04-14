package com.marketplace.ProductEtalationManagement.api;

import org.springframework.stereotype.Component;

import com.marketplace.ProductEtalationManagement.domain.Etalation;

@Component
public class EtalationMapper {
    
    public Etalation toEtalation(EtalationRequestDto etalationDto) {
        return new Etalation(etalationDto.etalationName());
    }

    public EtalationDto toDto(Etalation etalation){
        return new EtalationDto(etalation.getId(), etalation.getEtalationName());
    }

}
