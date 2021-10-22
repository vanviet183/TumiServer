package com.example.tumiweb.services;

import com.example.tumiweb.dto.HelpDTO;
import com.example.tumiweb.dao.Help;

import java.util.Set;

public interface IHelpService {
    Set<Help> getAllHelp(Long page, int size);
    Help findHelpById(Long id);
    Help getHelpById(Long id);
    Help createNewHelp(Long userId, HelpDTO helpDTO);
    Help deleteHelpById(Long id);
    Help disableHelp(Long id);
}
