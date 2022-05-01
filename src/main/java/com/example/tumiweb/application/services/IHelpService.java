package com.example.tumiweb.application.services;

import com.example.tumiweb.domain.dto.HelpDTO;
import com.example.tumiweb.domain.entity.Help;

import java.util.Set;

public interface IHelpService {

  Set<Help> getAllHelp(Long page, int size);

  Help findHelpById(Long id);

  Help createNewHelp(Long userId, HelpDTO helpDTO);

  Help deleteHelpById(Long id);

  Help disableHelp(Long id);

}
