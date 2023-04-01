package com.example.demo.controllers;

import com.example.demo.config.EncryptionType;
import com.example.demo.dto.CredentialDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entities.UserEntity;
import com.example.demo.exceptions.ExceptionHandler;
import com.example.demo.services.EncryptionService;
import com.example.demo.services.SessionUtilsService;
import com.example.demo.services.UserAuthenticationService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SimpleController {

    private final EncryptionService encryptionService;
    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;

    @GetMapping("/user/decode")
    public ResponseEntity<Object> decode() {

        return ResponseEntity.ok(encryptionService.decryptUserPasswords());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> sampleResponse(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        Optional<UserEntity> entity = userService.findByUsername(userDTO.username());

        if (entity.isPresent()) {
            UserEntity user = entity.get();
            return userAuthenticationService.performLogin(request, userDTO, user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/signup/{encryption}")
    public ResponseEntity<Object> signUp(@RequestBody UserEntity user, @PathVariable String encryption) {
        try {
            EncryptionType encryptionType = EncryptionType.valueOf(encryption);
            if (!userService.isUserExists(user.getUsername())) {
                return ResponseEntity.ok(userAuthenticationService.signUpUser(user, encryptionType));
            }
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body("Wrong encryption type provided");
        }

        return ResponseEntity.status(409).body("User already exists");
    }


}
