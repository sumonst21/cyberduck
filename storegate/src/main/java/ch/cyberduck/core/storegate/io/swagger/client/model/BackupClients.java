/*
 * Storegate.Web
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v4
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package ch.cyberduck.core.storegate.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import ch.cyberduck.core.storegate.io.swagger.client.model.BackupClient;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-02T17:31:35.366+02:00")
public class BackupClients {
  @JsonProperty("clients")
  private List<BackupClient> clients = null;

  @JsonProperty("numberOfLicenses")
  private Integer numberOfLicenses = null;

  @JsonProperty("availableLicenses")
  private Integer availableLicenses = null;

  public BackupClients clients(List<BackupClient> clients) {
    this.clients = clients;
    return this;
  }

  public BackupClients addClientsItem(BackupClient clientsItem) {
    if (this.clients == null) {
      this.clients = new ArrayList<BackupClient>();
    }
    this.clients.add(clientsItem);
    return this;
  }

   /**
   * List of used clients
   * @return clients
  **/
  @ApiModelProperty(value = "List of used clients")
  public List<BackupClient> getClients() {
    return clients;
  }

  public void setClients(List<BackupClient> clients) {
    this.clients = clients;
  }

  public BackupClients numberOfLicenses(Integer numberOfLicenses) {
    this.numberOfLicenses = numberOfLicenses;
    return this;
  }

   /**
   * Number of licenses
   * @return numberOfLicenses
  **/
  @ApiModelProperty(value = "Number of licenses")
  public Integer getNumberOfLicenses() {
    return numberOfLicenses;
  }

  public void setNumberOfLicenses(Integer numberOfLicenses) {
    this.numberOfLicenses = numberOfLicenses;
  }

  public BackupClients availableLicenses(Integer availableLicenses) {
    this.availableLicenses = availableLicenses;
    return this;
  }

   /**
   * Number of available licenses
   * @return availableLicenses
  **/
  @ApiModelProperty(value = "Number of available licenses")
  public Integer getAvailableLicenses() {
    return availableLicenses;
  }

  public void setAvailableLicenses(Integer availableLicenses) {
    this.availableLicenses = availableLicenses;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BackupClients backupClients = (BackupClients) o;
    return Objects.equals(this.clients, backupClients.clients) &&
        Objects.equals(this.numberOfLicenses, backupClients.numberOfLicenses) &&
        Objects.equals(this.availableLicenses, backupClients.availableLicenses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clients, numberOfLicenses, availableLicenses);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BackupClients {\n");
    
    sb.append("    clients: ").append(toIndentedString(clients)).append("\n");
    sb.append("    numberOfLicenses: ").append(toIndentedString(numberOfLicenses)).append("\n");
    sb.append("    availableLicenses: ").append(toIndentedString(availableLicenses)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

