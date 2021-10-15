package com.example.tumiweb.services;

import com.example.tumiweb.dto.HelpDTO;
import com.example.tumiweb.model.Help;

import java.util.Set;

public interface IHelpService {
    Set<Help> getAllHelp(Long page, int size);
    Help findHelpById(Long id);
    Help createNewHelp(HelpDTO helpDTO);
    Help deleteHelpById(Long id);
}
