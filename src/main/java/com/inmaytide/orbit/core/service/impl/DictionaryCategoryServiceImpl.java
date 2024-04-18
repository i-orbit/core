package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inmaytide.exception.web.ObjectNotFoundException;
import com.inmaytide.orbit.commons.business.impl.BasicServiceImpl;
import com.inmaytide.orbit.commons.constants.Bool;
import com.inmaytide.orbit.core.configuration.ErrorCode;
import com.inmaytide.orbit.core.domain.DictionaryCategory;
import com.inmaytide.orbit.core.mapper.DictionaryCategoryMapper;
import com.inmaytide.orbit.core.service.DictionaryCategoryService;
import com.inmaytide.orbit.core.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/16
 */
@Service
public class DictionaryCategoryServiceImpl extends BasicServiceImpl<DictionaryCategoryMapper, DictionaryCategory> implements DictionaryCategoryService {

    private DictionaryService dictionaryService;

    @Override
    public List<DictionaryCategory> all() {
        LambdaQueryWrapper<DictionaryCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictionaryCategory::getDisabled, Bool.N);
        wrapper.orderByAsc(DictionaryCategory::getSequence);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public DictionaryCategory findByCode(String code) {
        LambdaQueryWrapper<DictionaryCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictionaryCategory::getDisabled, Bool.N);
        wrapper.eq(DictionaryCategory::getCode, code);
        DictionaryCategory category = baseMapper.selectOne(wrapper);
        if (category == null) {
            throw new ObjectNotFoundException(ErrorCode.E_0x00300009, code);
        }
        category.setDictionaries(dictionaryService.treeByCategory(code));
        return category;
    }

    @Lazy
    @Autowired
    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
