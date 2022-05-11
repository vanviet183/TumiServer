package com.example.tumiweb.application.input.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetAllDataInput {

  private Long page;
  private Boolean activeFlag;
  private Boolean both;
  private int sizeOfPage;

}
