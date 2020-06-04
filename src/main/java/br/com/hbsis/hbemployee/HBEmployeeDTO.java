package br.com.hbsis.hbemployee;

public class HBEmployeeDTO {

    private String employeeUuid;
    private String employeeName;

    public HBEmployeeDTO() {
    }

    public HBEmployeeDTO(String employeeUuid, String employeeName) {
        this.employeeUuid = employeeUuid;
        this.employeeName = employeeName;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
