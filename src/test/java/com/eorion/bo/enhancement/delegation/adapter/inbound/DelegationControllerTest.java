package com.eorion.bo.enhancement.delegation.adapter.inbound;

import com.eorion.bo.enhancement.delegation.adapter.outbound.DelegationManagementRepository;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementSaveDTO;
import com.eorion.bo.enhancement.delegation.domain.dto.inbound.DelegationManagementUpdateDTO;
import com.eorion.bo.enhancement.delegation.domain.entity.Delegation;
import com.eorion.bo.enhancement.delegation.utils.BatchSQLExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.IdentityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DelegationControllerTest {
    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("demo:demo".getBytes(StandardCharsets.UTF_8)));
    }

    private final InputStreamReader delegationManagementDeleteReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("sql/delete-all.sql")));
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private DelegationManagementRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private BatchSQLExecutor executor;

    @BeforeEach
    public void clearUp() throws SQLException {
        executor.batchExecuteSqlFromFile(delegationManagementDeleteReader);
        identityService.setAuthenticatedUserId("demo");
    }

    @Test
    public void createDelegationManagementReturn200() throws Exception {

        DelegationManagementSaveDTO saveDTO = new DelegationManagementSaveDTO();
        saveDTO.setOwnerUserId("demo");
        saveDTO.setTenant("tenant");
        saveDTO.setGroups("group1,group2");
        saveDTO.setDelegateName("delegateName");
        saveDTO.setOwnerUserName("demo");
        saveDTO.setDelegateToUserName("userName");
        saveDTO.setStartDateTime(System.currentTimeMillis());
        saveDTO.setEndDateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        saveDTO.setDelegateToUserId("userId");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/enhancement/delegation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(mapper.writeValueAsString(saveDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void updateDelegationManagementReturn204() throws Exception {
        Delegation saveDTO = new Delegation();
        saveDTO.setOwnerUserId("demo");
        saveDTO.setTenant("tenant");
        saveDTO.setGroups("group1,group2");
        saveDTO.setDelegateName("delegateName");
        saveDTO.setOwnerUserName("demo");
        saveDTO.setDelegateToUserName("userName");
        saveDTO.setStartDateTime(System.currentTimeMillis());
        saveDTO.setEndDateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        saveDTO.setDelegateToUserId("userId");
        saveDTO.setCreatedBy("demo");

        repository.save(saveDTO);

        DelegationManagementUpdateDTO updateDTO = new DelegationManagementUpdateDTO();
        updateDTO.setDelegateName("name");
        updateDTO.setGroups("groups");
        updateDTO.setDelegateToUserId("tom");
        updateDTO.setStartDateTime(System.currentTimeMillis());
        updateDTO.setEndDateTime(System.currentTimeMillis());
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/enhancement/delegation/{id}", saveDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .content(mapper.writeValueAsString(updateDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        var management = repository.getById(saveDTO.getId());
        Assertions.assertNotNull(management);
        Assertions.assertEquals(management.getDelegateToUserId(), updateDTO.getDelegateToUserId());
    }

    @Test
    public void getGrantedToMeDelegationManagementsReturn200() throws Exception {

        Delegation saveDTO = new Delegation();
        saveDTO.setOwnerUserId("demo");
        saveDTO.setTenant("tenant");
        saveDTO.setGroups("group1,group2");
        saveDTO.setDelegateName("delegateName");
        saveDTO.setOwnerUserName("demo");
        saveDTO.setDelegateToUserName("userName");
        saveDTO.setStartDateTime(System.currentTimeMillis());
        saveDTO.setEndDateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        saveDTO.setDelegateToUserId("userId");
        saveDTO.setCreatedBy("demo");
        for (int i = 0; i < 5; i++) {
            saveDTO.setId(null);
            repository.save(saveDTO);
        }


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/delegation/granted")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .param("tenant", "tenant")
                                .param("delegateToUserId", "userId")
                                .param("firstResult", "0")
                                .param("maxResults", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andDo(print());
    }

    @Test
    public void getMyDelegationManagementsReturn200() throws Exception {

        Delegation saveDTO = new Delegation();
        saveDTO.setOwnerUserId("demo");
        saveDTO.setTenant("tenant");
        saveDTO.setGroups("group1,group2");
        saveDTO.setDelegateName("delegateName");
        saveDTO.setOwnerUserName("demo");
        saveDTO.setDelegateToUserName("userName");
        saveDTO.setStartDateTime(System.currentTimeMillis());
        saveDTO.setEndDateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        saveDTO.setDelegateToUserId("userId");
        saveDTO.setCreatedBy("demo");
        for (int i = 0; i < 5; i++) {
            saveDTO.setId(null);
            repository.save(saveDTO);
        }


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/enhancement/delegation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                                .param("tenant", "tenant")
                                .param("ownerUserId", "demo")
                                .param("firstResult", "0")
                                .param("maxResults", "2")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(2))
                .andDo(print());
    }


    @Test
    public void deleteDelegationManagementReturn204() throws Exception {

        Delegation saveDTO = new Delegation();
        saveDTO.setOwnerUserId("demo");
        saveDTO.setTenant("tenant");
        saveDTO.setGroups("group1,group2");
        saveDTO.setDelegateName("delegateName");
        saveDTO.setOwnerUserName("demo");
        saveDTO.setDelegateToUserName("userName");
        saveDTO.setStartDateTime(System.currentTimeMillis());
        saveDTO.setEndDateTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        saveDTO.setDelegateToUserId("userId");
        saveDTO.setCreatedBy("demo");

        repository.save(saveDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/enhancement/delegation/{id}", saveDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(headers)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
