package org.bp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bp.models.*;
import org.bp.models.shop.Notification;
import org.bp.models.shop.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopController {

    HashMap<Integer, Notification> notes = new HashMap<>();

    @org.springframework.beans.factory.annotation.Value("${shop.gateway.service}")
    private String shopGatewayService;

    @GetMapping("/shop")
    public String shopForm(Model model) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> re = restTemplate.getForEntity(
                "http://"+shopGatewayService+"/api/shop/items", String.class);

        String body = re.getBody();
        System.out.println(body);

        ObjectMapper mapper = new ObjectMapper();
        StorageItemList list = mapper.readValue(body, StorageItemList.class);
        List<StorageItemWrapper> items = new ArrayList<>();
        list.getItems().forEach(item -> {
            StorageItemWrapper itemData = new StorageItemWrapper();
            itemData.setItem(item);
            itemData.setSize(0);
            items.add(itemData);
        });

        ShopRequestWrapper shopRequestWrapper = new ShopRequestWrapper();
        shopRequestWrapper.setItems(items);
        model.addAttribute("request", shopRequestWrapper);
        return "shop";
    }


    @PostMapping("/shop/result")
    public ModelAndView processBasket(@ModelAttribute ShopRequestWrapper request) throws IOException {

        ShopRequest shopRequest = new ShopRequest();
        
        shopRequest.setPerson(request.getPerson());
        shopRequest.setCourierType(request.getCourierType());
        
        List<StorageItem> items = new ArrayList<>();
        request.getItems().forEach(
                storageItemWrapper -> {
                    storageItemWrapper.getItem().setCount(storageItemWrapper.getSize());
                    items.add(storageItemWrapper.getItem());
                }
        );
        
        shopRequest.setItems(items);
        String courierType = request.getPaymentType();

        if(courierType.equals("blikCode")) {
            shopRequest.setPaymentBlik(request.getPaymentBlik());
        }
        else {
            shopRequest.setPaymentCard(request.getPaymentCard());
        }

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> re = restTemplate.postForEntity(
                "http://"+shopGatewayService+"/api/shop/items", shopRequest, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Response shopResponse = mapper.readValue(re.getBody(), Response.class);

        System.out.println(shopResponse.getBasketId());
        ResponseEntity<String> notificationJSON = restTemplate.getForEntity(
                "http://"+shopGatewayService+"/api/shop/result/"+shopResponse.getBasketId(), String.class);


        Notification notification = mapper.readValue(notificationJSON.getBody(), Notification.class);

        notes.put(shopResponse.getBasketId(), notification);
        if(notification.getErrors() == null || notification.getErrors().isEmpty()) {
           return new ModelAndView("redirect:/result/"+shopResponse.getBasketId());
        }
        else {
            return new ModelAndView("redirect:/error/"+shopResponse.getBasketId());
        }

    }


    @GetMapping("/result/{id}")
    public String result(@PathVariable(name = "id") Integer id, ModelMap model) {
        model.addAttribute("note", notes.get(id));
        return "/result";
    }

    @GetMapping("/error/{id}")
    public String error(@PathVariable(name = "id") Integer id, ModelMap model) {
        model.addAttribute("note", notes.get(id));
        return "/error";
    }

}
