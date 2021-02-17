package org.bp.payment;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bp.payment.model.PaymentException;
import org.bp.payment.model.PaymentRequest;
import org.bp.payment.model.PaymentResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController

@OpenAPIDefinition(info = @Info(
        title = "Payment service",
        version = "1",
        description = "Service for payment"))

public class PaymentService {
		private Integer transactionNextId;

		public PaymentService() {
			transactionNextId = 0;
		}


		@org.springframework.web.bind.annotation.PostMapping("/payment")
	    @Operation(
	            summary = "payment operation",
	            description = "operation for payment with card or by blik code",
	            responses = {
	                @ApiResponse(responseCode = "200",
	                        description = "OK",
	                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))}),
	                @ApiResponse(responseCode = "400", description = "Bad Request",
	                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
	            })
		public PaymentResponse payment(
				@org.springframework.web.bind.annotation.RequestBody PaymentRequest paymentRequest
		) {
			if (paymentRequest!=null && paymentRequest.getAmount()!=null 
					&& paymentRequest.getAmount().getValue()!=null
					&& paymentRequest.getAmount().getValue().compareTo(new BigDecimal(0))<0) {
				throw new PaymentException("Amount value can not be negative");
			}
			if (paymentRequest != null
					&& ((paymentRequest.getPaymentCard()==null && paymentRequest.getPaymentBlik() == null)
					 || (paymentRequest.getPaymentCard()!=null && paymentRequest.getPaymentBlik() != null) )) {
				throw new PaymentException("Payment must be done by card or by blik code.");
			}

			if(paymentRequest!=null && paymentRequest.getAmount()!=null &&
					paymentRequest.getAmount().getValue().compareTo(new BigDecimal(1000)) > 0) {
				throw new PaymentException("Insufficient funds on bank account");
			}
				
			PaymentResponse paymentResponse = new PaymentResponse();
			paymentResponse.setTransactionDate(new Date().toString());
			paymentResponse.setTransactionId(transactionNextId);
			transactionNextId += 1;
			return paymentResponse;
		}

}
