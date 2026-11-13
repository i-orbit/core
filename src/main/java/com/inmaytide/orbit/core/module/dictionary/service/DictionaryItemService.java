package com.inmaytide.orbit.core.module.dictionary.service;

import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryCategory;
import com.inmaytide.orbit.core.module.dictionary.domain.DictionaryItemVO;
import com.inmaytide.orbit.framework.core.domain.dto.TreeNode;

import java.util.List;

/**
 * @author inmaytide
 * @since 2026/11/11
 */
public interface DictionaryItemService {

    String RESOURCE_TYPE = "Dictionary";

    List<TreeNode<DictionaryItemVO>> getTree(DictionaryCategory category);

}
