package com.studyhub.studyhub_backend.global.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "페이지 정보")
public record PageInfo(
        @Schema(description = "현재 페이지 번호", example = "0")
        int pageNumber,

        @Schema(description = "페이지 크기", example = "10")
        int pageSize,

        @Schema(description = "전체 요소 수", example = "35")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "4")
        int totalPages
) {
    public static PageInfo from(Page<?> page) {
        return new PageInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}