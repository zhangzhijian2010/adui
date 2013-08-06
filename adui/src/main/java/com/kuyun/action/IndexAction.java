package com.kuyun.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.kuyun.common.action.BaseAction;
import com.kuyun.common.json.JsonControllerHelper;

@Controller
@Scope("prototype")
public class IndexAction extends BaseAction {
    private static final long serialVersionUID = 1L;

    /**
     * 跳转到主页
     * @return
     */
    public String index() throws Exception {
        return dispatcher("/jsp/home.jsp");
    }

    private String node;

    /**
     * @return the node
     */
    public String getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(String node) {
        this.node = node;
    }

    public String menu() throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<String> menus = Files.readLines(new File(IndexAction.class.getResource("/menu/menu")
            .getPath()), Charsets.UTF_8);
        Collections.sort(menus, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1.split("\t")[4]) - Integer.parseInt(o2.split("\t")[4]);
            }
        });
        for (String menu : menus) {
            String[] menuInfo = menu.split("\t");
            Map<String, Object> map = new HashMap<String, Object>();
            // 原始模式
            //            map.put("id", menuInfo[0]);
            //            map.put("pId", menuInfo[2]);
            //            map.put("name", menuInfo[1]);
            //            map.put("url", menuInfo[3]);
            //            map.put("oNum", menuInfo[4]);
            //            list.add(map);

            // 树行模式
            if (menuInfo[2].equals(node)) {
                map.put("id", menuInfo[0]);
                map.put("leaf", "1".equals(menuInfo[5]));
                map.put("text", menuInfo[1]);
                map.put("url", menuInfo[3]);
                list.add(map);
            }
        }
        JsonControllerHelper.writeJsonToResponse(response, list);
        return null;

    }
}
