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

/**
 * File permission.
 */
@ApiModel(description = "File permission.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-02T17:31:35.366+02:00")
public class FilePermission {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("isGroup")
  private Boolean isGroup = null;

  /**
   * The permission.
   */
  public enum PermissionEnum {
    NUMBER_0(0),
    
    NUMBER_1(1),
    
    NUMBER_2(2),
    
    NUMBER_4(4),
    
    NUMBER_99(99),
    
    NUMBER_MINUS_1(-1);

    private Integer value;

    PermissionEnum(Integer value) {
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
    public static PermissionEnum fromValue(String text) {
      for (PermissionEnum b : PermissionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("permission")
  private PermissionEnum permission = null;

  /**
   * The permission.
   */
  public enum ParentPermissionEnum {
    NUMBER_0(0),
    
    NUMBER_1(1),
    
    NUMBER_2(2),
    
    NUMBER_4(4),
    
    NUMBER_99(99),
    
    NUMBER_MINUS_1(-1);

    private Integer value;

    ParentPermissionEnum(Integer value) {
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
    public static ParentPermissionEnum fromValue(String text) {
      for (ParentPermissionEnum b : ParentPermissionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("parentPermission")
  private ParentPermissionEnum parentPermission = null;

  public FilePermission id(String id) {
    this.id = id;
    return this;
  }

   /**
   * The user/group id.
   * @return id
  **/
  @ApiModelProperty(value = "The user/group id.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public FilePermission isGroup(Boolean isGroup) {
    this.isGroup = isGroup;
    return this;
  }

   /**
   * Indicates if the permission is set for a user or group.
   * @return isGroup
  **/
  @ApiModelProperty(value = "Indicates if the permission is set for a user or group.")
  public Boolean isIsGroup() {
    return isGroup;
  }

  public void setIsGroup(Boolean isGroup) {
    this.isGroup = isGroup;
  }

  public FilePermission permission(PermissionEnum permission) {
    this.permission = permission;
    return this;
  }

   /**
   * The permission.
   * @return permission
  **/
  @ApiModelProperty(value = "The permission.")
  public PermissionEnum getPermission() {
    return permission;
  }

  public void setPermission(PermissionEnum permission) {
    this.permission = permission;
  }

  public FilePermission parentPermission(ParentPermissionEnum parentPermission) {
    this.parentPermission = parentPermission;
    return this;
  }

   /**
   * The permission.
   * @return parentPermission
  **/
  @ApiModelProperty(value = "The permission.")
  public ParentPermissionEnum getParentPermission() {
    return parentPermission;
  }

  public void setParentPermission(ParentPermissionEnum parentPermission) {
    this.parentPermission = parentPermission;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilePermission filePermission = (FilePermission) o;
    return Objects.equals(this.id, filePermission.id) &&
        Objects.equals(this.isGroup, filePermission.isGroup) &&
        Objects.equals(this.permission, filePermission.permission) &&
        Objects.equals(this.parentPermission, filePermission.parentPermission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, isGroup, permission, parentPermission);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilePermission {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isGroup: ").append(toIndentedString(isGroup)).append("\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
    sb.append("    parentPermission: ").append(toIndentedString(parentPermission)).append("\n");
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

