package com.ssafy.server.chat.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MESSAGE_ID", updatable = false)
    private long messageID;

    private String sender; // 송신 유저 PK
    private String roomId; // 채팅방 번호 PK (지금은 임시로 UUID)

    public enum MessageType{
        ENTER, TALK, LEAVE, MEDIA;
    }

    @Enumerated(EnumType.STRING)
    private MessageType type; // 메시지 타입

    @Column(name = "message", nullable = false, columnDefinition = "VARCHAR(1024) CHARACTER SET UTF8")
    private String message; // 메시지
    private String time; // 채팅 발송 시간
}