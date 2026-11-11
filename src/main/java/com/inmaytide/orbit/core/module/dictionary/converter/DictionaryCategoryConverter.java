package com.inmaytide.orbit.core.module.dictionary.converter;

import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategory;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategoryVO;
import org.mapstruct.Mapper;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Mapper(componentModel = "spring")
public interface DictionaryCategoryConverter {

    DictionaryCategoryVO toVO(DictionaryCategory dictionaryCategory);

}
