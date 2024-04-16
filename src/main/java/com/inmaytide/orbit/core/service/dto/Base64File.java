package com.inmaytide.orbit.core.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author inmaytide
 * @since 2024/4/7
 */
@Schema(title = "以base64编码的方式上传文件接口数据对象")
public class Base64File {

    @NotBlank
    @Schema(title = "文件内容", description = "Base64编码")
    private String file;

    @NotBlank
    @Schema(title = "文件名(包含文件扩展名)")
    private String name;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
