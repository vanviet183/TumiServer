package com.example.tumiweb.dto;

import lombok.*;

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
