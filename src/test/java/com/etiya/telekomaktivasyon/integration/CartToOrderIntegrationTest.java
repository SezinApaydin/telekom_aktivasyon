package com.etiya.telekomaktivasyon.integration;

import com.etiya.telekomaktivasyon.entity.PackageEntity;
import com.etiya.telekomaktivasyon.entity.SimCard;
import com.etiya.telekomaktivasyon.repository.PackageRepository;
import com.etiya.telekomaktivasyon.repository.SimCardRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartToOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private SimCardRepository simCardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SimCard testSimCard;

    @Test
    void shouldCompleteFullCartToOrderFlow() throws Exception {
        testSimCard = new SimCard();
        testSimCard.setMsisdn("5009998877");
        testSimCard.setIccid("8990000000000000001");
        testSimCard.setStatus("available");
        testSimCard.setIsReserved(false);
        testSimCard = simCardRepository.save(testSimCard);

        PackageEntity firstPackage = packageRepository.findByIsActiveTrue().get(0);

        String cartResponse = mockMvc.perform(post("/api/cart").param("customerIdentifier", "integration-test-user"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Integer cartId = (Integer) objectMapper.readValue(cartResponse, Map.class).get("id");

        mockMvc.perform(post("/api/cart/" + cartId + "/items/package/" + firstPackage.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/cart/" + cartId + "/items/sim/" + testSimCard.getId()))
                .andExpect(status().isOk());

        String addressJson = objectMapper.writeValueAsString(Map.of(
                "city", "Eskişehir",
                "district", "Tepebaşı",
                "addressLine", "Test Sokak No:1",
                "postalCode", "26000"
        ));
        String addressResponse = mockMvc.perform(post("/api/addresses")
                        .contentType("application/json")
                        .content(addressJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Integer addressId = (Integer) objectMapper.readValue(addressResponse, Map.class).get("id");

        String checkoutJson = objectMapper.writeValueAsString(Map.of(
                "cartId", cartId,
                "addressId", addressId,
                "customerName", "Integration Test",
                "tckn", "11111111110"
        ));
        mockMvc.perform(post("/api/checkout")
                        .contentType("application/json")
                        .content(checkoutJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @AfterEach
    void cleanUp() {
        if (testSimCard != null) {
            simCardRepository.deleteById(testSimCard.getId());
        }
    }
}