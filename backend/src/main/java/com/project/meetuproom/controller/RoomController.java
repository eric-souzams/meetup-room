package com.project.meetuproom.controller;

import com.project.meetuproom.exception.ResourceNotFoundException;
import com.project.meetuproom.model.Room;
import com.project.meetuproom.repository.RoomRepository;
import com.project.meetuproom.utils.MessageUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/rooms")
public class RoomController {

    private RoomRepository roomRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Room> getById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.ROOM_NOT_FOUND));

        return ResponseEntity.ok().body(room);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        Room result = roomRepository.save(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Room> updateRoom(@PathVariable(value = "id") Long id,
                                           @Valid @RequestBody Room roomDetails) throws ResourceNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.ROOM_NOT_FOUND));

        room.setName(roomDetails.getName());
        room.setData(roomDetails.getData());
        room.setStartHour(roomDetails.getStartHour());
        room.setEndHour(roomDetails.getEndHour());

        Room updatedRoom = roomRepository.save(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping(value = "/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageUtils.ROOM_NOT_FOUND));

        roomRepository.delete(room);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

}
