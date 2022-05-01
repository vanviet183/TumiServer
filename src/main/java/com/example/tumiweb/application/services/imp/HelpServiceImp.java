package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.HelpRepository;
import com.example.tumiweb.application.mapper.HelpMapper;
import com.example.tumiweb.application.services.IHelpService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.HelpDTO;
import com.example.tumiweb.domain.entity.Help;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
  private HelpRepository helpRepository;
  @Autowired
  private IUserService userService;

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
      return null;
    }
    return help.get();
  }

  //  @Cacheable(value = "help", key = "#id")
  @Override
  public Help getHelpById(Long id) {
    Help help = findHelpById(id);
    if (help == null) {
      throw new VsException("Can not find help by id: " + id);
    }
    return help;
  }

  //  @CacheEvict(value = "help", allEntries = true)
  @Override
  public Help createNewHelp(Long userId, HelpDTO helpDTO) {
    User user = userService.getUserById(userId);
    if (user == null) {
      throw new VsException("Can not find user to create help");
    }
    Help help = helpMapper.toHelp(helpDTO);
    help.setUser(user);
    return helpRepository.save(help);
  }

  //  @CacheEvict(value = "help", allEntries = true)
  @Override
  public Help deleteHelpById(Long id) {
    Help help = findHelpById(id);
    if (help == null) {
      throw new VsException("Can not find help with id: " + id);
    }
    helpRepository.delete(help);
    return help;
  }

  //  @CacheEvict(value = "help", allEntries = true)
  @Override
  public Help disableHelp(Long id) {
    Help help = findHelpById(id);
    if (help == null) {
      throw new VsException("Can not find help by id: " + id);
    }
    help.setDeleteFlag(true);
    return helpRepository.save(help);
  }

}
