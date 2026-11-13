package com.inmaytide.orbit.core.module.dictionary.converter;

import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryItem;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryItemVO;
import com.inmaytide.orbit.framework.core.domain.dto.TreeNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author inmaytide
 * @since 2026/11/11
 */
@Mapper(componentModel = "spring")
public interface DictionaryItemConverter {

    @Mapping(target = "categoryCode", source = "categoryCode")
    DictionaryItemVO toVO(DictionaryItem inst, String categoryCode);

    @Mapping(target = "target", source = "inst")
    @Mapping(target = "hasAuthority", source = "hasAuthority")
    TreeNode<DictionaryItemVO> toTreeNode(DictionaryItemVO inst, boolean hasAuthority);

}
