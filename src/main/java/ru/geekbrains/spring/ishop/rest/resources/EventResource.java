package ru.geekbrains.spring.ishop.rest.resources;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.spring.ishop.entity.Event;
import ru.geekbrains.spring.ishop.rest.outentities.OutEntity;
import ru.geekbrains.spring.ishop.rest.services.OutEntityService;
import ru.geekbrains.spring.ishop.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/event")
@Slf4j
@RequiredArgsConstructor
public class EventResource extends AbstractResource {
    private final Gson gson = new Gson();
    private final OutEntityService outEntityService;
    private final EventService eventService;

    @GetMapping(value = "/{eventId}/eventId")
    public ResponseEntity<OutEntity> getEventOutEntity(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(outEntityService.createOutEntity(eventService.findById(eventId)));
    }

    @GetMapping(value = "/serverUnaccepted/first")
    public ResponseEntity<OutEntity> getFirstServerUnacceptedEventOutEntity() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(outEntityService.createOutEntity(eventService.findFirstByServerAcceptedAtIsNull()));
    }

    @PutMapping(value = "/{eventId}/eventId/serverAcceptedAt")
    public ResponseEntity<OutEntity> updateServerAcceptedAtFieldOfEventOutEntity(
            @RequestBody @Valid LocalDateTime serverAcceptedAt,
            @PathVariable("eventId") Long eventId) {
        Event oldEvent = eventService.findById(eventId);
        oldEvent.setServerAcceptedAt(serverAcceptedAt);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(outEntityService.createOutEntity(eventService.save(oldEvent)));
    }

//    @PostMapping(value = "/save/incoming")
//    public ResponseEntity<OutEntity> saveIncomingEventOutEntity(@RequestBody @Valid OutEntity outEntity) {
//        Event event = outEntityService.recognizeAndSaveEventFromOutEntity(outEntity);
//        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
//                .body(outEntityService.createOutEntity(event));
//    }

    @PostMapping(value = "/save/incoming/string")
    public ResponseEntity<OutEntity> saveIncomingStringEventOutEntity(@RequestBody @Valid String json) {
        OutEntity outEntity = gson.fromJson(json, OutEntity.class);
        Event event = outEntityService.recognizeAndSaveEventFromOutEntity(outEntity);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(outEntityService.createOutEntity(event));
    }

}
