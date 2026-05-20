package br.com.tcc.github_poc.metrics.dto.common;

import java.sql.Date;

public interface DailyCountProjection {
    Date getDay();
    Long getCnt();
}
