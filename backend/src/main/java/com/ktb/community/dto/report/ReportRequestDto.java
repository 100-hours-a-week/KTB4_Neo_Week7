package com.ktb.community.dto.report;

import com.ktb.community.entity.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ReportRequestDto {

    @NotNull(message = "신고 유형을 선택해주세요.")
    private ReportType reportType;

    private String reason;
}
