package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.exceptions.UserAlreadyExistsException;
import com.ufc.quixada.api.domain.entities.Contractor;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.domain.repositories.ContractorRepository;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import com.ufc.quixada.api.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // O Spring injeta a implementação da Infra
    private final FreelancerRepository freelancerRepository;
    private final ContractorRepository contractorRepository;

    public CreateUser(
            UserRepository userRepository,
            FreelancerRepository freelancerRepository,
            ContractorRepository contractorRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.freelancerRepository = freelancerRepository;
        this.contractorRepository = contractorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(User userDomain) {
        // Regras de negócio (ex: validar email)
        if(userRepository.findByEmail(userDomain.getEmail()).isPresent()){
            throw new UserAlreadyExistsException(userDomain.getEmail());
        }

        // Criptografia (Regra de segurança aplicada antes de salvar)
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
