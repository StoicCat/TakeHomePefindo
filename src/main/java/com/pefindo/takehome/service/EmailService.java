package com.pefindo.takehome.service;

public interface EmailService {
	void sendSimpleEmail(String to, String subject, String body);
}
