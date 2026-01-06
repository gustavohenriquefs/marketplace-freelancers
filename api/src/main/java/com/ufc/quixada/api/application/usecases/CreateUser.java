package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Contractor;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CreateUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // O Spring injeta a implementação da Infra

    public CreateUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(User userDomain) {
        if(userRepository.findByEmail(userDomain.getEmail()).isPresent()){
            throw new RuntimeException("Email já existe");
        }

        String encodedPass = passwordEncoder.encode(userDomain.getPassword());
        userDomain.setPassword(encodedPass);
        var freelancerProfile = new Freelancer();
        var contractorProfile = new Contractor();
        freelancerProfile.setUser(userDomain);
        contractorProfile.setUser(userDomain);
        userDomain.setFreelancerProfile(freelancerProfile);
        userDomain.setContractorProfile(contractorProfile);

        userRepository.save(userDomain);
    }
}
