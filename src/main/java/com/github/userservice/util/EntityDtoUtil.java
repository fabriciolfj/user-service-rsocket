package com.github.userservice.util;

import com.github.userservice.dto.TransactionRequest;
import com.github.userservice.dto.TransactionResponse;
import com.github.userservice.dto.TransactionStatus;
import com.github.userservice.dto.UserDto;
import com.github.userservice.entities.User;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static UserDto toDto(final User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    public static User toEntity(final UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);

        return user;
    }

    public static TransactionResponse toResponse(final TransactionRequest request, final TransactionStatus status) {
        final TransactionResponse response = new TransactionResponse();
        response.setAmount(response.getAmount());
        response.setType(response.getType());
        response.setUserId(request.getUserId());
        response.setStatus(status);

        return response;
    }
}
