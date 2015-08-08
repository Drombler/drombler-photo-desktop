/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.event.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 *
 * @author Florian
 */
public class FullTimeEventDuration implements EventDuration {

    private static final String MONTH_APPENDIX = "00";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{8}");

    private final LocalDate startDateInclusive;
    private final LocalDate endDateInclusive;

    public FullTimeEventDuration(LocalDate startDateInclusive, LocalDate endDateInclusive) {
        this.startDateInclusive = startDateInclusive;
        this.endDateInclusive = endDateInclusive;
    }

    @Override
    public String getDirName() {
        return getStartDateInclusive().equals(getEndDateInclusive()) ? getStartDateDirName() : getStartMonthDirName();
    }

    private String getStartDateDirName() {
        return DATE_FORMATTER.format(getStartDateInclusive());
    }

    private String getStartMonthDirName() {
        return MONTH_FORMATTER.format(getStartDateInclusive()) + MONTH_APPENDIX;
    }

    /**
     * @return the startDateInclusive
     */
    public LocalDate getStartDateInclusive() {
        return startDateInclusive;
    }

    /**
     * @return the endDateInclusive
     */
    public LocalDate getEndDateInclusive() {
        return endDateInclusive;
    }

    public static Optional<FullTimeEventDuration> singleDay(String dirName) {
        if (DATE_PATTERN.matcher(dirName).matches() && ! dirName.endsWith(MONTH_APPENDIX)){
            final LocalDate date = DATE_FORMATTER.parse(dirName, LocalDate::from);
            return Optional.of(new FullTimeEventDuration(date, date));
        } else {
            return Optional.empty();
        }
    }
}
