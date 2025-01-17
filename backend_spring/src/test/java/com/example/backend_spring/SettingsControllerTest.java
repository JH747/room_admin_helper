package com.example.backend_spring;

import com.example.backend_spring.dto.*;
import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Supply;
import com.example.backend_spring.entity.SupplyConsumption;
import com.example.backend_spring.repository.SupplyRepository;
import com.example.backend_spring.service.AppUserService;
import com.example.backend_spring.service.SettingsService;
import com.example.backend_spring.service.SupplyService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TestTransaction;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

// 실제 톰캣 실행, 통합 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 임의의 포트에서 서버 실행
@ActiveProfiles("test")
@TestInstance(PER_CLASS) // 테스트 클래스당 인스턴스 한 번 생성
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order 전략 지정
public class SettingsControllerTest {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private SupplyService supplyService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JWTUtil jwtUtil;

    HttpHeaders headers = new HttpHeaders();

    AppUser appUser_t;
    StandardRoomsInfoDTO standardRoomsInfoDTO_t;
    SupplyDTO supplyDTO_t;
    SupplyConsumptionDTO supplyConsumptionDTO_t;


    @BeforeAll // Junit 5 부터 PER_CLASS 라이프사이클과 함께 non-static으로 사용 가능
    public void setUp(){
        appUser_t = appUserService.create("test_username", "test_password", "test_email");

        PlatformsAuthInfoDTO platformsAuthInfoDTO_t = new PlatformsAuthInfoDTO();
        platformsAuthInfoDTO_t.setYapenId("test_yapen_id");
        platformsAuthInfoDTO_t.setYapenPass("test_yapen_pass");
        platformsAuthInfoDTO_t.setYogeiId("test_yogei_id");
        platformsAuthInfoDTO_t.setYogeiPass("test_yogei_pass");
        settingsService.setPlatformsAuthInfo(platformsAuthInfoDTO_t, "test_username");

        standardRoomsInfoDTO_t = new StandardRoomsInfoDTO();
        standardRoomsInfoDTO_t.setRoomName("test_standard_room_name");
        standardRoomsInfoDTO_t.setRoomQuantity(5);
        standardRoomsInfoDTO_t.setDisplayOrder(1);
        settingsService.createStandardRoomsInfo(standardRoomsInfoDTO_t, "test_username");

        PlatformsRoomsInfoDTO platformsRoomsInfoDTO_t = new PlatformsRoomsInfoDTO();
        platformsRoomsInfoDTO_t.setStandardRoomName("test_standard_room_name");
        platformsRoomsInfoDTO_t.setYapenRoomName("test_yapen_room_name");
        platformsRoomsInfoDTO_t.setYogeiRoomName("test_yogei_room_name");
        platformsRoomsInfoDTO_t.setDisplayOrder(1);
        try{
            settingsService.createPlatformsRoomsInfo(platformsRoomsInfoDTO_t, "test_username");
        } catch (Exception e){
            e.printStackTrace();
        }

        supplyDTO_t = new SupplyDTO();
        supplyDTO_t.setName("test_supply");
        supplyDTO_t.setCurrentQuantity(10);
        supplyDTO_t.setThresholdQuantity(5);
        supplyDTO_t.setDesiredQuantity(7);
        supplyService.createSupply(supplyDTO_t, "test_username");

        supplyConsumptionDTO_t = new SupplyConsumptionDTO();
        supplyConsumptionDTO_t.setSupplyName("test_supply");
        supplyConsumptionDTO_t.setStandardRoomName("test_standard_room_name");
        supplyConsumptionDTO_t.setConsumption(10);
        supplyService.createSupplyConsumption(supplyConsumptionDTO_t, "test_username");

        String jwtToken = jwtUtil.generateToken("test_username");
        headers.set("Authorization", "Bearer " + jwtToken);

    }

    @Test
    @Order(1)
    @DisplayName("/settings/platformsAuthInfo: GET")
    public void testGetPlatformsAuthInfo(){
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<PlatformsAuthInfoDTO> response = restTemplate.exchange(
                "/settings/platformsAuthInfo",
                HttpMethod.GET,
                request,
                PlatformsAuthInfoDTO.class
        );

        PlatformsAuthInfoDTO resp = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp.getYapenId(), "test_yapen_id");
    }

    @Test
    @Order(2)
    @DisplayName("/settings/platformsAuthInfo: POST")
    public void testPostPlatformsAuthInfo(){
        PlatformsAuthInfoDTO platformsAuthInfoDTO = new PlatformsAuthInfoDTO();
        platformsAuthInfoDTO.setYapenId("test_yapen_id2");
        platformsAuthInfoDTO.setYapenPass("test_yapen_pass2");
        platformsAuthInfoDTO.setYogeiId("test_yogei_id2");
        platformsAuthInfoDTO.setYogeiPass("test_yogei_pass2");

        HttpEntity<PlatformsAuthInfoDTO> request = new HttpEntity<>(platformsAuthInfoDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/settings/platformsAuthInfo",
                HttpMethod.POST,
                request,
                String.class
        );

        String resp = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, "PlatformsAuthInfo Setting succeeded");
    }

    @Test
    @Order(3)
    @DisplayName("/settings/supply: GET")
    public void testGetRequestSupply(){
        HttpEntity<Void> request = new HttpEntity<>(headers);
        // restTemplate을 사용해 API 요청을 보낼 때, 그 요청은 테스트 메서드의 트랜잭션과는 별개의 트랜잭션에서 처리
        // 따라서 @Transactional 사용시 요청 이전 이후 롤백 발생으로 인한 문제 발생
        ResponseEntity<SupplyDTO[]> response = restTemplate.exchange(
                "/settings/supply", // url
                HttpMethod.GET, // request method
                request, // object that includes header and body of request
                SupplyDTO[].class // class type that body of response will be automatically converted
        );

        SupplyDTO[] resp = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp[0].getName(), "test_supply");

    }

    @Test
    @Order(4)
    @DisplayName("/settings/supply: POST")
    public void testPostRequestSupply(){
        SupplyDTO supplyDTO = new SupplyDTO();
        supplyDTO.setName("test_supply2");
        supplyDTO.setCurrentQuantity(10);
        supplyDTO.setThresholdQuantity(5);
        supplyDTO.setDesiredQuantity(7);

        HttpEntity<SupplyDTO> request = new HttpEntity<>(supplyDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/settings/supply",
                HttpMethod.POST,
                request,
                String.class
        );

        String resp = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, "Supply Setting succeeded");
    }

    @Test
    @Order(5)
    @DisplayName("/settings/supplyConsumption: GET")
    public void testGetRequestSupplyConsumption(){
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<SupplyConsumptionDTO[]> response = restTemplate.exchange(
                "/settings/supplyConsumption",
                HttpMethod.GET,
                request,
                SupplyConsumptionDTO[].class
        );
        SupplyConsumptionDTO[] resp = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp[0].getStandardRoomName(), "test_standard_room_name");
    }

    @Test
    @Order(6)
    @DisplayName("/settings/supplyConsumption: POST")
    public void testPostRequestSupplyConsumption(){
        SupplyConsumptionDTO supplyConsumptionDTO = new SupplyConsumptionDTO();
        supplyConsumptionDTO.setStandardRoomName("test_standard_room_name");
        supplyConsumptionDTO.setSupplyName("test_supply_name");
        supplyConsumptionDTO.setConsumption(2);

        HttpEntity<SupplyConsumptionDTO> request = new HttpEntity<>(supplyConsumptionDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/settings/supplyConsumption",
                HttpMethod.POST,
                request,
                String.class
        );

        String resp = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, "SupplyConsumption Setting succeeded");
    }

}
