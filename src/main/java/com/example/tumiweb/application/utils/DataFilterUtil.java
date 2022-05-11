package com.example.tumiweb.application.utils;

import com.example.tumiweb.application.input.common.GetAllDataInput;
import com.example.tumiweb.application.output.common.GetAllDataOutput;
import com.example.tumiweb.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataFilterUtil<T> {

//  public List<T> filter(List<T> input, GetAllDataInput getAllDataInput) {
//    List<T> output = new ArrayList<>();
//    if (getAllDataInput.getPage() != null) {
//      Page<T> page = Page
//    } else {
//    }
//
//    if (getAllDataInput.getBoth() != null && getAllDataInput.getBoth()) {
//      return new GetAllDataOutput<>(users);
//    }
//
//    if (getAllDataInput.getActiveFlag() != null) {
//      users =
//          users.parallelStream().filter(item -> item.getActiveFlag().equals(getAllDataInput.getActiveFlag())).collect(Collectors.toList());
//    }
//    return new GetAllDataOutput<T>(users);
//  }

}
