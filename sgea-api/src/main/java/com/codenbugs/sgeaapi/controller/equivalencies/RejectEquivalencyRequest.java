package com.codenbugs.sgeaapi.controller.equivalencies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RejectEquivalencyRequest {
    private String comment;
}
