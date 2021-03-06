package com.example.tumiweb.adapter.web.base;

import com.example.tumiweb.domain.dto.pagination.PaginateDTO;
import com.example.tumiweb.domain.dto.pagination.PaginationDTO.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class BasePagination<E, R extends JpaRepository<E, ?>> {

  private R repository;

  public BasePagination() {
  }

  public BasePagination(R repository) {
    this.repository = repository;
  }

  public PaginateDTO<E> paginate(Integer page, Integer perPage) {
    if (page == null) {
      page = 0;
    }
    if (perPage == null) {
      perPage = 10;
    }
    Page<E> pageData = repository.findAll(PageRequest.of(page, perPage, Sort.by("createdDate").descending()));

    Pagination pagination = new Pagination(page, perPage, pageData.getTotalPages() - 1, pageData.getTotalElements());
    return new PaginateDTO<>(pageData, pagination);
  }

  public PaginateDTO<E> paginate(Integer page, Integer perPage, Page<E> pageData) {
    Pagination pagination = new Pagination(page, perPage, pageData.getTotalPages() - 1, pageData.getTotalElements());
    return new PaginateDTO<>(pageData, pagination);
  }

}
