package br.com.hbsis.invoice;

import br.com.hbsis.invoiceItem.InvoiceItemDTO;

import java.util.Set;

public class InvoiceDTO {

    private String cnpjFornecedor;
    private String employeeUuid;
    private Set<InvoiceItemDTO> invoiceItemDTOSet;
    private Double totalValue;

    public InvoiceDTO() {
    }

    public InvoiceDTO(String cnpjFornecedor, String employeeUuid, Set<InvoiceItemDTO> invoiceItemDTOSet, Double totalValue) {
        this.cnpjFornecedor = cnpjFornecedor;
        this.employeeUuid = employeeUuid;
        this.invoiceItemDTOSet = invoiceItemDTOSet;
        this.totalValue = totalValue;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public Set<InvoiceItemDTO> getInvoiceItemDTOSet() {
        return invoiceItemDTOSet;
    }

    public void setInvoiceItemDTOSet(Set<InvoiceItemDTO> invoiceItemDTOSet) {
        this.invoiceItemDTOSet = invoiceItemDTOSet;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
}
