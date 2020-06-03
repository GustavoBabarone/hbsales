package br.com.hbsis.fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "seg_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false, length = 100)
    private String razaoSocial;

    @Column(name = "cnpj", unique = true, nullable = false, length = 14)
    private String cnpj;

    @Column(name = "nome_fantasia", unique = true, nullable = false, length = 100)
    private String nomeFantasia;

    @Column(name = "endereco", nullable = false, length = 100)
    private String endereco;

    @Column(name = "telefone_contato", unique = true, nullable = false, length = 13)
    private String telefoneContato;

    @Column(name = "email_contato", unique = true, nullable = false, length = 50)
    private String emailContato;

    public Fornecedor() {
    }

    public Fornecedor(String razaoSocial, String cnpj, String nomeFantasia, String endereco, String telefoneContato, String emailContato) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.endereco = endereco;
        this.telefoneContato = telefoneContato;
        this.emailContato = emailContato;
    }

    public Fornecedor(Long id, String razaoSocial, String cnpj, String nomeFantasia, String endereco, String telefoneContato, String emailContato) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.endereco = endereco;
        this.telefoneContato = telefoneContato;
        this.emailContato = emailContato;
    }

    public static Fornecedor of(FornecedorDTO fornecedorDTO){
        return new Fornecedor(
                fornecedorDTO.getId(),
                fornecedorDTO.getRazaoSocial(),
                fornecedorDTO.getCnpj(),
                fornecedorDTO.getNomeFantasia(),
                fornecedorDTO.getEndereco(),
                fornecedorDTO.getTelefoneContato(),
                fornecedorDTO.getEmailContato()
        );
    }

    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefoneContato='" + telefoneContato + '\'' +
                ", emailContato='" + emailContato + '\'' +
                '}';
    }
}
