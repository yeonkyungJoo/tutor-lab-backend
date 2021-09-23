package com.tutor.tutorlab.modules.subject.controller.response;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

// TODO - CHECK
@RequiredArgsConstructor(staticName = "of")
@Value
public class ParentResponse {

    List<String> parents;
}
