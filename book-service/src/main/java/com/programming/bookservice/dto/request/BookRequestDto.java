package com.programming.bookservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class BookRequestDto {
        @NotBlank(message = "the name of book can not be empty")
        private String name;
        private String publisherName;
        private String publishDate;
        private String authorName;
        private String description;
        private BigDecimal price;
        private Long categoryId;
}
