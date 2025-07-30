package com.microservices.ecommerce.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
	
	private final JavaMailSender javaMailSender;
	
	@KafkaListener(topics = "order-placed")
	public void listen(com.microservices.ecommerce.order_service.event.OrderPlacedEvent orderPlacedEvent) {
		log.info("Kafka got message: {}", orderPlacedEvent);
		
		MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("springecom@email.com");
			messageHelper.setTo(orderPlacedEvent.getEmail().toString());
			messageHelper.setSubject(String.format("Your order with order number %s is placed successfully", orderPlacedEvent.getOrderNumber()));
			messageHelper.setText(String.format("""
					Hi,
					Your order number %s is now placed successfully.
					
					Best Regards
					Spring Ecommerce
					""", orderPlacedEvent.getOrderNumber()));
		};
		
		try {
			javaMailSender.send(mimeMessagePreparator);
			log.info("Notification sent!");
		} catch(MailException me) {
			log.error("Exception occurred during send mail.", me);
			throw new RuntimeException("Exception during send mail", me);
		}
			
	}
}
