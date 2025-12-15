package domain.entities;

public class Freelancer {
    private String id;
    private String nome;
    private String cpf;
    private Address address;

    public Freelancer() {}

    public Freelancer(String id, String name, String cpf, Address address)  {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
