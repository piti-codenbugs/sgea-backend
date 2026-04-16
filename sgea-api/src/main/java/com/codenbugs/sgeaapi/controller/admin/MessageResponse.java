package com.codenbugs.sgeaapi.controller.admin;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MessageResponse {
    private String message;
    private Long id;
}