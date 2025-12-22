package presentation.mappers;

import domain.entities.Freelancer;
import org.springframework.stereotype.Component;
import presentation.dtos.FreelancerResponseDTO;

@Component
public class FreelancerRestMapper {
    public FreelancerResponseDTO toDTO(Freelancer domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        return new FreelancerResponseDTO(
                domainEntity.getId(),
                domainEntity.getNome(),
                domainEntity.getEmail(),
                domainEntity.getSpecialty()
        );
    }
}
