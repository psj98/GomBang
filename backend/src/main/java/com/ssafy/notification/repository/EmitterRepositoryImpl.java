package com.ssafy.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {

    // 모든 Emitters를 저장하는 ConcurrentHashMap
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    /**
     * 주어진 아이디와 이미터를 저장한다.
     *
     * @param emitterId Emitter 아이디
     * @param emitter 이벤트 Emitter
     */
    @Override
    public SseEmitter save(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
        return emitter;
    }

    /**
     * 이벤트를 저장한다.
     *
     * @param eventCacheId 이벤트 아이디
     * @param event 이벤트
     */
    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    /**
     * 해당 사용자와 관련된 모든 Emitter를 찾는다.
     * 브라우저당 여러 개 연결이 가능하기 때문에 여러개의 Emitter가 존재할 수 있다.
     *
     * @param memberId 사용자 아이디
     */
    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 해당 사용자와 관련된 모든 이벤트를 찾는다.
     *
     * @param memberId 사용자 아이디
     */
    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 주어진 아이디의 Emitter를 지운다.
     *
     * @param emitterId emitterId
     */
    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    /**
     * 해당 사용자와 관련된 모든 Emitter를 지운다.
     *
     * @param memberId 사용자 아이디
     */
    @Override
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    /**
     * 해당 사용자와 관련된 모든 이벤트를 지운다.
     *
     * @param memberId 사용자 아이디
     */
    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }

//    /**
//     * 주어진 아이디의 Emitter를 가져온다.
//     *
//     * @param id emitterId
//     * @return SseEmitter 이벤트 Emitter
//     */
//    public SseEmitter get(String id) {
//        return emitters.get(id);
//    }
}
