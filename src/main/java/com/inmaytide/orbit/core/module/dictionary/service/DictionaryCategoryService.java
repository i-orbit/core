package com.inmaytide.orbit.core.module.dictionary.service;

import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategoryVO;

import java.util.List;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
public interface DictionaryCategoryService {

    List<DictionaryCategoryVO> findAll();

}
