package com.ssafy.server.hit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.server.api.ApiResponse;
import com.ssafy.server.common.error.ApiException;
import com.ssafy.server.common.error.ApiExceptionFactory;
import com.ssafy.server.hit.error.HitExceptionEnum;
import com.ssafy.server.hit.model.Hit;
import com.ssafy.server.hit.service.HitService;
import com.ssafy.server.like.error.LikeExceptionEnum;
import com.ssafy.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import java.util.UUID;

@RestController
@RequestMapping("/hit")
public class HitController {

    private final HitService hitService;
    private final UserService userService;

    @Autowired
    HitController(HitService hitService, UserService userService) {
        this.hitService = hitService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public void create(@RequestBody JsonNode jsonNode) {
        int feedId = -1, userPk = -1;
        UUID userUUID = null;
        try {
            feedId = Integer.parseInt(jsonNode.get("feedId").asText());
            if(jsonNode.get("uuid") != null) {
                userUUID = UUID.fromString(jsonNode.get("uuid").asText());
                userPk = userService.getUserPk(userUUID);
            }
            else {
                userPk = Integer.parseInt(jsonNode.get("userPk").asText());
            }

            hitService.save(feedId, userPk);
        } catch(Exception e) {
            return;
        }
    }

    @PostMapping("/get/{feedId}")
    public ResponseEntity<ApiResponse<Integer>> get(@PathVariable int feedId) {
        Integer count = Integer.MIN_VALUE;
        try {
            count = hitService.findAllByFeedId(feedId);
        } catch(Exception e) {
            throw new ApiException(ApiExceptionFactory.fromExceptionEnum(HitExceptionEnum.HIT_FETCH_ERROR));
        }
        return new ResponseEntity<>(ApiResponse.success(count), HttpStatus.ACCEPTED);
    }
}
