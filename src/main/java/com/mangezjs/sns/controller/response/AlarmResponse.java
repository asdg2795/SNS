package com.mangezjs.sns.controller.response;

import com.mangezjs.sns.model.Alarm;
import com.mangezjs.sns.model.AlarmArgs;
import com.mangezjs.sns.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;


@AllArgsConstructor
@Data
public class AlarmResponse {
    private Integer id;
    private String text;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm){
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
