package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.UserActivityLogType;
import com.sismics.docs.core.dao.criteria.UserActivityLogCriteria;
import com.sismics.docs.core.dao.dto.LoginStatisticsDto;
import com.sismics.docs.core.dao.dto.UserActivityLogDto;
import com.sismics.docs.core.model.jpa.UserActivityLog;
import com.sismics.docs.core.util.jpa.QueryParam;
import com.sismics.docs.core.util.jpa.QueryUtil;
import com.sismics.docs.core.util.jpa.SortCriteria;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class UserActivityLogDao {

    public String create(UserActivityLog userActivityLog) {
        userActivityLog.setId(UUID.randomUUID().toString());
        userActivityLog.setTime(LocalDateTime.now());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(userActivityLog);
        return userActivityLog.getId();
    }

    public List<LoginStatisticsDto> loginStatistics(
            UserActivityLogCriteria criteria,
            String scale
    ) {
        if(scale == null) return null;
        StringBuilder baseQuery = new StringBuilder("");
        String selectExpr = "";
        String groupByExpr = "";
        switch (scale) {
            case "daily":
                selectExpr = "select FORMATDATETIME(UAL_TIME_D, 'yyyy-MM-dd') as d, COUNT(*) as c";
                groupByExpr = " group by FORMATDATETIME(UAL_TIME_D, 'yyyy-MM-dd')";
                break;
            case "monthly":
                selectExpr = "select FORMATDATETIME(UAL_TIME_D, 'yyyy-MM') as d, COUNT(*) as c";
                groupByExpr = " group by FORMATDATETIME(UAL_TIME_D, 'yyyy-MM')";
                break;
            case "annually":
                selectExpr = "select FORMATDATETIME(UAL_TIME_D, 'yyyy') as d, COUNT(*) as c";
                groupByExpr = " group by FORMATDATETIME(UAL_TIME_D, 'yyyy')";
                break;
            default:
                return null;
        }
        baseQuery.append(selectExpr);
        baseQuery.append(" from T_USER_ACTIVITY_LOG l");
        List<String> criteriaList = new ArrayList<>();
        Map<String, Object> parameterMap = new HashMap<>();
        if(criteria != null){
            if(criteria.getStartTime() != null){
                criteriaList.add("l.UAL_TIME_D >= :startTime");
                parameterMap.put("startTime", criteria.getStartTime());
            }
            if(criteria.getEndTime() != null){
                criteriaList.add("l.UAL_TIME_D <= :endTime");
                parameterMap.put("endTime", criteria.getEndTime());
            }
            if(criteria.getTypes() != null){
                criteriaList.add("l.UAL_ACTION_N in (:types)");
                List<Integer> typeCodes = new ArrayList<>();
                for(UserActivityLogType type : criteria.getTypes()){
                    typeCodes.add(type.getCode());
                }
                parameterMap.put("types", typeCodes);
            }
        }
        if(!criteriaList.isEmpty()){
            baseQuery.append(" where ");
            baseQuery.append(String.join(" and ", criteriaList));
        }
        baseQuery.append(groupByExpr);
        baseQuery.append(" order by d");
        QueryParam queryParam = new QueryParam(baseQuery.toString(), parameterMap);
        @SuppressWarnings("unchecked")
        List<Object[]> l = QueryUtil.getNativeQuery(queryParam).getResultList();

        List<LoginStatisticsDto> loginStatisticsDtos = new ArrayList<>();
        for(Object[] row : l){
            int i = 0;
            LoginStatisticsDto dto = new LoginStatisticsDto();
            dto.setDate((String) row[i++]);
            dto.setLoginCount((Long) row[i++]);
            loginStatisticsDtos.add(dto);
        }

        return loginStatisticsDtos;
    }

    public List<UserActivityLogDto> findByCriteria(UserActivityLogCriteria criteria, SortCriteria sortCriteria){
        Map<String, Object> parameterMap = new HashMap<>();
        List<String> criteriaList = new ArrayList<>();
        StringBuilder baseQuery = new StringBuilder("select * from T_USER_ACTIVITY_LOG l ");

        if(criteria.getUserId() != null){
            criteriaList.add("l.UAL_USERID_C = :userId");
            parameterMap.put("userId", criteria.getUserId());
        }

        if(criteria.getStartTime() != null){
            criteriaList.add("l.UAL_TIME_D >= :startTime");
            parameterMap.put("startTime", criteria.getStartTime());
        }

        if(criteria.getEndTime() != null){
            criteriaList.add("l.UAL_TIME_D <= :endTime");
            parameterMap.put("endTime", criteria.getEndTime());
        }

        if(criteria.getTypes() != null && !criteria.getTypes().isEmpty()){
            criteriaList.add("l.UAL_ACTION_N in (:types)");
            List<Integer> typeCodes = new ArrayList<>();
            for(UserActivityLogType type : criteria.getTypes()){
                typeCodes.add(type.getCode());
            }
            parameterMap.put("types", typeCodes);
        }

        if(!parameterMap.isEmpty()){
            baseQuery.append(" where ");
            baseQuery.append(String.join(" and ", criteriaList));
        }

        QueryParam queryParam = new QueryParam(baseQuery.toString(), parameterMap);
        if(sortCriteria != null){
            queryParam = QueryUtil.getSortedQueryParam(new QueryParam(baseQuery.toString(), parameterMap), sortCriteria);
        }
        else {
            StringBuilder tmp_sb = new StringBuilder(queryParam.getQueryString());
            tmp_sb.append(" order by l.UAL_TIME_D asc");
            queryParam = new QueryParam(tmp_sb.toString(), queryParam.getParameterMap());
        }

        @SuppressWarnings("unchecked")
        List<Object[]> l = QueryUtil.getNativeQuery(queryParam).getResultList();

        List<UserActivityLogDto> UALDtoList = new ArrayList<>();
        for(Object[] row : l){
            int i = 0;
            UserActivityLogDto dto = new UserActivityLogDto();
            dto.setId((String) row[i++]);
            dto.setUserId((String) row[i++]);
            dto.setUsername((String) row[i++]);
            dto.setAction(UserActivityLogType.getType((Integer) row[i++]));
            dto.setDocumentId((String) row[i++]);
            dto.setDocumentName((String) row[i++]);
            Timestamp ts = (Timestamp) row[i++];
            dto.setTime(ts.toLocalDateTime());
            UALDtoList.add(dto);
        }
        return UALDtoList;
    }

}
