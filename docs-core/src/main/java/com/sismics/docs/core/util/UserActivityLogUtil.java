package com.sismics.docs.core.util;


import com.sismics.docs.core.constant.UserActivityLogType;
import com.sismics.docs.core.dao.DocumentDao;
import com.sismics.docs.core.dao.UserActivityLogDao;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.dto.LoginStatisticsDto;
import com.sismics.docs.core.model.jpa.UserActivityLog;
import org.joda.time.DateTime;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class UserActivityLogUtil {

    public static void createDocumentActivityLog(String userId, UserActivityLogType type, String documentId) {
        if (userId == null || userId.isEmpty()) return;
        if (documentId == null || documentId.isEmpty()) return;

        UserDao userDao = new UserDao();
        DocumentDao documentDao = new DocumentDao();
        UserActivityLogDao userActivityLogDao = new UserActivityLogDao();

        String username = userDao.getById(userId).getUsername();
        if (username == null || username.isEmpty()) return;
        String documentName = documentDao.getById(documentId).getTitle();
        if (documentName == null) return;

        UserActivityLog log = new UserActivityLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setAction(type.getCode());
        log.setDocumentId(documentId);
        log.setDocumentName(documentName);

        userActivityLogDao.create(log);
    }

    public static void createUserActivityLog(String userId, UserActivityLogType type) {
        if (userId == null || userId.isEmpty()) return;

        UserDao userDao = new UserDao();
        UserActivityLogDao userActivityLogDao = new UserActivityLogDao();
        String username = userDao.getById(userId).getUsername();
        if (username == null || username.isEmpty()) return;

        UserActivityLog log = new UserActivityLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setAction(type.getCode());
        log.setDocumentId(null);
        log.setDocumentName(null);

        userActivityLogDao.create(log);
    }

    public static List<LoginStatisticsDto> fillMissingDates(
            List<LoginStatisticsDto> loginStatisticsDtos,
            String scale,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        if (loginStatisticsDtos == null || loginStatisticsDtos.isEmpty()) return null;
        if (startTime.isAfter(endTime)) return null;

        Map<String, LoginStatisticsDto> timeMap = new HashMap<>();
        for (LoginStatisticsDto dto : loginStatisticsDtos) timeMap.put(dto.getDate(), dto);
        List<LoginStatisticsDto> result = new ArrayList<>();
        DateTimeFormatter formatter;
        switch (scale) {
            case "daily": formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); break;
            case "monthly": formatter = DateTimeFormatter.ofPattern("yyyy-MM"); break;
            case "annually": formatter = DateTimeFormatter.ofPattern("yyyy"); break;
            default: return null;
        }

        LocalDateTime tmp = startTime;
        try {
            while (tmp.isBefore(endTime)) {
                String timeIdx = tmp.format(formatter);
                if (!timeMap.containsKey(timeIdx)) {
                    LoginStatisticsDto dto = new LoginStatisticsDto();
                    dto.setDate(timeIdx).setLoginCount(0L);
                    result.add(dto);
                } else {
                    result.add(timeMap.get(timeIdx));
                }
                switch (scale) {
                    case "daily": tmp = tmp.plusDays(1); break;
                    case "monthly": tmp = tmp.plusMonths(1); break;
                    case "annually": tmp = tmp.plusYears(1); break;
                    default: return null;
                }
            }
        } catch(DateTimeParseException e){
            return null;
        }
        return result;
    }

    public static LocalDateTime getLocalDateTime(String time, String scale, boolean isStart) {
        DateTimeFormatter formatter;
        LocalDateTime result;
        try {
            switch (scale) {
                case "daily": {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(time, formatter);
                    if (isStart) result = date.atStartOfDay();
                    else result = date.atTime(23, 59, 59, 999);
                    break;
                }
                case "monthly": {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                    YearMonth yearMonth = YearMonth.parse(time, formatter);
                    if (isStart) result = yearMonth.atDay(1).atStartOfDay();
                    else result = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999);
                    break;
                }
                case "annually": {
                    formatter = DateTimeFormatter.ofPattern("yyyy");
                    Year year = Year.parse(time, formatter);
                    if (isStart) result = year.atDay(1).atStartOfDay();
                    else result = year.atMonth(12).atEndOfMonth().atTime(23, 59, 59, 999);
                    break;
                }
                default: {
                    return null;
                }
            }
        } catch(DateTimeParseException e) {
            return null;
        }

        return result;
    }
}
