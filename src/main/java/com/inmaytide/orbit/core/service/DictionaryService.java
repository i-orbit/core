package com.inmaytide.orbit.core.service;

import com.inmaytide.orbit.commons.business.BasicService;
import com.inmaytide.orbit.commons.domain.dto.result.TreeNode;
import com.inmaytide.orbit.core.domain.Dictionary;

import java.util.List;
import java.util.Map;

/**
 * @author inmaytide
 * @since 2024/4/16
 */
public interface DictionaryService extends BasicService<Dictionary> {

    String ROOT_CODE = "ROOT";

    String SYMBOL = "DICTIONARY";

    List<TreeNode<Dictionary>> treeByCategory(String category);

    List<Dictionary> listByCategory(String category);

    Map<String, String> findNamesByCodes(List<String> codes);
}
