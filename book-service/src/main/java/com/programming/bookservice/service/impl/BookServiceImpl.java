package com.programming.bookservice.service.impl;

import com.programming.bookservice.common.ExcelUtility;
import com.programming.bookservice.domain.Book;
import com.programming.bookservice.domain.Category;
import com.programming.bookservice.dto.ResponseMessageDto;
import com.programming.bookservice.dto.request.BookRequestDto;
import com.programming.bookservice.dto.response.BookResponseDto;
import com.programming.bookservice.dto.response.CategoryResponseDto;
import com.programming.bookservice.exception.ExceptionNotFound;
import com.programming.bookservice.repository.BookRepository;
import com.programming.bookservice.repository.CategoryRepository;
import com.programming.bookservice.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Mono<Long> saveProduct(BookRequestDto request) {
        Mono<BookRequestDto> requestDtoMono = Mono.just(request);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return requestDtoMono.flatMap(requestDto -> {
            try {
                LocalDate date = LocalDate.parse(requestDto.getPublishDate(), formatter);
                return saveBook(requestDto,date);
            } catch (Exception e) {
                return Mono.error(new RuntimeException("Invalid date format", e));
            }
        });
    }

    private Mono<Long> saveBook(BookRequestDto requestDto,LocalDate publishDate){
        Mono<Category> byId = categoryRepository.findById(requestDto.getCategoryId());
        return byId.flatMap(category -> {
            Book book = Book.builder()
                    .name(requestDto.getName())
                    .publisherName(requestDto.getPublisherName())
                    .publishDate(publishDate)
                    .description(requestDto.getDescription())
                    .price(requestDto.getPrice())
                    .categoryId(category.getId())
                    .build();
            return bookRepository.save(book).map(Book::getId);
        }).switchIfEmpty(Mono.error(new RuntimeException("Category not found")));

    }

    @Override
    public Flux<BookResponseDto> getAll() {
        Flux<Book> all = bookRepository.findAll();
        return all.map(book -> BookResponseDto.builder()
                .name(book.getName())
                .price(book.getPrice())
                .description(book.getDescription())
                .categoryId(book.getCategoryId())
                .id(book.getId())
                .build());
    }

    @Override
    public Mono<ResponseMessageDto> getBookById(Mono<Long> id) {
        Mono<Book> byId = bookRepository.findById(id);
        return byId.map(book -> ResponseMessageDto.builder()
                        .data(book)
                        .status(HttpStatus.OK)
                        .message(HttpStatus.OK.name())
                        .build())
                .defaultIfEmpty(ResponseMessageDto.builder()
                        .message(HttpStatus.NOT_FOUND.name())
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @Override
    public Flux<BookResponseDto> getBooksByCategory(Mono<Long> categoryId) {
        Mono<Category> categoryById = categoryRepository.findById(categoryId)
                .switchIfEmpty(Mono.error(new ExceptionNotFound("Category not found")));
        Flux<Book> byCategoryId = bookRepository.findByCategoryId(categoryId);
        return byCategoryId.map(book -> BookResponseDto.builder().id(book.getId())
                .categoryName(categoryById.block().getName())
                .price(book.getPrice())
                .categoryId(book.getCategoryId())
                .description(book.getDescription())
                .build());
    }

    @Override
    public Mono<ResponseMessageDto> upload(MultipartFile file) {
        log.info("upload started");
        if (!ExcelUtility.hasExcelFormat(file)){
            String message = "the format of file should be xlsx";
            return Mono.just(ResponseMessageDto.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(message)
                    .message(HttpStatus.BAD_REQUEST.name()).build());
        }

        return Mono.fromCallable(file::getInputStream)
                .flatMapMany(this::processInputStream)
                .collectList()
                .map(savedBook -> ResponseMessageDto.builder()
                        .message(HttpStatus.OK.name())
                        .data(savedBook)
                        .status(HttpStatus.OK)
                        .build());

    }

    private Flux<Book> processInputStream(InputStream inputStream){
        List<Book> bookList = ExcelUtility.excelToBookList(inputStream);
        bookList.forEach(book -> {
           categoryRepository.findById(book.getCategoryId()).subscribe(category -> book.setCategoryId(category.getId()));
           bookRepository.save(book);
        });
        return Flux.fromIterable(bookList);
    }

    @Override
    @Cacheable(value = "cache")
    public Flux<CategoryResponseDto> getCategories() {
        Flux<Category> categories = categoryRepository.findAll();
        return categories.map(this::toDto);
    }

    @Override
    public Mono<ResponseMessageDto> uploadImage(MultipartFile file, Long bookId) {
        return Mono.fromCallable(file::getBytes)
                .flatMap(byteImage -> bookRepository.findById(bookId)
                        .flatMap(book -> {
                            book.setImage(byteImage);
                            return bookRepository.save(book);
                        })
                        .map(savedBook -> ResponseMessageDto.builder()
                                .status(HttpStatus.OK)
                                .message(HttpStatus.OK.name())
                                .data(ResponseMessageDto.builder()
                                        .status(HttpStatus.OK)
                                        .message(HttpStatus.OK.name())
                                        .build())
                                .build())
                        .switchIfEmpty(Mono.error(new ExceptionNotFound("book not found")))
                ).onErrorResume(IOException.class, e -> Mono.error(new RuntimeException("Failed to read image file", e)));

    }

    private CategoryResponseDto toDto(Category category){
        return CategoryResponseDto.builder().id(category.getId())
                .name(category.getName())
                .persianName(category.getPersianName())
                .build();
    }
}
