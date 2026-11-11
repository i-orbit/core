package com.inmaytide.orbit.core.module.dictionary.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Mapper
public interface DictionaryCategoryRepository extends BaseMapper<DictionaryCategory> {
}
