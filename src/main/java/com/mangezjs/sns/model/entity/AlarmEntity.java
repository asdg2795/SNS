package com.mangezjs.sns.model.entity;

import com.mangezjs.sns.model.AlarmArgs;
import com.mangezjs.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.mapping.TypeDef;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name =  "`alarm`", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알람을 받은 사람
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private AlarmArgs args;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args){
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        entity.setArgs(args);
        return entity;
    }

}

