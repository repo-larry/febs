package com.febs.system.properties;

import lombok.Data;

/**
 * @description:
 * @date: 2022/9/25
 **/
@Data
public class FebsSwaggerProperties {

    private String basePackage;
    private String title;
    private String description;
    private String version;
    private String author;
    private String url;
    private String email;
    private String license;
    private String licenseUrl;

    private String grantUrl;
    private String name;
    private String scope;

}
