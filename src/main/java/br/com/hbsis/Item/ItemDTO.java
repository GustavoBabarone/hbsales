package br.com.hbsis.Item;

public class ItemDTO {

    /* ATRIBUTOS */
    private Long id;
    private Long idPedido;
    private String nomeProduto;
    private Long quantidade;

    /* CONSTRUTORES */
    public ItemDTO() {
    }

    public ItemDTO(Long id, Long idPedido, String nomeProduto, Long quantidade) {
        this.id = id;
        this.idPedido = idPedido;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
    }

    public static ItemDTO of(Item item){
        return new ItemDTO(

                item.getId(),
                item.getPedido().getId(),
                item.getProduto().getNome(),
                item.getQuantidade()
        );
    }

    /* GETTER & SETTER */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }
}
