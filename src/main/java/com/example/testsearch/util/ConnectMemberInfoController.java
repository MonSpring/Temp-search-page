package com.example.testsearch.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/log")
@RequiredArgsConstructor
public class ConnectMemberInfoController {

    private final LogMemberAddressService logMemberAddressService;

    @GetMapping
    public String logInfoPage(Model model) {

        SpreadFrontUtil.spreadFrontCalender(model);

        return "logInfoPage";
    }

    @PostMapping("/info")
    public String getLogList(Model model, String startDatetime, String endDatetime) {

        SpreadFrontUtil.spreadFrontCalender(model);
        model.addAttribute("logdata", logMemberAddressService.searchAllMemberAddressLog(startDatetime, endDatetime));

        return "logInfoPage";
    }

}
