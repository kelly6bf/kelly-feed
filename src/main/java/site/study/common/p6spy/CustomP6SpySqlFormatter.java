package site.study.common.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class CustomP6SpySqlFormatter implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(
        int connectionId, String now, long elapsed,
        String category, String prepared, String sql, String url
    ) {
        // ✅ Filter에서 설정된 traceId 가져오기
        String traceId = MDC.get("traceId");
        if (traceId == null) traceId = "NO-TRACE";

        // ✅ SQL이 없더라도 commit/rollback 등은 로그 남기기
        if (sql == null || sql.trim().isEmpty()) {
            return String.format("[traceId:%s] | [%s] | %d ms |",
                traceId, category, elapsed);
        }

        // ✅ SQL 구문이 있는 경우만 포맷팅 적용
        String formattedSql = formatSql(category, sql);

        return String.format("[traceId:%s] | [%s] | %d ms | %s",
            traceId, category, elapsed, formattedSql);
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().isEmpty()) return sql;

        if (Category.STATEMENT.getName().equals(category)) {
            String trimmedSQL = sql.trim().toLowerCase(Locale.ROOT);
            if (trimmedSQL.startsWith("create") || trimmedSQL.startsWith("alter") || trimmedSQL.startsWith("comment")) {
                return FormatStyle.DDL.getFormatter().format(sql);
            } else {
                return FormatStyle.BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }
}
