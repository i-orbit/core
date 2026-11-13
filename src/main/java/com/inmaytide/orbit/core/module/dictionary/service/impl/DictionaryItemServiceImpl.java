package com.inmaytide.orbit.core.module.dictionary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inmaytide.orbit.core.module.dictionary.converter.DictionaryItemConverter;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategory;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryItem;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryItemVO;
import com.inmaytide.orbit.core.module.dictionary.repository.DictionaryItemRepository;
import com.inmaytide.orbit.core.module.dictionary.service.DictionaryItemService;
import com.inmaytide.orbit.framework.core.domain.dto.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author inmaytide
 * @since 2026/11/11
 */
@Service
public class DictionaryItemServiceImpl implements DictionaryItemService {

    private final DictionaryItemRepository repository;

    private final DictionaryItemConverter converter;

    public DictionaryItemServiceImpl(DictionaryItemRepository repository, DictionaryItemConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    private List<DictionaryItem> listByCategory(DictionaryCategory category) {
        LambdaQueryWrapper<DictionaryItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictionaryItem::getCategoryId, Objects.requireNonNull(category).getId());
        wrapper.orderByAsc(DictionaryItem::getDisplayOrder);
        return repository.selectList(wrapper);
    }

    @Override
    public List<TreeNode<DictionaryItemVO>> getTree(DictionaryCategory category) {
        Objects.requireNonNull(category);
        List<DictionaryItemVO> items = listByCategory(category).stream().map(x -> converter.toVO(x, category.getCode())).toList();
        Map<String, TreeNode<DictionaryItemVO>> nodeMap = new HashMap<>(items.size());
        for (DictionaryItemVO item : items) {
            nodeMap.put(item.getId(), converter.toTreeNode(item, true));
        }
        List<TreeNode<DictionaryItemVO>> res = new ArrayList<>();
        for (DictionaryItemVO item : items) {
            String parentId = item.getParentId();
            if (StringUtils.isBlank(parentId)) {
                res.add(nodeMap.get(item.getId()));
                continue;
            }
            TreeNode<DictionaryItemVO> parent = nodeMap.get(parentId);
            if (parent != null) {
                parent.getChildren().add(nodeMap.get(item.getId()));
            } else {
                res.add(nodeMap.get(item.getId()));
            }
        }
        return res;
    }
}
