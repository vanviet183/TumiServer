package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.HelpDTO;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Help;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.repository.HelpRepository;
import com.example.tumiweb.services.IHelpService;
import com.example.tumiweb.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HelpServiceImp implements IHelpService {

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IUserService userService;

    @Override
    public Set<Help> getAllHelp(Long page, int size) {
        List<Help> helps;
        if(page != null) {
            Page<Help> helpPage = helpRepository.findAll(PageRequest.of(page.intValue(), size));
            helps = helpPage.getContent();
        }else {
            helps = helpRepository.findAll();
        }

        return new HashSet<>(helps);
    }

    @Override
    public Help findHelpById(Long id) {
        Optional<Help> help = helpRepository.findById(id);
        if(help.isEmpty()) {
            return null;
        }
        return help.get();
    }

    @Override
    public Help getHelpById(Long id) {
        Help help = findHelpById(id);
        if(help == null) {
            throw new NotFoundException("Can not find help by id: " + id);
        }
        return help;
    }

    @Override
    public Help createNewHelp(Long userId, HelpDTO helpDTO) {
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new NotFoundException("Can not find user to create help");
        }
        Help help = modelMapper.map(helpDTO, Help.class);
        help.setUser(user);
        return helpRepository.save(help);
    }

    @Override
    public Help deleteHelpById(Long id) {
        Help help = findHelpById(id);
        if(help == null) {
            throw new NotFoundException("Can not find help with id: " + id);
        }
        helpRepository.delete(help);
        return help;
    }

    @Override
    public Help disableHelp(Long id) {
        Help help = findHelpById(id);
        if(help == null) {
            throw new NotFoundException("Can not find help by id: " + id);
        }
        help.setStatus(false);
        return helpRepository.save(help);
    }
}
