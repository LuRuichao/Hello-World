package com.unisk.kafkatest.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.unisk.kafkatest.entity.ProbeData;
import com.unisk.kafkatest.entity.UserData;

@Component
public class KafkaService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(ProbeData probeData) {
		String deviceMac = probeData.getDeviceMac();
		String shopId = probeData.getShopId();
		List<UserData> data = probeData.getData();

		StringBuilder msgSB = new StringBuilder();
		if (data != null && !data.isEmpty()) {
			for (UserData userData : data) {
				String visitorMac = userData.getVisitorMac();
				String visitorTime = userData.getVisitorTime();
				String msg = shopId + "," + deviceMac + "," + visitorMac + "," + visitorTime;

				msgSB.append(msg).append(";");
			}
			@SuppressWarnings("rawtypes")
			ListenableFuture futher = kafkaTemplate.sendDefault(msgSB.toString());
//			/*
				// 此处用于控制是否同步
				try {
					futher.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
//			*/
		}
	}
}
