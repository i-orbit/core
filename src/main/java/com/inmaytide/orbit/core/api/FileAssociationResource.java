package com.inmaytide.orbit.core.api;

import com.inmaytide.orbit.commons.domain.dto.result.AffectedResult;
import com.inmaytide.orbit.commons.domain.validation.groups.Add;
import com.inmaytide.orbit.commons.domain.validation.groups.Delete;
import com.inmaytide.orbit.core.domain.FileAssociation;
import com.inmaytide.orbit.core.service.FileAssociationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author inmaytide
 * @since 2024/5/15
 */
@RestController
@Tag(name = "文件库文件交换接口")
@RequestMapping("/api/files/associations")
public class FileAssociationResource {

    private final FileAssociationService fileAssociationService;

    public FileAssociationResource(FileAssociationService fileAssociationService) {
        this.fileAssociationService = fileAssociationService;
    }

    @PostMapping
    @Operation(summary = "创建业务对象对文件库文件的引用记录")
    public FileAssociation create(@RequestBody @Validated(Add.class) FileAssociation entity) {
        return fileAssociationService.create(entity);
    }

    @DeleteMapping
    @Operation(summary = "移除业务对象对文件库文件的引用记录")
    public AffectedResult delete(@RequestBody @Validated(Delete.class) FileAssociation entity) {
        return fileAssociationService.delete(entity);
    }

}
