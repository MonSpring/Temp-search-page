package com.example.testsearch.util;

import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Calendar;

public class SpreadFrontUtil {

    public static void spreadFrontCalender(Model model) {

        LocalDate localDate = LocalDate.now();
        String stringlocalDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

        Calendar mon = Calendar.getInstance();
        mon.add(Calendar.MONTH , -1);
        String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());

        model.addAttribute("localdate", stringlocalDate);
        model.addAttribute("beforemonth", beforeMonth);

    }
}
