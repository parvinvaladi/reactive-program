package com.programming.bookservice.common;


import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;


import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@EnableR2dbcAuditing
@Data
public class BaseEntity {

    @Version
    private Integer version;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
