package com.codenbugs.sgeaapi.dto.professor;

import com.codenbugs.sgeaapi.enums.AccountStatusType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountStatusDTO {
    private String rejectionReason;
    private AccountStatusType status;
}
