package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.HelpRepository;
import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.application.mapper.HelpMapper;
import com.example.tumiweb.application.services.IHelpService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.HelpDTO;
import com.example.tumiweb.domain.entity.Help;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HelpServiceImp implements IHelpService {
  private final HelpMapper helpMapper = Mappers.getMapper(HelpMapper.class);
  private final HelpRepository helpRepository;
  private final UserRepository userRepository;

  public HelpServiceImp(HelpRepository helpRepository, UserRepository userRepository) {
    this.helpRepository = helpRepository;
    this.userRepository = userRepository;
  }

  //  @Cacheable(value = "help", key = "'all'")
  @Override
  public Set<Help> getAllHelp(Long page, int size) {
    List<Help> helps;
    if (page != null) {
      Page<Help> helpPage = helpRepository.findAll(PageRequest.of(page.intValue(), size));
      helps = helpPage.getContent();
    } else {
      helps = helpRepository.findAll();
    }

    return new HashSet<>(helps);
  }

  @Override
  public Help findHelpById(Long id) {
    Optional<Help> help = helpRepository.findById(id);
    if (help.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "help", id));
    }
    if (help.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
    }
    return help.get();
  }

  //  @CacheEvict(value = "help", allEntries = true)
  @Override
  public Help createNewHelp(Long userId, HelpDTO helpDTO) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "user", userId));
    }
    if (user.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Help.USER_WAS_DELETE, userId));
    }
    Help help = helpMapper.toHelp(helpDTO);
    help.setUser(user.get());
    return helpRepository.save(help);
  }

  //  @CacheEvict(value = "help", allEntries = true)
  @Override
  public Help deleteHelpById(Long id) {
    Help help = findHelpById(id);

    help.setDeleteFlag(true);

    return helpRepository.save(help);
  }

  //  @CacheEvict(value = "help", allEntries = true)
  @Override
  public Help disableHelp(Long id) {
    Help help = findHelpById(id);

    help.setActiveFlag(false);

    return helpRepository.save(help);
  }

}
