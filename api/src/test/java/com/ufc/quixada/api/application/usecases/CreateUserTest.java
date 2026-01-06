package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUser createUser;

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPassword("pass");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> createUser.execute(user));

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldEncodePasswordCreateProfilesAndSave() {
        User user = new User();
        user.setName("User");
        user.setEmail("new@example.com");
        user.setPassword("plain");

        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        createUser.execute(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("encoded", saved.getPassword());
        assertNotNull(saved.getFreelancerProfile());
        assertNotNull(saved.getContractorProfile());
        assertSame(saved, saved.getFreelancerProfile().getUser());
        assertSame(saved, saved.getContractorProfile().getUser());
    }
}
