package com.example.demo.controllers;

import com.example.demo.config.EncryptionType;
import com.example.demo.dto.CredentialDTO;
import com.example.demo.entities.UserEntity;
import com.example.demo.exceptions.ExceptionHandler;
import com.example.demo.services.EncryptionService;
import com.example.demo.services.SessionUtilsService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SimpleROController {

	private final EncryptionService encryptionService;
	private final UserService userService;

	@PutMapping("/user/changePassword/{encryptionType}")
	public ResponseEntity<UserEntity> encryptPassword(@PathVariable String encryptionType, @RequestBody CredentialDTO credentialDTO) {
		String currentUser = SessionUtilsService.getSessionUserName();
		UserEntity userEntity = userService.findByUsername(currentUser)
				.orElseThrow(() -> new ExceptionHandler("Problem with your session has occured. Please log in again"));
		userEntity.setPassword(encryptionService.encrypt(credentialDTO.password(), userEntity.getDecryptionKey(), EncryptionType.valueOf(encryptionType)));

		return ResponseEntity.ok(userService.saveNewUser(userEntity));
	}

	@PostMapping("/user/addCredentials")
	public ResponseEntity<Object> addCredentials(@RequestBody CredentialDTO credentialDTO) {

		return ResponseEntity.ok(encryptionService.encryptGivenUserCredentials(credentialDTO));
	}

	@PostMapping("/switchToRW")
	public ResponseEntity<Object> switchToRW() {
		SessionUtilsService.updateUserPermsToRW();

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/switchToRO")
	public ResponseEntity<Object> switchToRO() {
		SessionUtilsService.updateUserPermsToRO();

		return ResponseEntity.noContent().build();
	}

}
