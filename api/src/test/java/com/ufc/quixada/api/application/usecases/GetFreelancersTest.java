package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetFreelancersTest {

    @Mock
    private FreelancerRepository repository;

    @InjectMocks
    private GetFreelancers getFreelancers;

    @Test
    void shouldReturnAllFreelancers() {
        Freelancer f1 = new Freelancer();
        f1.setId(1L);

        when(repository.findAll()).thenReturn(List.of(f1));

        List<Freelancer> result = getFreelancers.execute();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(repository).findAll();
    }
}
