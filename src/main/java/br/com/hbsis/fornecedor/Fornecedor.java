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

    /** GETTER & SETTER*/
    public Long getId() { return id;}

    public String getRazaoSocial() { return razaoSocial; }

    public String getCnpj() { return cnpj; }

    public String getNomeFantasia() { return nomeFantasia; }

    public String getEndereco() { return endereco; }

    public String getTelefoneContato() { return telefoneContato; }

    public String getEmailContato() { return emailContato; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public void setCnpj(String cnpj) {this.cnpj = cnpj;}

    public void setNomeFantasia(String nomeFantasia) {this.nomeFantasia = nomeFantasia;}

    public void setEndereco(String endereco) {this.endereco = endereco;}

    public void setTelefoneContato(String telefoneContato) {this.telefoneContato = telefoneContato;}

    public void setEmailContato(String emailContato) {this.emailContato = emailContato;}

    @Override
    public String toString(){
        return "Fornecedor{ " +
                "id = " + id +
                ", razao_social = " + razaoSocial + '\'' +
                ", cnpj = " + cnpj + '\'' +
                ", nome_fantasia = " + nomeFantasia + '\'' +
                ", endereco = " + nomeFantasia + '\'' +
                ", telefone_contato = " + telefoneContato + '\'' +
                ", email_contato = " + emailContato + '\'' +
                "}";
    }
}
