package com.codenbugs.sgeaapi.dto.professor;

import com.codenbugs.sgeaapi.entity.users.AccountStatusType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountStatusDTO {

    private Long professorId;
    private String comment;
    private AccountStatusType status;

}
