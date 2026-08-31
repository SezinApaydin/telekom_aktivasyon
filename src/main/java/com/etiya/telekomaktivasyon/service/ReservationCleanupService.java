package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.SimCard;
import com.etiya.telekomaktivasyon.repository.SimCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationCleanupService {

    @Autowired
    private SimCardRepository simCardRepository;

    private static final int RESERVATION_TIMEOUT_MINUTES = 2;

    @Scheduled(fixedRate = 30000)
    public void releaseExpiredReservations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(RESERVATION_TIMEOUT_MINUTES);
        List<SimCard> expiredReservations = simCardRepository.findByStatusAndReservedAtBefore("reserved", threshold);

        for (SimCard simCard : expiredReservations) {
            simCard.setStatus("available");
            simCard.setIsReserved(false);
            simCard.setReservedAt(null);
            simCardRepository.save(simCard);
        }

        if (!expiredReservations.isEmpty()) {
            System.out.println(expiredReservations.size() + " adet rezervasyon süresi doldu, serbest bırakıldı.");
        }
    }
}