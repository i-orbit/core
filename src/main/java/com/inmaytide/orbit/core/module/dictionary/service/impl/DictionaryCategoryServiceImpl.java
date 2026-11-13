package com.inmaytide.orbit.core.module.dictionary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inmaytide.orbit.core.module.dictionary.converter.DictionaryCategoryConverter;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategory;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategoryVO;
import com.inmaytide.orbit.core.module.dictionary.repository.DictionaryCategoryRepository;
import com.inmaytide.orbit.core.module.dictionary.service.DictionaryCategoryService;
import com.inmaytide.orbit.core.module.dictionary.service.DictionaryItemService;
import com.inmaytide.orbit.framework.core.enums.EnableStatus;
import com.inmaytide.orbit.framework.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2026/11/6
 */
@Service
public class DictionaryCategoryServiceImpl implements DictionaryCategoryService {

    private final DictionaryCategoryRepository repository;

    private final DictionaryCategoryConverter converter;

    private final DictionaryItemService itemService;

    public DictionaryCategoryServiceImpl(DictionaryCategoryRepository repository, DictionaryCategoryConverter converter, DictionaryItemService itemService) {
        this.repository = repository;
        this.converter = converter;
        this.itemService = itemService;
    }

    @Override
    public List<DictionaryCategoryVO> listAll() {
        LambdaQueryWrapper<DictionaryCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DictionaryCategory::getDisplayOrder);
        if (!SecurityUtils.isSuperAdministrator()) {
            wrapper.eq(DictionaryCategory::getStatus, EnableStatus.ENABLED.getValue());
        }
        return repository.selectList(wrapper).stream().map(converter::toVO).toList();
    }

    @Override
    public Optional<DictionaryCategoryVO> getByCode(String code) {
        List<DictionaryCategory> list = repository.selectList(new LambdaQueryWrapper<DictionaryCategory>().eq(DictionaryCategory::getCode, code));
        if (list.isEmpty()) {
            return Optional.empty();
        }
        DictionaryCategoryVO res = converter.toVO(list.getFirst());
        res.setItems(itemService.getTree(list.getFirst()));
        return Optional.of(res);
    }

}
