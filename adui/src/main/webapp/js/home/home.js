Ext.require([ '*' ]);
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
	var west_panel = Ext.create('Ext.panel.Panel', {
		region : 'west',
		stateId : 'navigation-panel',
		id : 'west-panel',
		title : '酷云广告功能列表',
		split : true,
		width : 200,
		autoScroll:true,
		overflowY:'auto',
		collapsible : true,
		animCollapse : true,
		margins : '0 0 0 5',
		items : []
	});

	var north_panel = Ext.create('Ext.Component', {
		region : 'north',
		height : 1
	// , // give north and south regions a height
	// autoEl: {
	// tag: 'div',
	// html:'<p>north - generally for menus, toolbars and/or advertisements</p>'
	// }
	});
	// in this instance the TabPanel is not wrapped by another panel
	// since no title is needed, this Panel is added directly
	// as a Container

	var tab_panel = Ext.create('Ext.tab.Panel', {
		region : 'center', // a center region is ALWAYS required for border
		// layout
		deferredRender : false,
		activeTab : 0, // first tab initially active
		items : []
	});

	var viewport = Ext.create('Ext.Viewport', {
		id : 'border-example',
		layout : 'border',
		items : [ north_panel, west_panel, tab_panel ]
	});

	// 创建Tab标签页
	var addTab = function(id, title, url) {
		var newId = 'tab_' + id;
		// 判断Tab标签页是否已经打开
		var tab = Ext.getCmp(newId);
		if (tab) {
			tab_panel.setActiveTab(tab);
			return;
		}
		var newTab = tab_panel.add({
			id : newId,
			title : title,
			layout : 'fit',
			closable : true,
			html : "<iframe width='100%' height='100%' scrolling='auto' "
					+ "frameborder='0' src='" + url + "'></iframe>"
		});
		tab_panel.setActiveTab(newTab);
	};

	// 获取系统菜单数据
	var _ROOT_ID = "000"; // 根节点ID
	
	var rootNode = {
		text : 'test',
		id:_ROOT_ID,
		expanded : true
	};
	
	var treeMenu = Ext.create('Ext.tree.Panel', {
		border : false,
		autoScroll:true,
		overflowY:'auto',
		useArrows: true,
		rootVisible : false,
		store: new Ext.data.TreeStore({
            proxy: {
                type: 'ajax',
                url: './index_index_menu.htm'
            },
            root: rootNode,
            folderSort: true
        })
	});
	
	treeMenu.on("itemclick", function(view,record,item) {
		if(record.raw.leaf) {
			addTab(record.raw.id, record.raw.text , record.raw.url);
		}
	});
	
	west_panel.add(treeMenu);
	west_panel.doLayout();
	
});