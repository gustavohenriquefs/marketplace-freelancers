package com.ufc.quixada.api;

import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import com.ufc.quixada.api.domain.usecases.GetFreelancers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public GetFreelancers getFreelancers(FreelancerRepository repo) {
        return new GetFreelancers(repo);
    }
}
