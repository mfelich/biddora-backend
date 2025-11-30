package com.example.biddora_backend.user;

import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.user.repo.UserRepo;
import com.example.biddora_backend.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSeviceImplTest {

    @Mock
    UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_withUsername_callsFindByUsername() {

        User user1 = new User();
        Page<User> mockPage = new PageImpl<>(List.of(user1));

        PageRequest expectedPageRequest = PageRequest.of(0,12, Sort.Direction.ASC,"username");

        when(userRepo.findByUsernameContainingIgnoreCase("test", expectedPageRequest)).thenReturn(mockPage);

        Page<UserDto> result = userService.getAllUsers(
                Optional.of(0),
                Optional.empty(),
                Optional.of("test")
        );

        assertEquals(result.getTotalElements(),1);
    }

    @Test
    void getAllUsers_withoutUsername_callsFindAll() {

        User user = new User();

        Page<User> expectedPage = new PageImpl<>(List.of(user));

        PageRequest expectedPageRequest = PageRequest.of(0,12, Sort.Direction.ASC,"username");

        when(userRepo.findAll(expectedPageRequest)).thenReturn(expectedPage);

        Page<UserDto> result = userService.getAllUsers(
                Optional.of(0),
                Optional.empty(),
                Optional.empty()
        );

        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
    }
}
