package com.onebook.frontapi.dto.point.request;

import java.math.BigDecimal;

public record FindPointRequest(Long pointId, BigDecimal pointCurrent) {
}