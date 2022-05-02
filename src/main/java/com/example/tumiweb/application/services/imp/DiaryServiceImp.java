package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.DiaryRepository;
import com.example.tumiweb.application.services.IDiaryService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.utils.DateTimeUtil;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.entity.Diary;
import com.example.tumiweb.domain.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiaryServiceImp implements IDiaryService {
  private final IUserService userService;
  private final DiaryRepository diaryRepository;

  public DiaryServiceImp(IUserService userService, DiaryRepository diaryRepository) {
    this.userService = userService;
    this.diaryRepository = diaryRepository;
  }

  //  @Cacheable(value = "diary", key = "'du'+#userId")
  @Override
  public Set<Diary> findAllByUserId(Long userId) {
    User user = userService.getUserById(userId);
    if (user == null) {
      throw new VsException("Can not find user by id: " + userId);
    }

    return diaryRepository.findAllByUser_Id(userId);
  }

  //  @Cacheable(value = "diary", key = "'day'+#userId")
  @Override
  public List<Diary> findAllByUserIdAndOnDay(Long userId, String day) {
    User user = userService.getUserById(userId);
    if (user == null) {
      throw new VsException("Can not find user by id: " + userId);
    }

//        return diaryRepository.findAllByUser_Id(userId).stream().filter(item -> {
//            return item.getStart().split(" ")[0].replaceAll("/", "-").equals(day);
//        }).collect(Collectors.toSet());
    return diaryRepository.findAllByUser_IdAndDay(userId, day);
  }

  //  @Cacheable(value = "diary", key = "#id")
  @Override
  public Diary findDiaryById(Long id) {
    System.out.println("get by id diary");
    Optional<Diary> diary = diaryRepository.findById(id);
    if (diary.isEmpty()) {
      throw new VsException("Can not find diary by id: " + id);
    }
    return diary.get();
  }

  //  @CacheEvict(value = "diary", allEntries = true)
  @Override
  public Diary createNewDiary(Long idUser) {
    User user = userService.getUserById(idUser);
    if (user == null) {
      throw new VsException("Can not find user by id: " + idUser);
    }
    Diary diary = new Diary();
    diary.setStart(DateTimeUtil.getDateTimeNow());
    diary.setDay(DateTimeUtil.getDateTimeNowDetail());

    Diary newDiary = diaryRepository.save(diary);
    userService.save(user);

    return diaryRepository.save(newDiary);
  }

  //  @CachePut(value = "diary", key = "#id")
  @Override
  public Diary editDiaryById(Long id) {
    clearCache();
    System.out.println("Cache put");
    Diary diary = findDiaryById(id);
    if (diary == null) {
      throw new VsException("Can not find diary by id: " + id);
    }
    diary.setEnd(DateTimeUtil.getDateTimeNowDetail());
    return diaryRepository.save(diary);
  }

  //  @CacheEvict(value = "diary", key = "#id")
  @Override
  public Diary deleteDiaryById(Long id) {
    Diary diary = findDiaryById(id);
    if (diary == null) {
      throw new VsException("Can not find diary by id: " + id);
    }
    diaryRepository.delete(diary);
    return diary;
  }

  //  @Cacheable(value = "diary", key = "'all'")
  @Override
  public List<Diary> findAll() {
    System.out.println("get all diaries");
    return diaryRepository.findAll();
  }

  //lấy những ai k đăng nhập
//  @Cacheable(value = "diary", key = "'day'")
  @Override
  public Set<User> findAllUserByDay(String day) {
    List<Diary> diaries = diaryRepository.findAllByDay(day);

    Set<User> userSet = diaries.stream().map(Diary::getUser).collect(Collectors.toSet());

    List<User> users = userService.getAllUsers(null, 0, false, false);

    Set<User> userDiary = new HashSet<>();
    for (User user : users) {
      if (!isContainUser(userSet, user)) {
        userDiary.add(user);
      }
    }
    return userDiary;
  }

  private boolean isContainUser(Set<User> users, User user) {
    for (User u : users) {
      if (u.getUsername().equals(user.getUsername())) {
        return true;
      }
    }
    return false;
  }

  @CacheEvict("diary")
  public void clearCacheById(String id) {
  }

  @CacheEvict(value = "diary", allEntries = true)
  public void clearCache() {
  }

}
