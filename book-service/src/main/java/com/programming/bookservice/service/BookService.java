package com.programming.bookservice.service;

import com.programming.bookservice.dto.ResponseMessageDto;
import com.programming.bookservice.dto.request.BookRequestDto;
import com.programming.bookservice.dto.response.BookResponseDto;
import com.programming.bookservice.dto.response.CategoryResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookService {

//    SaveBookResponseDto saveProduct(BookRequestDto requestDto);
    Mono<Long> saveProduct(BookRequestDto requestDto);
    Flux<BookResponseDto> getAll();
    Flux<BookResponseDto> getBooksByCategory(Mono<Long> categoryId);
    Mono<ResponseMessageDto> upload(MultipartFile file);
    Flux<CategoryResponseDto> getCategories();

    Mono<ResponseMessageDto> getBookById(Mono<Long> id);

    Mono<ResponseMessageDto> uploadImage(MultipartFile file,Long bookId);
}
