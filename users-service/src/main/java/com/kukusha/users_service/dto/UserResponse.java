package com.kukusha.users_service.dto;

import com.kukusha.users_shared_lib.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserResponse {
    private String username;
    private String email;
    private String name;
    private String surname;

    public UserResponse(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
