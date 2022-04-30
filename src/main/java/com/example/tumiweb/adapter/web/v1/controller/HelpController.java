package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.EmailConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IHelpService;
import com.example.tumiweb.domain.dto.HelpDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class HelpController {
  private final IHelpService helpService;

  public HelpController(IHelpService helpService) {
    this.helpService = helpService;
  }

  @RequestMapping(UrlConstant.Help.DATA_HELP)
  public ResponseEntity<?> getAllHelp(@RequestParam(name = "page", required = false) Long page) {
    return VsResponseUtil.ok(helpService.getAllHelp(page, EmailConstant.SIZE_OFF_PAGE));
  }

  @GetMapping(UrlConstant.Help.DATA_HELP_ID)
  public ResponseEntity<?> getHelpById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(helpService.getHelpById(id));
  }

  @PostMapping(UrlConstant.Help.DATA_HELP_USER_ID)
  public ResponseEntity<?> createNewHelp(@PathVariable("userId") Long id, @RequestBody HelpDTO helpDTO) {
    return VsResponseUtil.ok(helpService.createNewHelp(id, helpDTO));
  }

  @DeleteMapping(UrlConstant.Help.DATA_HELP_ID)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> deleteHelpById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(helpService.deleteHelpById(id));
  }

  @PostMapping(UrlConstant.Help.DATA_HELP_ID_STATUS)
  public ResponseEntity<?> disableStatus(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(helpService.disableHelp(id));
  }

}
