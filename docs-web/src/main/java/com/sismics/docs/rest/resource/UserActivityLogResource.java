package com.sismics.docs.rest.resource;

import com.sismics.docs.core.constant.UserActivityLogType;
import com.sismics.docs.core.dao.UserActivityLogDao;
import com.sismics.docs.core.dao.criteria.UserActivityLogCriteria;
import com.sismics.docs.core.dao.dto.LoginStatisticsDto;
import com.sismics.docs.core.dao.dto.UserActivityLogDto;
import com.sismics.docs.core.model.jpa.UserActivityLog;
import com.sismics.docs.core.util.UserActivityLogUtil;
import com.sismics.rest.exception.ForbiddenClientException;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("/user-activity-log")
public class UserActivityLogResource extends BaseResource{

    @GET
    @Path("/{resource}")
    public Response list(@PathParam("resource") String resource) {
        boolean isAuth = authenticate();
        if(!isAuth) throw new ForbiddenClientException();
        if(!principal.getId().equals("admin")) throw new ForbiddenClientException();

        UserActivityLogDao userActivityLogDao = new UserActivityLogDao();
        UserActivityLogCriteria userActivityLogCriteria = new UserActivityLogCriteria();
        List<UserActivityLogType> types = new ArrayList<>();
        if(resource.equals("login")) {
            types.add(UserActivityLogType.LOGIN);
        }
        else if(resource.equals("document")) {
            types.add(UserActivityLogType.VIEW_DOCUMENT);
        }

        userActivityLogCriteria.setTypes(types);
        List<UserActivityLogDto> userActivityLogDtoList =
                userActivityLogDao.findByCriteria(userActivityLogCriteria, null);

        JsonArrayBuilder logs = Json.createArrayBuilder();
        for(UserActivityLogDto userActivityLogDto : userActivityLogDtoList){
            JsonObjectBuilder log = Json.createObjectBuilder()
                    .add("id", userActivityLogDto.getId())
                    .add("userId", userActivityLogDto.getUserId())
                    .add("userName", userActivityLogDto.getUsername())
                    .add("action", userActivityLogDto.getAction().name())
                    .add("time", userActivityLogDto.getTime().toString());

            if(userActivityLogDto.getDocumentId() != null){
                log.add("documentId", userActivityLogDto.getDocumentId())
                        .add("documentName", userActivityLogDto.getDocumentName());
            }

            logs.add(log.build());
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("logs", logs)
                .add("total", userActivityLogDtoList.size());

        return Response.ok().entity(response.build()).build();
    }

    @GET
    @Path("/login-stat")
    public Response loginStat(
        @QueryParam("scale") String scale,
        @QueryParam("startTime") String startTime,
        @QueryParam("endTime") String endTime
    ) {
        boolean isAuth = authenticate();
        if(!isAuth) throw new ForbiddenClientException();
        if(!principal.getId().equals("admin")) throw new ForbiddenClientException();

        LocalDateTime startTimeStd = UserActivityLogUtil.getLocalDateTime(startTime, scale, true);
        LocalDateTime endTimeStd = UserActivityLogUtil.getLocalDateTime(endTime, scale, false);
        if(startTimeStd == null || endTimeStd == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserActivityLogDao userActivityLogDao = new UserActivityLogDao();
        UserActivityLogCriteria userActivityLogCriteria = new UserActivityLogCriteria();
        List<UserActivityLogType> types = new ArrayList<>();
        types.add(UserActivityLogType.LOGIN);
        userActivityLogCriteria.setTypes(types);
        userActivityLogCriteria.setStartTime(startTimeStd);
        userActivityLogCriteria.setEndTime(endTimeStd);

        List<LoginStatisticsDto> loginStatisticsDtos =
                userActivityLogDao.loginStatistics(userActivityLogCriteria, scale);
        loginStatisticsDtos = UserActivityLogUtil.fillMissingDates(
                loginStatisticsDtos,
                scale,
                startTimeStd,
                endTimeStd
        );
        if(loginStatisticsDtos == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        JsonArrayBuilder logs = Json.createArrayBuilder();
        for(LoginStatisticsDto loginStatisticsDto : loginStatisticsDtos){
            JsonObjectBuilder log = Json.createObjectBuilder();
            log.add("date", loginStatisticsDto.getDate());
            log.add("cnt", loginStatisticsDto.getLoginCount());
            logs.add(log.build());
        }
        JsonObjectBuilder response = Json.createObjectBuilder();
        response.add("logs", logs);
        return Response.ok().entity(response.build()).build();
    }
}
