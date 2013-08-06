<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../common/common.jsp"%>
<title>测试Grid</title>
<script type="text/javascript">
Ext.onReady(function(){
    Ext.tip.QuickTipManager.init();
    /**
     * 模型类
     */
    Ext.define('Test', {
        extend: 'Ext.data.Model',
        // name:
        fields: [
           'name',{name:'age',type:'int'},'intros'
        ],
        idProperty: 'threadid'
    });
    // 请求远端数据类
    var store = Ext.create('Ext.data.Store', {
        pageSize: 50,
        model: 'Test',
        remoteSort: true,
        proxy: {
            type: 'json',
            url: './test_test_load.htm',
            reader: {
                root: 'root',
                totalProperty: 'sum'
            },
            simpleSortMode: true
        },
        sorters: [{
            property: 'age',
            direction: 'DESC'
        }]
    });
	// 显示列表页面
    var grid = Ext.create('Ext.grid.Panel', {
        region:"center",
        title: 'ExtJS.com - Browse Forums',
        store: store,
        disableSelection: true,
        loadMask: true,
        columns:[{
            text: "名称",
            dataIndex: 'name',
            width: 200,
            flex:1,
            sortable: false
        },{
            text: "描述",
            dataIndex: 'intros',
            width: 100,
            sortable: true
        },{
            text: "年龄",
            dataIndex: 'age',
            width: 70,
            align: 'right',
            sortable: true
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display"
        })
    });
	
    store.loadPage(1);
    
    Ext.create('Ext.Viewport', {
		id : 'test_view',
		layout : 'border',
		items : [grid]
	});
});
</script>
</head>
<body>

</body>
</html>