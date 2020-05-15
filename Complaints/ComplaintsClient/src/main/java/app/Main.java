/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static java.lang.System.in;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.stream.JsonParser;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 *
 * @author Konto
 */
public class Main {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget main_site = client.target("http://localhost:8080/Complaints/"
                + "resources/complaints");
        String count = main_site.path("count").request(MediaType.TEXT_PLAIN)
                .get(String.class);
        // Count
        System.out.println("Count: " + count);

        // Print all complaints
        System.out.println("All complaints:" + get(main_site, "", 0) + "\n\n");
        
        // Print complaints with id
        Scanner in = new Scanner(System.in);
        System.out.println("Which of complaints to show? Provide id:");
        String complaint_id = in.nextLine();
        System.out.println("Complaint:" + get(main_site, "", 
                Integer.parseInt(complaint_id)) + "\n\n");
       
        // Change status of a complaint with id
        System.out.println("Changing complaint(id: "+complaint_id+
                ") status to closed \n\n");
        System.out.println("Status: "+ Integer.toString(
                modifyStatus(main_site, "closed", Integer.parseInt(complaint_id)))
                + "\n\n");
      
        // Print all complaints
        System.out.println("All complaints:" + get(main_site, "", 0) + "\n\n");
        
        // Print all closed complaints
         System.out.println("All closed complaints:" +
                 get(main_site, "closed", 0) + 
                 "\n\n");
      
        
        // Print

        client.close();
    }

    public static String get(WebTarget main, String status, int id) {
        /*Filter: 0-all, 1-open, 2-closed */
 /*id: if 0 then get all else get given message by id*/
        if (id > 0) {
                main = main.path(Integer.toString(id));
            }
        if (!"".equals(status)) {
            main = main.queryParam("status", status);
        } 
        return main.request(MediaType.APPLICATION_JSON).get(String.class);
    }

    public static int modifyStatus(WebTarget main, String status, int id) {
        Complaint complaint = main.path(Integer.toString(id)).
                request(MediaType.APPLICATION_JSON_TYPE).
                get(Complaint.class);
      
       complaint.setStatus(status);
       Response response = main.path(Integer.toString(id))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(complaint, MediaType.APPLICATION_JSON_TYPE));
        
        return response.getStatus();
    }
}
