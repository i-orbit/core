package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.commons.business.BasicService;
import com.inmaytide.orbit.commons.domain.dto.result.AffectedResult;
import com.inmaytide.orbit.core.domain.FileAssociation;

/**
 * @author inmaytide
 * @since 2024/5/15
 */
public interface FileAssociationService extends BasicService<FileAssociation> {

    AffectedResult delete(FileAssociation entity);

}
