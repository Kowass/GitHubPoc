package br.com.tcc.github_poc.metrics.dto.common;

import java.sql.Date;

public interface DailyDurationProjection {
    Date getDay();
    Double getAvgHours();
}
