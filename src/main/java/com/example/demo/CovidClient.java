package com.example.demo;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.glassfish.jersey.client.ClientConfig;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;

public class CovidClient {
static  ObjectMapper mapper = JsonMapper.builder()
        .findAndAddModules()
        .build();
    private static String baseURI = "http://localhost:8080/api";
    private static String encodedString = "Basic YWRtaW46YWRtaW4=";
    public static void main(String[] args) throws JsonProcessingException {
        testList();
        testAdd();
        testMean();
        testMedian();
        testUpdate();
        testDelete();
    }

    private static void testUpdate() throws JsonProcessingException {
        WebTarget target = getWebTarget("");
        CovidData covidData = new CovidData(23,23,23, LocalDate.of(2020,2,25));
        String json = mapper.writeValueAsString(covidData);
        String productId = "25-02-2020";
        Response response = target.path(productId).request().header("Authorization",encodedString)
                .put(Entity.entity(json, MediaType.APPLICATION_JSON), Response.class);
        System.out.println(response);
    }

    private static void testMean() {
        WebTarget target = getWebTarget("/mean");

        String response = target.request().header("Authorization",encodedString).accept(MediaType.APPLICATION_JSON).get(String.class);

        System.out.println(response);
    }

    private static void testMedian() {
        WebTarget target = getWebTarget("/median");

        String response = target.request().header("Authorization",encodedString).accept(MediaType.APPLICATION_JSON).get(String.class);

        System.out.println(response);
    }

    //    private static void testGet(){
//        WebTarget target = getWebTarget();
//        String productId = "2";
//        String product = target.path(productId)
//                .request().accept(MediaType.APPLICATION_JSON)
//                .get(CovidData.class);
//
//        System.out.println(product);
//    }
    private static void testDelete() {
        WebTarget target = getWebTarget("");
        String productId = "25-02-2020";
        Response response = target.path(productId).request().header("Authorization",encodedString)
                .delete(Response.class);
        System.out.println(response);
    }

    static void testAdd() throws JsonProcessingException {
        CovidData covidData = new CovidData(23,23,23, LocalDate.of(2020,2,25));

        String json = mapper.writeValueAsString(covidData);

        WebTarget target = getWebTarget("");
        Response response = target.request().header("Authorization",encodedString)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON), Response.class);

        System.out.println(response.getLocation().toString());
    }

    //GET all request
    static void testList() {

        WebTarget target = getWebTarget("");

        String response = target.request().header("Authorization",encodedString).accept(MediaType.APPLICATION_JSON).get(String.class);

        System.out.println(response);
    }
    //creates client object with any filters/interceptors registered with the config object (here null)
    static WebTarget getWebTarget(String extension) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);

        //returns baseURI (i.e. servlet URI) as target URI
        return client.target(baseURI+extension);
    }
}
