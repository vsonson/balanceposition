package com.balpos.app.stat;

public final class StatConstant {
    public final static Integer NL_THRESH = 3; // Threshold in days for "not logged"
    public final static Integer TRAILING_DAYS = 30;
    public final static Double SEC_PER_DAY = 86400.0;
    public final static Integer SLEEP_HOUR_TARGET = 8;
    public final static Double SLEEP_ADJV = 0.25; // adjustment magnitude toward self-reported status
}
