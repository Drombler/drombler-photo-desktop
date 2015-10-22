/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.event.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 *
 * @author Florian
 */
public class FullTimeEventDuration implements EventDuration {

    private static final String MONTH_APPENDIX = "00";
    private static final DateTimeFormatter SINGLE_DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{8}");

    private final LocalDate startDateInclusive;
    private final LocalDate endDateInclusive;

    public FullTimeEventDuration(LocalDate startDateInclusive, LocalDate endDateInclusive) {
        this.startDateInclusive = startDateInclusive;
        this.endDateInclusive = endDateInclusive;
    }

    @Override
    public String getDirName() {
        return isSingleDay() ? getSingleDayDirName() : getPeriodDirName();
    }

    public boolean isSingleDay() {
        return getStartDateInclusive().equals(getEndDateInclusive());
    }

    private String getSingleDayDirName() {
        return SINGLE_DAY_FORMATTER.format(getStartDateInclusive());
    }

    private String getPeriodDirName() {
        return SINGLE_DAY_FORMATTER.format(getStartDateInclusive()) + "-" + SINGLE_DAY_FORMATTER.format(getEndDateInclusive());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.startDateInclusive);
        hash = 53 * hash + Objects.hashCode(this.endDateInclusive);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FullTimeEventDuration)) {
            return false;
        }
        final FullTimeEventDuration other = (FullTimeEventDuration) obj;
        return Objects.equals(this.startDateInclusive, other.startDateInclusive)
                && Objects.equals(this.endDateInclusive, other.endDateInclusive);
    }

    @Override
    public String toString() {
        return "FullTimeEventDuration{" + "startDateInclusive=" + startDateInclusive + ", endDateInclusive=" + endDateInclusive + '}';
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
        if (matches(dirName)) {
            final LocalDate date = SINGLE_DAY_FORMATTER.parse(dirName, LocalDate::from);
            return Optional.of(new FullTimeEventDuration(date, date));
        } else {
            return Optional.empty();
        }
    }

    private static boolean matches(String dirName) {
        return DATE_PATTERN.matcher(dirName).matches() && !dirName.endsWith(MONTH_APPENDIX);
    }

    public static Optional<FullTimeEventDuration> period(String startDateInclusiveString, String endDateInclusiveString) {
        if (matches(startDateInclusiveString) && matches(endDateInclusiveString)) {
            final LocalDate startDateInclusive = SINGLE_DAY_FORMATTER.parse(startDateInclusiveString, LocalDate::from);
            final LocalDate endDateInclusive = SINGLE_DAY_FORMATTER.parse(endDateInclusiveString, LocalDate::from);
            return Optional.of(new FullTimeEventDuration(startDateInclusive, endDateInclusive));
        } else {
            return Optional.empty();
        }
    }
}
