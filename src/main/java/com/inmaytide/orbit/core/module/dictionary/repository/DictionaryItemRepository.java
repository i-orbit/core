package com.inmaytide.orbit.core.module.dictionary.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author inmaytide
 * @since 2026/11/11
 */
@Mapper
public interface DictionaryItemRepository extends BaseMapper<DictionaryItem> {
}
