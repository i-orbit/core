package com.inmaytide.orbit.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.inmaytide.orbit.commons.business.impl.BasicServiceImpl;
import com.inmaytide.orbit.commons.domain.SystemUser;
import com.inmaytide.orbit.commons.domain.dto.result.TreeNode;
import com.inmaytide.orbit.commons.security.SecurityUtils;
import com.inmaytide.orbit.core.domain.Dictionary;
import com.inmaytide.orbit.core.mapper.DictionaryMapper;
import com.inmaytide.orbit.core.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<String, Dictionary> dictionaries = self.listByCategory(category).stream().collect(Collectors.toMap(Dictionary::getCode, Function.identity(), (a, b) -> b, TreeMap::new));
        List<String> requiredCodes = getRequiredCodes(dictionaries);
        return findTreeNodes(ROOT_CODE, 1, dictionaries, requiredCodes);
    }

    private List<TreeNode<Dictionary>> findTreeNodes(Serializable parentCode, int level, Map<String, Dictionary> all, List<String> requiredCodes) {
        return all.values().stream()
                .filter(e -> Objects.equals(e.getParent(), parentCode))
                .filter(e -> requiredCodes.contains(e.getCode()))
                .map(e -> toTreeNode(e, level, all, requiredCodes))
                .toList();
    }

    private TreeNode<Dictionary> toTreeNode(Dictionary dictionary, int level, Map<String, Dictionary> all, List<String> requiredCodes) {
        TreeNode<Dictionary> node = new TreeNode<>();
        node.setId(dictionary.getCode());
        node.setSymbol(SYMBOL);
        node.setLevel(level);
        node.setName(dictionary.getName());
        node.setParent(dictionary.getParent());
        node.setEntity(dictionary);
        node.setChildren(findTreeNodes(dictionary.getCode(), level + 1, all, requiredCodes));
        node.setAuthorized(dictionary.isAuthorized(SecurityUtils.getAuthorizedUser()));
        return node;
    }

    /**
     * 查询当前用户所有有权限的数据字典数据从根节点开始构成一个树结构集合需要的所有数据字典编码集合
     *
     * @param all 所有数据字典集合
     */
    private List<String> getRequiredCodes(Map<String, Dictionary> all) {
        SystemUser user = SecurityUtils.getAuthorizedUser();
        return all.values().parallelStream()
                .filter(e -> e.isAuthorized(user))
                .map(Dictionary::getCode)
                .map(code -> getChainCodes(code, all, new ArrayList<>()))
                .flatMap(Collection::stream)
                .distinct()
                .toList();
    }

    /**
     * 查询某个组织的父组织id链
     *
     * @param code 需要查询的数据字典编码
     * @param all  所有数据字典集合
     * @param res  查询结果容器
     */
    private List<String> getChainCodes(String code, Map<String, Dictionary> all, List<String> res) {
        res.add(code);
        Dictionary dictionary = all.get(code);
        if (dictionary != null) {
            if (ROOT_CODE.equals(dictionary.getParent())) {
                res.add(dictionary.getCode());
            } else {
                getChainCodes(dictionary.getParent(), all, res);
            }
        }
        return res;
    }


    @Override
    @Cacheable(cacheNames = "DICTIONARIES_OF_CATEGORY", key = "#category")
    public List<Dictionary> listByCategory(String category) {
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dictionary::getCategory, category);
        wrapper.orderByAsc(Dictionary::getSequence);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, String> findNamesByCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Dictionary::getName, Dictionary::getCode);
        wrapper.in(Dictionary::getCode, codes);
        return getBaseMapper().selectList(wrapper).stream().collect(Collectors.toMap(Dictionary::getCode, Dictionary::getName));
    }

    @Lazy
    @Autowired
    public void setSelf(DictionaryService self) {
        this.self = self;
    }

}
