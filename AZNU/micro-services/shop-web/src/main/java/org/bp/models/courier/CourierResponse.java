/*
 * Courier service
 * Service for package delivery
 *
 * OpenAPI spec version: 1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.bp.models.courier;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * CourierResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-01-28T14:32:20.574+01:00[Europe/Belgrade]")
public class CourierResponse {
  @SerializedName("courierId")
  private Integer courierId = null;

  @SerializedName("receiveDate")
  private String receiveDate = null;

  @SerializedName("expectedDeliveryTime")
  private String expectedDeliveryTime = null;

  @SerializedName("cost")
  private Double cost = null;

  public CourierResponse courierId(Integer courierId) {
    this.courierId = courierId;
    return this;
  }

  /**
   * Get courierId
   * @return courierId
   **/

  public Integer getCourierId() {
    return courierId;
  }

  public void setCourierId(Integer courierId) {
    this.courierId = courierId;
  }

  public CourierResponse receiveDate(String receiveDate) {
    this.receiveDate = receiveDate;
    return this;
  }

  /**
   * Get receiveDate
   * @return receiveDate
   **/

  public String getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(String receiveDate) {
    this.receiveDate = receiveDate;
  }

  public CourierResponse expectedDeliveryTime(String expectedDeliveryTime) {
    this.expectedDeliveryTime = expectedDeliveryTime;
    return this;
  }

  /**
   * Get expectedDeliveryTime
   * @return expectedDeliveryTime
   **/

  public String getExpectedDeliveryTime() {
    return expectedDeliveryTime;
  }

  public void setExpectedDeliveryTime(String expectedDeliveryTime) {
    this.expectedDeliveryTime = expectedDeliveryTime;
  }

  public CourierResponse cost(Double cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   **/

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CourierResponse courierResponse = (CourierResponse) o;
    return Objects.equals(this.courierId, courierResponse.courierId) &&
            Objects.equals(this.receiveDate, courierResponse.receiveDate) &&
            Objects.equals(this.expectedDeliveryTime, courierResponse.expectedDeliveryTime) &&
            Objects.equals(this.cost, courierResponse.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(courierId, receiveDate, expectedDeliveryTime, cost);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CourierResponse {\n");

    sb.append("    courierId: ").append(toIndentedString(courierId)).append("\n");
    sb.append("    receiveDate: ").append(toIndentedString(receiveDate)).append("\n");
    sb.append("    expectedDeliveryTime: ").append(toIndentedString(expectedDeliveryTime)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
