package com.example.tumiweb.utils;

import com.example.tumiweb.dto.*;
import com.example.tumiweb.model.*;

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

    public static Question convertQuestionDTOToQuestion(Question question, QuestionDTO questionDTO) {
        question.setTitle(questionDTO.getTitle());
        question.setSeo(questionDTO.getSeo());
        question.setAvatar(questionDTO.getAvatar());
        return question;
    }

    public static Answer convertAnswerDTOToAnswer(Answer answer, AnswerDTO answerDTO) {
        answer.setTitle(answerDTO.getTitle());
        answer.setImage(answerDTO.getImage());
        answer.setIsTrue(answerDTO.getIsTrue());
        return answer;
    }
}
