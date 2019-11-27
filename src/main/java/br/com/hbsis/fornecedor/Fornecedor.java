package br.com.hbsis.fornecedor;

import javax.persistence.*;

/**
 * CLASSE RESPONSÁVEL PELO MAPEAMENTO DA ENTIDADE DO BANCO DE DADOS
 */
@Entity
@Table(name = "seg_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razaoSocial", nullable = false, length = 100)
    private String razaoSocial;
    @Column(name = "cnpj", unique = true, nullable = false, length = 18)
    private String cnpj;
    @Column(name = "nomeFantasia", unique = true, nullable = false, length = 50)
    private String nomeFantasia;
    @Column(name = "endereco", nullable = false, length = 70)
    private String endereco;
    @Column(name = "telefoneContato", unique = true, nullable = false, length = 16)
    private String telefoneContato;
    @Column(name = "emailContato", unique = true, nullable = false, length = 60)
    private String emailContato;

    /* GETTER */
    public Long getId() { return id;}

    public String getRazaoSocial() { return razaoSocial; }

    public String getCnpj() { return cnpj; }

    public String getNomeFantasia() { return nomeFantasia; }

    public String getEndereco() { return endereco; }

    public String getTelefoneContato() { return telefoneContato; }

    public String getEmailContato() { return emailContato; }

    /* SETTER */
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
                ", Razão Social = " + razaoSocial + '\'' +
                ", CNPJ = " + cnpj + '\'' +
                ", Nome Fantasia = " + nomeFantasia + '\'' +
                ", Endereco = " + nomeFantasia + '\'' +
                ", Telefone de Contato = " + telefoneContato + '\'' +
                ", E-mail de Contato = " + emailContato + '\'' +
                "}";
    }
}
