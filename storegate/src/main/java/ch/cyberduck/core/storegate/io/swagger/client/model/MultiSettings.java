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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * A accounts multi settings. Properties that are null/undefined/missing are not available
 */
@ApiModel(description = "A accounts multi settings. Properties that are null/undefined/missing are not available")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-02T17:31:35.366+02:00")
public class MultiSettings {
  @JsonProperty("versionsAvailable")
  private List<Integer> versionsAvailable = null;

  @JsonProperty("idProvider")
  private String idProvider = null;

  @JsonProperty("idProviderCode")
  private String idProviderCode = null;

  @JsonProperty("officeOnline")
  private Boolean officeOnline = null;

  @JsonProperty("recycleBin")
  private Boolean recycleBin = null;

  @JsonProperty("versions")
  private Integer versions = null;

  /**
   * Permission for common root
   */
  public enum CommonRootPermissionEnum {
    NUMBER_0(0),
    
    NUMBER_1(1),
    
    NUMBER_2(2),
    
    NUMBER_4(4),
    
    NUMBER_99(99),
    
    NUMBER_MINUS_1(-1);

    private Integer value;

    CommonRootPermissionEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CommonRootPermissionEnum fromValue(String text) {
      for (CommonRootPermissionEnum b : CommonRootPermissionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("commonRootPermission")
  private CommonRootPermissionEnum commonRootPermission = null;

  @JsonProperty("extendedPermissions")
  private Boolean extendedPermissions = null;

  @JsonProperty("allowShare")
  private Boolean allowShare = null;

  public MultiSettings versionsAvailable(List<Integer> versionsAvailable) {
    this.versionsAvailable = versionsAvailable;
    return this;
  }

  public MultiSettings addVersionsAvailableItem(Integer versionsAvailableItem) {
    if (this.versionsAvailable == null) {
      this.versionsAvailable = new ArrayList<Integer>();
    }
    this.versionsAvailable.add(versionsAvailableItem);
    return this;
  }

   /**
   * Lists the available settings for versioning.
   * @return versionsAvailable
  **/
  @ApiModelProperty(value = "Lists the available settings for versioning.")
  public List<Integer> getVersionsAvailable() {
    return versionsAvailable;
  }

  public void setVersionsAvailable(List<Integer> versionsAvailable) {
    this.versionsAvailable = versionsAvailable;
  }

  public MultiSettings idProvider(String idProvider) {
    this.idProvider = idProvider;
    return this;
  }

   /**
   * Available identity provider for SSO/Provisioning
   * @return idProvider
  **/
  @ApiModelProperty(value = "Available identity provider for SSO/Provisioning")
  public String getIdProvider() {
    return idProvider;
  }

  public void setIdProvider(String idProvider) {
    this.idProvider = idProvider;
  }

  public MultiSettings idProviderCode(String idProviderCode) {
    this.idProviderCode = idProviderCode;
    return this;
  }

   /**
   * Code for identity provider
   * @return idProviderCode
  **/
  @ApiModelProperty(value = "Code for identity provider")
  public String getIdProviderCode() {
    return idProviderCode;
  }

  public void setIdProviderCode(String idProviderCode) {
    this.idProviderCode = idProviderCode;
  }

  public MultiSettings officeOnline(Boolean officeOnline) {
    this.officeOnline = officeOnline;
    return this;
  }

   /**
   * Enable Office Online for entire subscription
   * @return officeOnline
  **/
  @ApiModelProperty(value = "Enable Office Online for entire subscription")
  public Boolean isOfficeOnline() {
    return officeOnline;
  }

  public void setOfficeOnline(Boolean officeOnline) {
    this.officeOnline = officeOnline;
  }

  public MultiSettings recycleBin(Boolean recycleBin) {
    this.recycleBin = recycleBin;
    return this;
  }

   /**
   * Indicates if the recycle bin is enabled.
   * @return recycleBin
  **/
  @ApiModelProperty(value = "Indicates if the recycle bin is enabled.")
  public Boolean isRecycleBin() {
    return recycleBin;
  }

  public void setRecycleBin(Boolean recycleBin) {
    this.recycleBin = recycleBin;
  }

  public MultiSettings versions(Integer versions) {
    this.versions = versions;
    return this;
  }

   /**
   * Number of versions keept in versioning.
   * @return versions
  **/
  @ApiModelProperty(value = "Number of versions keept in versioning.")
  public Integer getVersions() {
    return versions;
  }

  public void setVersions(Integer versions) {
    this.versions = versions;
  }

  public MultiSettings commonRootPermission(CommonRootPermissionEnum commonRootPermission) {
    this.commonRootPermission = commonRootPermission;
    return this;
  }

   /**
   * Permission for common root
   * @return commonRootPermission
  **/
  @ApiModelProperty(value = "Permission for common root")
  public CommonRootPermissionEnum getCommonRootPermission() {
    return commonRootPermission;
  }

  public void setCommonRootPermission(CommonRootPermissionEnum commonRootPermission) {
    this.commonRootPermission = commonRootPermission;
  }

  public MultiSettings extendedPermissions(Boolean extendedPermissions) {
    this.extendedPermissions = extendedPermissions;
    return this;
  }

   /**
   * Extended permissions in common
   * @return extendedPermissions
  **/
  @ApiModelProperty(value = "Extended permissions in common")
  public Boolean isExtendedPermissions() {
    return extendedPermissions;
  }

  public void setExtendedPermissions(Boolean extendedPermissions) {
    this.extendedPermissions = extendedPermissions;
  }

  public MultiSettings allowShare(Boolean allowShare) {
    this.allowShare = allowShare;
    return this;
  }

   /**
   * Allow share for all users
   * @return allowShare
  **/
  @ApiModelProperty(value = "Allow share for all users")
  public Boolean isAllowShare() {
    return allowShare;
  }

  public void setAllowShare(Boolean allowShare) {
    this.allowShare = allowShare;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultiSettings multiSettings = (MultiSettings) o;
    return Objects.equals(this.versionsAvailable, multiSettings.versionsAvailable) &&
        Objects.equals(this.idProvider, multiSettings.idProvider) &&
        Objects.equals(this.idProviderCode, multiSettings.idProviderCode) &&
        Objects.equals(this.officeOnline, multiSettings.officeOnline) &&
        Objects.equals(this.recycleBin, multiSettings.recycleBin) &&
        Objects.equals(this.versions, multiSettings.versions) &&
        Objects.equals(this.commonRootPermission, multiSettings.commonRootPermission) &&
        Objects.equals(this.extendedPermissions, multiSettings.extendedPermissions) &&
        Objects.equals(this.allowShare, multiSettings.allowShare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(versionsAvailable, idProvider, idProviderCode, officeOnline, recycleBin, versions, commonRootPermission, extendedPermissions, allowShare);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultiSettings {\n");
    
    sb.append("    versionsAvailable: ").append(toIndentedString(versionsAvailable)).append("\n");
    sb.append("    idProvider: ").append(toIndentedString(idProvider)).append("\n");
    sb.append("    idProviderCode: ").append(toIndentedString(idProviderCode)).append("\n");
    sb.append("    officeOnline: ").append(toIndentedString(officeOnline)).append("\n");
    sb.append("    recycleBin: ").append(toIndentedString(recycleBin)).append("\n");
    sb.append("    versions: ").append(toIndentedString(versions)).append("\n");
    sb.append("    commonRootPermission: ").append(toIndentedString(commonRootPermission)).append("\n");
    sb.append("    extendedPermissions: ").append(toIndentedString(extendedPermissions)).append("\n");
    sb.append("    allowShare: ").append(toIndentedString(allowShare)).append("\n");
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

