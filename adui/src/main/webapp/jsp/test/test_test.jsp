<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../common/common.jsp"%>
<title>测试Grid</title>
<script type="text/javascript">
Ext.onReady(function() {
    Ext.tip.QuickTipManager.init();
    // 加载本地数据的下拉框
    var staticCombox = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '静态Combox',
        id:'staticComboxId',
        store: Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            data: [{
                "abbr": "AL",
                "name": "Alabama"
            },
            {
                "abbr": "AK",
                "name": "Alaska"
            },
            {
                "abbr": "AZ",
                "name": "Arizona"
            }]
        }),
        queryMode: 'local',
        displayField: 'name',
        labelWidth: 90,
        valueField: 'abbr',
        width: 200
    });
    // 加载远程数据的下拉框
    var dynamicCombox = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '动态Combox',
        id:'dynamicComboxId',
        store: Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: './test_test_combox.htm',
                simpleSortMode: true
            },
            sorters: [{
                property: 'abbr',
                direction: 'DESC'
            }]
        }),
        queryMode: 'remote',
        displayField: 'name',
        labelWidth: 90,
        valueField: 'abbr',
        width: 200
    });
    // 查询条件Panel
    var searchPanel = Ext.create('Ext.form.Panel', {
        formId: 'test_form_panel',
        region: 'north',
        collapsible: true,
        title: '查询表达式',
        split: true,
        height: 120,
        layout: 'form',
        bodyStyle: 'padding:10px;',
        defaults: {
            border: false
        },
        items: [{
            layout: 'column',
            defaults: {
                border: false
            },
            style: 'padding-left:5px',
            items: [{
                width: 250,
                layout: 'form',
                defaults: {
                    border: false
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '测试名称',
                    labelWidth: 65,
                    id: 'channelId',
                    width: 200
                }]
            },
            {
                style: 'padding-left:10px',
                width: 250,
                layout: 'form',
                defaults: {
                    border: false
                },
                items: [{
                    xtype: 'datefield',
                    fieldLabel: '测试日期',
                    labelWidth: 65,
                    format: 'Y-m-d',
                    id: 'date_id',
                    width: 200
                }]
            },
            {
                style: 'padding-left:10px',
                width: 250,
                layout: 'form',
                defaults: {
                    border: false
                },
                items: [staticCombox]
            },
            {
                style: 'padding-left:10px',
                width: 250,
                layout: 'form',
                defaults: {
                    border: false
                },
                items: [dynamicCombox]
            },
            {
                style: 'padding-left:10px',
                width: 100,
                layout: 'form',
                defaults: {
                    border: false
                },
                items: [{
                    xtype: 'button',
                    text: '查&nbsp;&nbsp;&nbsp;询',
                    width: 80,
                    handler: function() {
                        searchFunc();
                    }
                }]
            },
            {
                columnWidth: .2,
                layout: 'form',
                defaults: {
                    border: false
                },
                items: [{
                    xtype: 'button',
                    text: '重&nbsp;&nbsp;&nbsp;置',
                    width: 80,
                    handler: function() {
                    	searchPanel.getForm().reset();
                    }
                }]
            }]
        }]
    });
    /**
	 * 模型类
	 */
    Ext.define('Test', {
        extend: 'Ext.data.Model',
        fields: ['name', {
            name: 'age',
            type: 'int'
        },
        'intros'],
        idProperty: 'threadid'
    }); 
    // 请求远端数据类
    var store = Ext.create('Ext.data.Store', {
        pageSize: 200,
        model: 'Test',
        remoteSort: true,
        proxy: {
            type: 'ajax',
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
    }); // 显示列表页面
    var grid = Ext.create('Ext.grid.Panel', {
        region: "center",
        store: store,
        disableSelection: true,
        loadMask: true,
        columns: [{
            text: "名称",
            dataIndex: 'name',
            width: 200,
            flex: 1,
            sortable: false
        },
        {
            text: "描述",
            dataIndex: 'intros',
            width: 100,
            sortable: true
        },
        {
            text: "年龄",
            dataIndex: 'age',
            width: 70,
            align: 'right',
            sortable: true
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true,
            emptyMsg: "No topics to display"
        })
    });
    // 加载查询参数
    store.on('beforeload', function (store, options) {
        var new_params = {
			  'dynamicCombox' : Ext.getCmp('dynamicComboxId').getValue(),
			  'staticCombox' : Ext.getCmp('staticComboxId').getValue(),
			  'testDate' : Ext.getCmp('date_id').getRawValue() ,
			  'testChannel' : Ext.getCmp('channelId').getValue()};
        Ext.apply(store.proxy.extraParams, new_params);
    });
    store.loadPage(1);
    Ext.create('Ext.Viewport', {
        id: 'test_view',
        layout: 'border',
        items: [searchPanel, grid]
    });
    
 	// 定义查询按键事件
	var searchFunc = function() {
		store.loadPage(1);
	};
});
</script>
</head>
<body>

</body>
</html>