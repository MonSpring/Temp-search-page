package com.example.testsearch.util;

import com.example.testsearch.dto.Pagination;
import org.springframework.ui.Model;

import java.util.Calendar;

public class SpreadFrontUtil {

    public static void spreadFrontCalender(Model model) {

        Calendar mon = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        mon.add(Calendar.MONTH , -1);

        String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());
        String todayDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(today.getTime());

        Pagination pagination = new Pagination(0, 1);

        model.addAttribute("localdate", todayDate);
        model.addAttribute("beforemonth", beforeMonth);
        model.addAttribute("pagination", pagination);
        model.addAttribute("startDatetime", beforeMonth);
        model.addAttribute("endDatetime", todayDate);
    }

    public static void spreadFrontCalender(Model model, String startDatetime, String endDatetime) {

        Calendar mon = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        mon.add(Calendar.MONTH , -1);

        String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());
        String todayDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(today.getTime());

        model.addAttribute("localdate", todayDate);
        model.addAttribute("beforemonth", beforeMonth);
        model.addAttribute("startDatetime", startDatetime);
        model.addAttribute("endDatetime", endDatetime);

    }
}
