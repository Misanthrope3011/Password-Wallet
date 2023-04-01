package com.example.demo.controllers;

import com.example.demo.config.EncryptionType;
import com.example.demo.dto.CredentialDTO;
import com.example.demo.entities.DataChange;
import com.example.demo.entities.UserEntity;
import com.example.demo.entities.UserPasswordsEntity;
import com.example.demo.enumeration.Action;
import com.example.demo.enumeration.FunctionDESC;
import com.example.demo.exceptions.ExceptionHandler;
import com.example.demo.services.EncryptionService;
import com.example.demo.services.SessionUtilsService;
import com.example.demo.services.UserActivityService;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SimpleROController {

	private final EncryptionService encryptionService;
	private final UserService userService;
	private final UserActivityService userActivityService;

	@PutMapping("/user/changePassword/{encryptionType}")
	public ResponseEntity<Object> encryptPassword(@PathVariable String encryptionType, @RequestBody CredentialDTO credentialDTO) throws JsonProcessingException {
		String currentUser = SessionUtilsService.getSessionUserName();
		UserEntity userEntity = userService.findByUsername(currentUser)
				.orElseThrow(() -> new ExceptionHandler("Problem with your session has occured. Please log in again"));
		String encryptedNewPassword = encryptionService.encrypt(credentialDTO.password(), userEntity.getDecryptionKey(), EncryptionType.valueOf(encryptionType));
		DataChange userActivity = userActivityService.registerNewActivity(FunctionDESC.EDIT_PASSWORD_USER,  userEntity.getPassword(), encryptedNewPassword, Action.EDIT,  UserEntity.class.getAnnotation(Table.class).name());
		userEntity.setPassword(encryptedNewPassword);

		return ResponseEntity.ok(userActivity);
	}

	@PostMapping("/user/addCredentials")
	public ResponseEntity<Object> addCredentials(@RequestBody CredentialDTO credentialDTO) throws JsonProcessingException {
		encryptionService.encryptGivenUserCredentials(credentialDTO);
		return ResponseEntity.ok(userActivityService.registerNewActivity(FunctionDESC.INSERT_USER_ENTITY, null, credentialDTO, Action.INSERT, UserPasswordsEntity.class.getAnnotation(Table.class).name()));
	}

	@DeleteMapping("/user/credentials/{id}/delete")
	public ResponseEntity<Object> delete(@RequestBody CredentialDTO credentialDTO) {

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

	@GetMapping("/user/actions")
	public List<DataChange> listChanges() {
		return userActivityService.listChanges();
	}

	@GetMapping("/user/action/{type}")
	public List<DataChange> listChanges(@PathVariable String type) {
		return userActivityService.listChangesByType(Action.valueOf(type));
	}

}
