package com.example.tumiweb.utils;

import com.example.tumiweb.dto.CourseDTO;
import com.example.tumiweb.dto.GiftDTO;
import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.model.Course;
import com.example.tumiweb.model.Gift;
import com.example.tumiweb.model.User;

public class ConvertObject {
    public static User convertUserDTOToUser(User user, UserDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        return user;
    }

    public static Gift convertGiftDTOToGift(Gift gift, GiftDTO giftDTO) {
        gift.setName(giftDTO.getName());
        gift.setMark(giftDTO.getMark());
        gift.setAvatar(giftDTO.getAvatar());
        return gift;
    }

    public static Course convertCourseDTOToCourse(Course course, CourseDTO courseDTO) {
        course.setSeo(courseDTO.getSeo());
        course.setName(courseDTO.getName());
        course.setPrice(courseDTO.getPrice());
        course.setProcess(courseDTO.getProcess());
        course.setDescription(courseDTO.getDescription());
        return course;
    }
}
