package br.com.bassi.h_bank.dto;

public record PaginationDto(Integer page,
                            Integer pageSize,
                            Long totalElements,
                            Integer totalPages) {
}
