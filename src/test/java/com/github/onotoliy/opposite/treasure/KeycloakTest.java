package com.github.onotoliy.opposite.treasure;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class KeycloakTest {

    @Test
    public void test() throws KeyManagementException, NoSuchAlgorithmException, Exception {
        Keycloak keycloak = Keycloak
                .getInstance(
                        "https://185.12.95.242:44",
                        "opposite",
                        "admin",
                        "admin",
                        "admin-cli"
                );

        UsersResource ur = keycloak.realm("opposite").users();
//
        try (InputStream is = new FileInputStream("C:\\Projects\\onotoliy\\opposite\\opposite-treasure-service\\as.json")) {
            ArrayNode users = (ArrayNode) new ObjectMapper().readTree(is);
//
//            StringBuilder sql = new StringBuilder("insert into USER_ENTITY(id, username, first_name, last_name, enabled, realm_id, created_timestamp)values");

            //                UserRepresentation representation = new UserRepresentation();
            //                representation.setId(it.get("id").asText());
            //                representation.setFirstName(it.get("firstName").asText());
            //                representation.setLastName(it.get("lastName").asText());
            //                representation.setUsername(it.get("username").asText());
            //
            //                Response response = ur.create(representation);
            //
            //                System.out.println(response.getStatus());
            //                System.out.println(response.readEntity(String.class));
//            for (JsonNode it : users) {
//                sql.append(String.format("('%s', '%s', '%s','%s', true, 'de616e4a-9bb9-42a7-b319-1e94ea841e80',  1742456453107),",
//                        it.get("id").asText(),
//                        it.get("username").asText(),
//                        Optional.of(it).map(it1 -> it1.get("firstName")).map(JsonNode::asText).orElse(""),
//                        Optional.of(it).map(it1 -> it1.get("lastName")).map(JsonNode::asText).orElse("")
//                ));
//            }
//
//            System.out.println(sql.toString());

            users.forEach(it -> {
                System.out.println(ur.get(it.get("id").asText()).toRepresentation().getUsername().equals(it.get("username").asText()));
            });

        }


    }

}
