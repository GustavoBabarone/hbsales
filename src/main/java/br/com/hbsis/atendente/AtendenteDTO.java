package br.com.hbsis.atendente;

public class AtendenteDTO {

    /** ATRIBUTOS */
    private Long id;
    private String nome;
    private String foto;
    private String email;
//    private List<ITEM> itemList;
    private String senha;

    /** CONSTRUTORES */
    public AtendenteDTO() {
    }

    public AtendenteDTO(Long id, String nome, String foto, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.email = email;
        this.senha = senha;
    }

    public static AtendenteDTO of(Atendente atendente) {
        return new AtendenteDTO(

                atendente.getId(),
                atendente.getNome(),
                atendente.getFoto(),
                atendente.getEmail(),

                atendente.getSenha()
        );
    }

//    public AtendenteDTO(Long id, String nome, String foto, String email, List<ITEM> itemList, String senha) {
//        this.id = id;
//        this.nome = nome;
//        this.foto = foto;
//        this.email = email;
//        this.itemList = itemList;
//        this.senha = senha;
//    }

    /** GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public List<ITEM> getItemList() {
        return itemList;
    }

    public void setItemList(List<ITEM> itemList) {
        this.itemList = itemList;
    }*/

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "AtendenteDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", foto='" + foto + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }

}
