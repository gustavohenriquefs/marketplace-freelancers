package data.models;

import domain.entities.Address;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table()
public class FreelancerJpaEntity {
    @Id()
    private String id;
    private String nome;
    private String cpf;
    private Address address;
}
