package com.jsb.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remote-transfer")
public class RemoteTestController {
    private final RemoteTestService remoteTestService;

    @Autowired
    public RemoteTestController(RemoteTestService remoteTestService) {
        this.remoteTestService = remoteTestService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFiles() {
        // Implement the logic to transfer files here
        try {
            // Call a method to transfer files to the remote server
            remoteTestService.transferFilesToRemoteServer();
            return ResponseEntity.ok("Files transferred successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error transferring files: " + e.getMessage());
        }
    }
}
