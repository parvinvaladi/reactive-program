package com.programming.bookservice.repository;

import com.programming.bookservice.domain.Category;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;

public interface CategoryRepository extends R2dbcRepository<Category,Long> {
}
