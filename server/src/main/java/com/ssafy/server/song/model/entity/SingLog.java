package com.ssafy.server.song.model.entity;

import com.ssafy.server.audit.Auditable;
import com.ssafy.server.user.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "sing_log")
@Getter
@Setter
public class SingLog extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sing_log_id")
    private int singLogId;

    @ManyToOne
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "sing_mode")
    private String singMode;

    @Column(name = "sing_status")
    private String singStatus;

    @Column(name = "sing_score")
    private int singScore;

}
