package com.koreait.scheduleapp;

import lombok.Data;

@Data
public class Schedule {
    private int member_id;
    private int schedule_id;
    private int yy;
    private int mm;
    private int dd;
    private String msg;
}
