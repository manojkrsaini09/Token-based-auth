package com.metacube.sageclarity.predictable.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class ProductionScheduleData extends BaseEntity<String> implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private ProductionScheduleMaster scheduleMaster;

    @Column
    private String workOrderNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "line_id", referencedColumnName = "id")
    private Line line;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column
    private Long orderQuantity;

    @Column
    private Double machineTime;

    @Column
    private Double setupTime;

    @Column
    private LocalDateTime scheduledStart;

    @Column
    private Boolean isActive=true;

    @Column(length = 500)
    private  String  errors;

    @Column
    private String unit;

    public ProductionScheduleMaster getScheduleMaster() {
        return scheduleMaster;
    }

    public void setScheduleMaster(ProductionScheduleMaster scheduleMaster) {
        this.scheduleMaster = scheduleMaster;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getMachineTime() {
        return machineTime;
    }

    public void setMachineTime(Double machineTime) {
        this.machineTime = machineTime;
    }

    public Double getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(Double setupTime) {
        this.setupTime = setupTime;
    }

    public LocalDateTime getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(LocalDateTime scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
