package com.programming.bookservice.domain;

import com.programming.bookservice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

// can not create table automatically
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "book_service_book")
@Data
public class Book extends BaseEntity{

    @Id
    @Column("PID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("NAME")
    private String name;

    @Column("PUBLISHER_NAME")
    private String publisherName;

    @Column("PUBLISH_DATE")
    private LocalDate publishDate;

    @Column("AUTHOR_NAME")
    private String authorName;

    @Column("DESCRIPTION")
    private String description;

    @Column("PRICE")
    private BigDecimal price;

    //// can not support nested entity
//    @ManyToOne
//    @JoinColumn(name = "FK_BOOK_CATEGORY")
//    private Category category;

    @Column("BOOK_IMAGE")
    private byte[] image;

    @Column("FK_BOOK_CATEGORY")
    private Long categoryId;

}
