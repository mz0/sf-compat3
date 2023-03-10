/*
 * Copyright 2009-2018 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exactpro.sf.comparison.conversion.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.TimeUnit;

import com.exactpro.sf.comparison.conversion.ConversionException;
import com.exactpro.sf.util.DateTimeUtility;

public class LongConverter extends AbstractNumberConverter<Long> {
    private final static long NANOS_IN_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1);
    @Override
    public Long convert(LocalDate value) {
        return convertTemporal(value);
    }

    @Override
    public Long convert(LocalTime value) {
        if(checkNanosecondPrecision(value.getNano())){
          throw new ConversionException("Cannot convert from LocalTime to Long with a given precision, time contains nanoseconds: " + value);
        }
        return convertTemporal(value);
    }

    @Override
    public Long convert(LocalDateTime value) {
        if(checkNanosecondPrecision(value.getNano())){
            throw new ConversionException("Cannot convert from LocalDateTime to Long with a given precision, time contains nanoseconds: " + value);
        }
        return convertTemporal(value);
    }

    private Long convertTemporal(TemporalAccessor accessor) {
        return accessor != null ? DateTimeUtility.toTimestamp(accessor).getTime() : null;
    }

    @Override
    protected Long convertValue(Number value) {
        return value.longValue();
    }

    @Override
    protected Long convertValue(String value) {
        return Long.valueOf(value);
    }

    @Override
    public Class<Long> getTargetClass() {
        return Long.class;
    }

    private boolean checkNanosecondPrecision(long value){
        return (value % NANOS_IN_MILLISECOND != 0);
    }
}
