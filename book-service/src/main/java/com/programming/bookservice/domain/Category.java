package com.programming.bookservice.domain;

import com.programming.bookservice.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

// can not create table automatically
@Table(name = "book_service_category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Category extends BaseEntity{
    @Id
    @Column("PID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("CATEGORY_NAME")
    private String name;

    @Column("PERSIAN_NAME")
    private String persianName;

    @Lob
    private String description;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = Book.class,mappedBy = "category")
    private List<Book> books;

}
