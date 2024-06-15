package com.programming.bookservice.controller;

import com.programming.bookservice.dto.ResponseMessageDto;
import com.programming.bookservice.dto.request.BookRequestDto;
import com.programming.bookservice.dto.response.BookResponseDto;
import com.programming.bookservice.dto.response.CategoryResponseDto;
import com.programming.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/book")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping(value = "/save-book")
    @Operation(summary = "ذخیره کتاب جدید")
    public Mono<Long> saveBook(@RequestBody BookRequestDto requestDto){
        return bookService.saveProduct(requestDto);
    }

    @Operation(summary = "دریافت لیست کتاب ها")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookResponseDto> getAll(){
        return bookService.getAll();
    }

    @Operation(summary = "دریافت کتاب با استفاده از id")
    @GetMapping("/book-by-id")
    public Mono<ResponseMessageDto> getBookById(@RequestParam Mono<Long> id){
        return bookService.getBookById(id);
    }

    @Operation(summary = "دریافت لیست کتاب ها براساس دسته بندی")
    @GetMapping("/books-by-category")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookResponseDto> getBooksByCategory(@RequestParam Mono<Long> categoryId){
        return bookService.getBooksByCategory(categoryId);
    }

    @PostMapping("/upload-and-save-books")
    @Operation(summary = "بارگذاری فایل اکسل و ذخیره کتاب ها")
    public Mono<ResponseMessageDto> upload(@RequestParam("file")MultipartFile file){
        return bookService.upload(file);
    }

    @GetMapping("/get-categories")
    @Operation(summary = "دریافت لیست دسته بندی ها",tags = {"category"})
    public Flux<CategoryResponseDto> getCategories(){
        return bookService.getCategories();
    }





    @PostMapping(value = "/upload-image-book")
    public Mono<ResponseMessageDto> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam Long bookId){
        return bookService.uploadImage(file,bookId);
    }









}
