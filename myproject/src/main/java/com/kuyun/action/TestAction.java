package com.kuyun.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kuyun.common.action.BaseAction;
import com.kuyun.common.json.JsonControllerHelper;
import com.kuyun.common.util.PageModel;

@SuppressWarnings("serial")
/**
 * 
 * 控制器类必须有
 * @Controller
 * @Scope("prototype")注解
 *
 */
@Controller
@Scope("prototype")
public class TestAction extends BaseAction {
    /**
     * 调用的规则是：test_test_test.htm
     * 默认返回的界面是/test/test_test.jsp
     * 具体查看struts.xml配置
     * @return
     */
    public String test() {
        return DEFAULT;
    }

    public String load() throws Exception {
        System.out.println(testDate + "--->" + dynamicCombox + "--->" + staticCombox + "--->" + testChannel);
        List<Test> ts = new ArrayList<Test>();
        for (int i = getStart(); i < getStart() + getLimit(); i++) {
            Test t = new Test();
            t.setName("ttttttttt--------------->" + i);
            t.setAge(123);
            t.setIntros("intors---------------->" + i);
            ts.add(t);

        }
        int count = 10000;
        PageModel<Test> p = new PageModel<Test>(ts, count);
        JsonControllerHelper.writePageModelToResponse(response, p);
        return null;
    }
    /**
     * Combox查询过来的对象
     */
    private String query;
    
    private String testDate;
    
    private String dynamicCombox;
    
    private String staticCombox;
    
    private String testChannel;
    /**
     * @return the testDate
     */
    public String getTestDate() {
        return testDate;
    }

    /**
     * @param testDate the testDate to set
     */
    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    /**
     * @return the dynamicCombox
     */
    public String getDynamicCombox() {
        return dynamicCombox;
    }

    /**
     * @param dynamicCombox the dynamicCombox to set
     */
    public void setDynamicCombox(String dynamicCombox) {
        this.dynamicCombox = dynamicCombox;
    }

    /**
     * @return the staticCombox
     */
    public String getStaticCombox() {
        return staticCombox;
    }

    /**
     * @param staticCombox the staticCombox to set
     */
    public void setStaticCombox(String staticCombox) {
        this.staticCombox = staticCombox;
    }

    /**
     * @return the testChannel
     */
    public String getTestChannel() {
        return testChannel;
    }

    /**
     * @param testChannel the testChannel to set
     */
    public void setTestChannel(String testChannel) {
        this.testChannel = testChannel;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String combox() throws Exception {
        List<Map<String,String>> ts = new ArrayList<Map<String,String>>();
        for(int i = 0 ; i < 10 ; i++) {
            Map<String,String> t = new HashMap<String, String>();
            t.put("abbr", "test-" + i);
            t.put("name", "n-combo-" + i);
            ts.add(t);
        }
        JsonControllerHelper.writeJsonToResponse(response, ts);
        return null;
    }
}
