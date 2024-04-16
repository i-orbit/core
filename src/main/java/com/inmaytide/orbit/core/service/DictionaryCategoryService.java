package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.commons.business.BasicService;
import com.inmaytide.orbit.core.domain.DictionaryCategory;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/16
 */
public interface DictionaryCategoryService extends BasicService<DictionaryCategory> {

    List<DictionaryCategory> all();

    DictionaryCategory findByCode(String code);

}
