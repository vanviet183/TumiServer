package com.example.tumiweb.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseDTO {

  private String name;
  private Long price;
  private String description;
  private Long process;
  private String seo;

}
