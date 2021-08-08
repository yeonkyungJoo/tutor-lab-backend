package com.tutor.tutorlab.modules.subject;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Value
public class ParentResponse {
    private final List<String> parents;
}
