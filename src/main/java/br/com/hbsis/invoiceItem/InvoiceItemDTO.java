package br.com.hbsis.invoiceItem;

public class InvoiceItemDTO {

    private Long amount;
    private String itemName;

    public InvoiceItemDTO() {
    }

    public InvoiceItemDTO(Long amount, String itemName) {
        this.amount = amount;
        this.itemName = itemName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
