package com.example.tumiweb.services.imp;

import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Diary;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.repository.DiaryRepository;
import com.example.tumiweb.services.IDiaryService;
import com.example.tumiweb.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiaryServiceImp implements IDiaryService {

    @Autowired
    private IUserService userService;

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private ModelMapper modelMapper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Set<Diary> findAllByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + userId);
        }

        return diaryRepository.findAllByUser_Id(userId);
    }

    @Override
    public List<Diary> findAllByUserIdAndOnDay(Long userId, String day) {
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + userId);
        }

//        return diaryRepository.findAllByUser_Id(userId).stream().filter(item -> {
//            return item.getStart().split(" ")[0].replaceAll("/", "-").equals(day);
//        }).collect(Collectors.toSet());
        return diaryRepository.findAllByUser_IdAndDay(userId, day);
    }

    @Override
    public Diary findDiaryById(Long id) {
        Optional<Diary> diary = diaryRepository.findById(id);
        if(diary.isEmpty()) {
            throw new NotFoundException("Can not find diary by id: " + id);
        }
        return diary.get();
    }

    @Override
    public Diary createNewDiary(Long idUser) {
        User user = userService.getUserById(idUser);
        if(user == null) {
            throw new NotFoundException("Can not find user by id: " + idUser);
        }
        Diary diary = new Diary();
        diary.setStart(simpleDateFormat.format(new Date()));
        diary.setDay(simpleDateFormatDay.format(new Date()));

        Diary newDiary = diaryRepository.save(diary);
        user.addRelationDiary(newDiary);
        userService.save(user);

        return diaryRepository.save(newDiary);
    }

    @Override
    public Diary editDiaryById(Long id) {
        Diary diary = findDiaryById(id);
        if(diary == null) {
            throw new NotFoundException("Can not find diary by id: " + id);
        }
        diary.setEnd(simpleDateFormat.format(new Date()));
        return diaryRepository.save(diary);
    }

    @Override
    public Diary deleteDiaryById(Long id) {
        Diary diary = findDiaryById(id);
        if(diary == null) {
            throw new NotFoundException("Can not find diary by id: " + id);
        }
        diaryRepository.delete(diary);
        return diary;
    }

    @Override
    public List<Diary> findAll() {
        return diaryRepository.findAll();
    }

    //lấy những ai k đăng nhập
    @Override
    public Set<User> findAllUserByDay(String day) {
        List<Diary> diaries = diaryRepository.findAllByDay(day);

        Set<User> userSet = diaries.stream().map(Diary::getUser).collect(Collectors.toSet());

        Set<User> users = userService.getAllUsers(null, 0, false, false);

        Set<User> userDiary = new HashSet<>();
        for(User user : users) {
            if(!userSet.contains(user)) {
                userDiary.add(user);
            }
        }

        return userDiary;
    }
}
