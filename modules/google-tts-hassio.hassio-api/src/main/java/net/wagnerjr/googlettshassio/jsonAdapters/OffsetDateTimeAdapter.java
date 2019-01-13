package net.wagnerjr.googlettshassio.jsonAdapters;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class OffsetDateTimeAdapter {
    public static final OffsetDateTimeAdapter ADAPTER = new OffsetDateTimeAdapter();
    private final DateTimeFormatter dateIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @ToJson
    public String toJson(OffsetDateTime date) {
        if (date == null) return null;
        return date.format(dateIso);
    }

    /**
     * Converts a Date in a String representation inside of a JsonObject to a {@link LocalDate}.
     *
     * @param date The String representation of the Date
     * @return The {@link LocalDate} version of the Date
     * @throws DateTimeParseException When we are unable to parse the format of the date.
     */
    @FromJson
    public OffsetDateTime fromJson(String date) throws DateTimeParseException {
        if (date == null) return null;
        if (date.length() == 0) return null;
        try {
            return OffsetDateTime.parse(date, dateIso);
        } catch (DateTimeParseException e) {
            throw e;
        }
    }
}
