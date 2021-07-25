package com.tutor.tutorlab.config.mapstruct;

import com.tutor.tutorlab.config.mapstruct.support.EnumerableConverter;
import org.mapstruct.MapperConfig;

import static org.mapstruct.ReportingPolicy.IGNORE;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = IGNORE,
        uses = {EnumerableConverter.class})
public interface MapstructConfig {
}
