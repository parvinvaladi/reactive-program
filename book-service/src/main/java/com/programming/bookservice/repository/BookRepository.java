package com.programming.bookservice.repository;

import com.programming.bookservice.domain.Book;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
@Repository
public interface BookRepository extends R2dbcRepository<Book,Long> {
    Flux<Book> findByCategoryId(Long categoryId);
    default Flux<Book> findByCategoryId(Mono<Long> categoryIdMono){
        return categoryIdMono.flatMapMany(this::findByCategoryId);
    }
}
