package org.bp.courier;

import org.bp.courier.model.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController

@OpenAPIDefinition(info = @Info(
        title = "Courier service",
        version = "1",
        description = "Service for package delivery"))

public class CourierService {
    private int courierNextId = 0;
    @org.springframework.web.bind.annotation.PostMapping("/courier")
    @Operation(
            summary = "courier operation",
            description = "operation for courier service",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourierResponse.class))}),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
            })
    public CourierResponse deliver(
            @org.springframework.web.bind.annotation.RequestBody CourierRequest courierRequest) {
        System.out.println("Activated delivery");
        if (courierRequest==null
                || courierRequest.getPerson()==null
                || courierRequest.getPerson().getAddress()==null
                || courierRequest.getCourierType()==null){

            throw new CourierException("Insufficient data. Unable to delivery");
        }

        if(!CourierTypes.getInstance().getTypes().contains(courierRequest.getCourierType())){
            throw new CourierException("Unrecognized courier type. Available are 'courier' or 'locker'. Got " + courierRequest.getCourierType());
        }

        CourierResponse courierResponse = new CourierResponse();
        courierResponse.setReceiveDate(new Date());
        courierResponse.setCourierId(courierNextId);
        courierNextId += 1;

        Random random = new Random();
        Calendar deliveryTime = Calendar.getInstance();
        deliveryTime.setTime(new Date());
        deliveryTime.add(Calendar.DATE, random.nextInt(12)+2);
        courierResponse.setExpectedDeliveryTime(deliveryTime.getTime());

        return courierResponse;
    }
    @org.springframework.web.bind.annotation.PostMapping("/courier/types")
    @Operation(
            summary = "payment operation",
            description = "operation for payment",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourierGetTypesResponse.class))}),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
            })
    public CourierGetTypesResponse getCourierTypes() {
        return new CourierGetTypesResponse();
    }

    @org.springframework.web.bind.annotation.PostMapping("/courier/cancel")
    @Operation(
            summary = "cancel courier operation",
            description = "cancel courier",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))}),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
            })
    public Integer cancelCourier(@RequestParam(name = "courierId") Integer courierId) {
        if(courierId == null) {
            throw new CourierException("Courier id required");
        }

        if(courierId < 0) {
            throw new CourierException("Courier id less than 0.");
        }

        return 1;
    }

}
