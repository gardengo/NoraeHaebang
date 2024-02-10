package com.ssafy.server.notification.cotnoller;

import com.ssafy.server.notification.util.SseEmitters;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {

    private final SseEmitters sseEmitters; //콜백함수 다른 쓰레드에서 실행되므로 thread-safe한 자료구조 사용.

    public NotificationController(SseEmitters sseEmitters){
        this.sseEmitters =  sseEmitters;
    }

    //요청보낸 유저의 구독(연결) 요청
    @GetMapping(value = "/subscribe")
    public ResponseEntity<SseEmitter> subscribe() throws IOException {
        System.out.println("subscribe() 컨트롤러 메소드 호출");
        String userId = "1"; //추후 헤더에서 가져온다.
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); //새로운 연결 객체 생성. 매개변수로 만료시간 줄 수 있다. 기본30초
        sseEmitters.add(userId, emitter); //객체 메모리에 저장.

        //연결했으면, 더미데이터 연결확인 메시지 보내기.
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")    //해당 이벤트의 이름 지정.
                    .data("connected!")); //503 에러 방지를 위한 더미 데이터
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //보내고나서, 연결 잘됐다고 응답해주기.
        return ResponseEntity.ok(emitter);
    }

    //알림메시지 보내기.
    @PostMapping("/sendNotification")
    public ResponseEntity<Void> sendNotification(@RequestParam String message, @RequestParam String userId, @RequestParam String type) {
        //메시지 보낼 유저의 emitter객체찾기.
        SseEmitter emitter = sseEmitters.getSseEmitter(userId);
        if (emitter != null) {
            System.out.println("메시지 보낼 emitter : "+emitter);
            try {
                //메시지보내고
                emitter.send(SseEmitter.event()
                        .name("karaoke")
                        .data(message, MediaType.TEXT_PLAIN));

//                디비에저장도 해야지
           } catch (IOException e) {
                sseEmitters.remove(userId, emitter);
            }
        }
        else{
            System.out.println("에미터 없음.");
        }
        return ResponseEntity.ok().build();
    }
}
