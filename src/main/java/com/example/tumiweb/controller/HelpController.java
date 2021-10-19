package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dto.HelpDTO;
import com.example.tumiweb.model.Help;
import com.example.tumiweb.services.IHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/helps")
public class HelpController extends BaseController<Help> {

    @Autowired
    private IHelpService helpService;

    @RequestMapping("")
    public ResponseEntity<?> getAllHelp(
            @RequestParam(name = "page", required = false) Long page) {
        return this.resSetSuccess(helpService.getAllHelp(page, Constants.SIZE_OFF_PAGE));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHelpById(@PathVariable("id") Long id) {
        return this.resSuccess(helpService.getHelpById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createNewHelp(
            @PathVariable("userId") Long id,
            @RequestBody HelpDTO helpDTO) {
        return this.resSuccess(helpService.createNewHelp(id, helpDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHelpById(@PathVariable("id") Long id) {
        return this.resSuccess(helpService.deleteHelpById(id));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> disableStatus(@PathVariable("id") Long id) {
        return this.resSuccess(helpService.disableHelp(id));
    }

}
