package com.github.ideepakkalra.eventmanagement.config;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.model.*;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean (name = "communicationReferralRequestToCommunicationReferralModelMapper")
    public ModelMapper createCommunicationReferralRequestToCommunicationReferral() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<CommunityReferralRequest, CommunityReferral> typeMap = modelMapper.createTypeMap(CommunityReferralRequest.class, CommunityReferral.class);
        typeMap.setPropertyCondition(Conditions.isNotNull());
        Converter<String, CommunityReferral.State> stateConverter = mappingContext -> CommunityReferral.State.valueOf(mappingContext.getSource());
        typeMap.addMappings(mapper -> mapper.using(stateConverter).map(CommunityReferralRequest::getState, CommunityReferral::setState));
        typeMap.addMappings(mapper ->  mapper.skip(CommunityReferral::setReferrer));
        return modelMapper;
    }

    @Bean (name = "communicationReferralToCommunicationReferralResponseModelMapper")
    public ModelMapper createCommunicationReferralToCommunicationReferralResponse() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<CommunityReferral, CommunityReferralResponse> typeMap = modelMapper.createTypeMap(CommunityReferral.class, CommunityReferralResponse.class);
        typeMap.setPropertyCondition(Conditions.isNotNull());
        Converter<CommunityReferral.State, String> stateConverter = mappingContext -> mappingContext.getSource().toString();
        typeMap.addMappings(mapper -> mapper.using(stateConverter).map(CommunityReferral::getState, CommunityReferralResponse::setState));
        Converter<User, Long> referrerConverter = mappingContext -> mappingContext.getSource().getId();
        typeMap.addMappings(mapper -> mapper.using(referrerConverter).map(CommunityReferral::getReferrer, CommunityReferralResponse::setReferrer));
        return modelMapper;
    }

    @Bean (name = "loginRequestToLoginModelMapper")
    public ModelMapper loginRequestToLoginModelMapper() {
        return new ModelMapper();
    }

    @Bean (name = "loginToLoginResponseModelMapper")
    public ModelMapper loginToLoginResponseModelMapper() {
        return new ModelMapper();
    }

    @Bean (name = "userRequestToLoginModelMapper")
    public ModelMapper userRequestToLoginModelMapper() {
        return new ModelMapper();
    }

    @Bean (name = "userRequestToUserModelMapper")
    public ModelMapper userRequestToUserModelMapper() {
        return new ModelMapper();
    }

    @Bean (name = "userToUserResponseMapper")
    public ModelMapper userToUserResponseMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<User, UserResponse> typeMap = modelMapper.createTypeMap(User.class, UserResponse.class);
        typeMap.setPropertyCondition(Conditions.isNotNull());
        Converter<User, Long> referrerConverter = mappingContext -> mappingContext.getSource().getId();
        typeMap.addMappings(mapper -> mapper.using(referrerConverter).map(User::getReferredBy, UserResponse::setReferredBy));
        return modelMapper;
    }

    /*@Bean (name = "userToLoginResponseMapper")
    public ModelMapper userToLoginResponseMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<User, LoginResponse> typeMap = modelMapper.createTypeMap(User.class, LoginResponse.class);
        typeMap.setPropertyCondition(Conditions.isNotNull());
        Converter<User, Long> referrerConverter = mappingContext -> mappingContext.getSource().getId();
        typeMap.addMappings(mapper -> mapper.using(referrerConverter).map(User::getReferredBy, LoginResponse::setReferredBy));
        return modelMapper;
    }*/
}
