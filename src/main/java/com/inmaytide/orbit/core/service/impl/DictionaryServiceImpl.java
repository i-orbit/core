package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inmaytide.orbit.commons.business.impl.BasicServiceImpl;
import com.inmaytide.orbit.commons.domain.dto.result.TreeNode;
import com.inmaytide.orbit.core.domain.Dictionary;
import com.inmaytide.orbit.core.mapper.DictionaryMapper;
import com.inmaytide.orbit.core.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inmaytide
 * @since 2024/4/16
 */
@Service
public class DictionaryServiceImpl extends BasicServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    /**
     * 解决通过this调用内部方法无法触发AOP相关功能问题
     */
    private DictionaryService self;

    @Override
    public List<TreeNode<Dictionary>> treeByCategory(String category) {
        List<Dictionary> dictionaries = self.listByCategory(category);


        return null;
    }

    @Override
    @Cacheable(cacheNames = "DICTIONARIES_OF_CATEGORY", key = "#category")
    public List<Dictionary> listByCategory(String category) {
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dictionary::getCategory, category);
        wrapper.orderByAsc(Dictionary::getSequence);
        return baseMapper.selectList(wrapper);
    }

    @Lazy
    @Autowired
    public void setSelf(DictionaryService self) {
        this.self = self;
    }

}
