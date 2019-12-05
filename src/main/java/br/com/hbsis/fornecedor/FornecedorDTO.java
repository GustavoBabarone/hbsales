package br.com.hbsis.fornecedor;

import org.bouncycastle.asn1.x500.style.RFC4519Style;

/**
 * Classe para tráfego das informações do usuário
 */
public class FornecedorDTO{

    /* ATRIBUTOS */
    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String nomeFantasia;
    private String endereco;
    private String telefoneContato;
    private String emailContato;

    /* CONSTRUTORES */
    public FornecedorDTO(){
    }

    public FornecedorDTO(Long id, String razaoSocial, String cnpj, String nomeFantasia,
                         String endereco, String telefoneContato, String emailContato){
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.endereco = endereco;
        this.telefoneContato = telefoneContato;
        this.emailContato = emailContato;
    }

    public static FornecedorDTO of(Fornecedor fornecedor){
        return new FornecedorDTO(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getNomeFantasia(),
                fornecedor.getEndereco(),
                fornecedor.getTelefoneContato(),
                fornecedor.getEmailContato()
        );
    }

    /* GETTER */
    public Long getId() { return id; }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    /* SETTER */
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    @Override
    public  String toString() {
        return "FornecedorDTO{" +
                "id = " + id +
                ", razao_social = " + razaoSocial + '\'' +
                ", cnpj = " + cnpj + '\'' +
                ", nome_fantasia = " + nomeFantasia + '\'' +
                ", endereco = " + endereco + '\'' +
                ", telefone_contato = " + telefoneContato + '\'' +
                ", email_contato = " + emailContato + '\'' +
                '}';
    }
}
