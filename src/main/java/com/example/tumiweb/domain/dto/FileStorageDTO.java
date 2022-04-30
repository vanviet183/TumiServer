package com.example.tumiweb.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileStorageDTO {

  private String name;
  private String path;
  private Long size;
  private String type;

}
