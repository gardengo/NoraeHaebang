package com.ssafy.server.auth.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenKey {
    ACCESS("Authorization"),
    REFRESH("refreshToken");

    private String key;

    TokenKey(String key) {
        this.key = key;
    }
}
