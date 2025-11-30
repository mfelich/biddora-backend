package com.example.biddora_backend.user;

import com.example.biddora_backend.common.exception.UserAccessDeniedException;
import com.example.biddora_backend.common.util.EntityFetcher;
import com.example.biddora_backend.user.dto.EditUserDto;
import com.example.biddora_backend.user.dto.UserDto;
import com.example.biddora_backend.user.entity.User;
import com.example.biddora_backend.user.enums.Role;
import com.example.biddora_backend.user.mapper.UserMapper;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSeviceImplTest {

    @Mock
    UserRepo userRepo;

    @Mock
    UserMapper userMapper;

    @Mock
    EntityFetcher entityFetcher;

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

        assertEquals(1,result.getTotalElements());
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

    @Test
    void editUser_whenUserIsOwnerAndNotAdmin_editAndReturnUserDto() {

        Long ownerId = 1L;

        User owner = new User();
        owner.setId(ownerId);
        owner.setRole(Role.USER);
        owner.setFirstName("oldName");
        owner.setLastName("lastName");

        EditUserDto editUserDto =
                new EditUserDto("editedName", "editedLastName", Role.ADMIN);

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(entityFetcher.getUserById(ownerId)).thenReturn(owner);
        when(userRepo.save(owner)).thenReturn(owner);
        when(userMapper.mapToDto(owner)).thenReturn(new UserDto(ownerId,null,"editedName","editedLastName",Role.USER,null,null));

        UserDto result = userService.editUser(ownerId, editUserDto);

        assertEquals("editedName", owner.getFirstName());
        assertEquals("editedLastName", owner.getLastName());
        assertEquals(Role.USER, owner.getRole());

        assertEquals("editedName", result.getFirstName());
        assertEquals("editedLastName", result.getLastName());
        assertEquals(Role.USER, result.getRole());

        verify(userRepo).save(owner);
    }

    @Test
    void editUser_whenUserIsAdmin_editAndReturnUserDto() {

        Long targetId = 1L;
        Long adminId = 10L;

        User target = new User();
        target.setId(targetId);
        target.setFirstName("oldFirstName");
        target.setLastName("oldLastName");
        target.setRole(Role.USER);

        User admin = new User();
        admin.setId(adminId);
        admin.setRole(Role.ADMIN);

        EditUserDto editUserDto =
                new EditUserDto("editedFirstName", "editedLastName", Role.ADMIN);

        when(entityFetcher.getCurrentUser()).thenReturn(admin);
        when(entityFetcher.getUserById(targetId)).thenReturn(target);
        when(userRepo.save(target)).thenReturn(target);
        when(userMapper.mapToDto(target)).thenReturn(new UserDto(targetId,null,"editedFirstName","editedLastName",Role.ADMIN,null,null));

        UserDto result = userService.editUser(targetId, editUserDto);

        verify(userRepo).save(target);

        assertEquals("editedFirstName", target.getFirstName());
        assertEquals("editedLastName", target.getLastName());
        assertEquals(Role.ADMIN, target.getRole());

        assertEquals("editedFirstName", result.getFirstName());
        assertEquals("editedLastName", result.getLastName());
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void editUser_whenUserIsAttacker_throwsUserAccessDeniedException() {

        Long attackerId = 99L;
        Long targetId = 1L;

        User attacker = new User();
        attacker.setId(attackerId);
        attacker.setRole(Role.USER);

        User target = new User();
        target.setId(targetId);
        target.setFirstName("FirstName");
        target.setLastName("LastName");
        target.setRole(Role.USER);

        EditUserDto editUserDto = new EditUserDto("test","test",Role.ADMIN);

        when(entityFetcher.getCurrentUser()).thenReturn(attacker);
        when(entityFetcher.getUserById(targetId)).thenReturn(target);

        assertThatThrownBy(() -> userService.editUser(targetId, editUserDto))
                .isInstanceOf(UserAccessDeniedException.class);

        verify(userRepo, never()).save(any());

        assertEquals("FirstName", target.getFirstName());
        assertEquals("LastName", target.getLastName());
        assertEquals(Role.USER, target.getRole());
    }

    @Test
    void deleteUser_whenUserIsOwner_deletesAndReturnsMessage() {

        Long userId = 1L;

        User owner = new User();
        owner.setId(userId);
        owner.setRole(Role.USER);

        when(entityFetcher.getCurrentUser()).thenReturn(owner);
        when(entityFetcher.getUserById(userId)).thenReturn(owner);

        String result = userService.deleteUser(userId);

        verify(userRepo).delete(owner);
        assertEquals("User deleted successfully.",result);
    }

    @Test
    void deleteUser_whenAdminDeletesAnotherUser_success() {

        Long adminId = 10L;
        Long targetId = 1L;

        User admin = new User();
        admin.setId(adminId);
        admin.setRole(Role.ADMIN);

        User target = new User();
        target.setId(targetId);
        target.setRole(Role.USER);

        when(entityFetcher.getCurrentUser()).thenReturn(admin);
        when(entityFetcher.getUserById(targetId)).thenReturn(target);

        String result = userService.deleteUser(targetId);

        verify(userRepo).delete(target);
        assertEquals("User deleted successfully.", result);
    }

    @Test
    void deleteUser_whenAttackerTriesToDeleteAnotherUser_throwsUserAccessDeniedException() {

        Long attackerId = 1L;
        Long targetId = 99L;

        User attacker = new User();
        attacker.setId(attackerId);
        attacker.setRole(Role.USER);

        User target = new User();
        target.setId(targetId);

        when(entityFetcher.getCurrentUser()).thenReturn(attacker);

        verify(userRepo, never()).delete(any());
        assertThatThrownBy(() -> userService.deleteUser(targetId))
                .isInstanceOf(UserAccessDeniedException.class);
    }

}
