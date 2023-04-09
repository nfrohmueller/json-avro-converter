package tech.allegro.schema.json2avro.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.allegro.schema.json2avro.converter.util.DateTimeUtils.getEpochDay;
import static tech.allegro.schema.json2avro.converter.util.DateTimeUtils.getEpochMicros;
import static tech.allegro.schema.json2avro.converter.util.DateTimeUtils.getMicroSeconds;

public class DateTimeUtilsTest {

    private static class JsonDateTimeSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(1537012800000000L, "2018-09-15 12:00:00"),
                    Arguments.of(1537012800006000L, "2018-09-15 12:00:00.006542"),
                    Arguments.of(1537012800006000L, "2018-09-15 12:00:00.006542123"),
                    Arguments.of(1537012800000000L, "2018/09/15 12:00:00"),
                    Arguments.of(1537012800000000L, "2018.09.15 12:00:00"),
                    Arguments.of(1531656000000000L, "2018 Jul 15 12:00:00"),
                    Arguments.of(1531627200000000L, "2018 Jul 15 12:00:00 GMT+08:00"),
                    Arguments.of(1531630800000000L, "2018 Jul 15 12:00:00GMT+07"),
                    Arguments.of(1609462861000000L, "2021-1-1 01:01:01"),
                    Arguments.of(1609462861000000L, "2021.1.1 01:01:01"),
                    Arguments.of(1609462861000000L, "2021/1/1 01:01:01"),
                    Arguments.of(1609459261000000L, "2021-1-1 01:01:01 +01"),
                    Arguments.of(1609459261000000L, "2021-01-01T01:01:01+01:00"),
                    Arguments.of(1609459261546000L, "2021-01-01T01:01:01.546+01:00"),
                    Arguments.of(1609462861000000L, "2021-01-01 01:01:01"),
                    Arguments.of(1609462861000000L, "2021-01-01 01:01:01 +0000"),
                    Arguments.of(1609462861000000L, "2021/01/01 01:01:01 +0000"),
                    Arguments.of(1609462861000000L, "2021-01-01T01:01:01Z"),
                    Arguments.of(1609466461000000L, "2021-01-01T01:01:01-01:00"),
                    Arguments.of(1609459261000000L, "2021-01-01T01:01:01+01:00"),
                    Arguments.of(1609462861000000L, "2021-01-01 01:01:01 UTC"),
                    Arguments.of(1609491661000000L, "2021-01-01T01:01:01 PST"),
                    Arguments.of(1609462861000000L, "2021-01-01T01:01:01 +0000"),
                    Arguments.of(1609462861000000L, "2021-01-01T01:01:01+0000"),
                    Arguments.of(1609462861000000L, "2021-01-01T01:01:01UTC"),
                    Arguments.of(1609459261000000L, "2021-01-01T01:01:01+01"),
                    Arguments.of(-125941863974322000L, "2022-01-23T01:23:45.678-11:30 BC"),
                    Arguments.of(1642942425678000L, "2022-01-23T01:23:45.678-11:30"));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(JsonDateTimeSource.class)
    public void testDateTimeConversion(Long expected, String dateTime) {
        assertEquals(expected, getEpochMicros(dateTime));
    }

    private static class JsonDateSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(18628, "2021-1-1"),
                    Arguments.of(18628, "2021-01-01"),
                    Arguments.of(18629, "2021/01/02"),
                    Arguments.of(18630, "2021.01.03"),
                    Arguments.of(18631, "2021 Jan 04"),
                    Arguments.of(-1457318, "2021-1-1 BC")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(JsonDateSource.class)
    public void testDateConversion(Integer expected, String jsonDate) {
        assertEquals(expected, getEpochDay(jsonDate));
    }

    private static class JsonTimeSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(3661000000L, "01:01:01"),
                    Arguments.of(3660000000L, "01:01"),
                    Arguments.of(44581541000L, "12:23:01.541"),
                    Arguments.of(44581541214L, "12:23:01.541214"),
                    Arguments.of(44581541214L, "12:23:01.541214112")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(JsonTimeSource.class)
    public void testTimeConversion(Long expected, String jsonTime) {
        assertEquals(expected, getMicroSeconds(jsonTime));
    }

    @Test
    public void cleanLineBreaksTest() {
        assertEquals(1585612800000000L, getEpochMicros("2020-03-\n31T00:00:00Z\r"));
        assertEquals(18628, getEpochDay("2021-\n1-1\r"));
    }
}
