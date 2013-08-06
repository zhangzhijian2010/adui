Ext.require([ '*' ]);
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
	var west_panel = Ext.create('Ext.Panel', {
		region : 'west',
		stateId : 'navigation-panel',
		id : 'west-panel',
		title : '酷云广告功能列表',
		split : true,
		width : 200,
		minWidth : 175,
		maxWidth : 400,
		collapsible : true,
		animCollapse : true,
		margins : '0 0 0 5',
		layout : 'accordion',
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

	var _fields = {
		id : 'id',
		name : 'name',
		pId : 'pId',
		oNum : 'oNum',
		url : 'url'
	};

	function toStoreFields(fields) {
		var array = new Array();
		var i = 0;
		for (prop in fields) {
			array[i++] = prop;
		}
		return array;
	}

	var menuStore = new Ext.data.JsonStore({
		proxy : {
			type : 'ajax',
			url : './index_index_menu.htm',
			reader : {
				type : 'json'
			}
		},
		fields : toStoreFields(_fields),
		listeners : {
			'load' : function(store, records, options) {
				doMenu();
			}
		}
	});

	menuStore.load();

	var createMenu = function(pm) {
		var itemPanel = new Ext.Panel({
			title : pm.get(_fields.name),
			border : false
		});
		
		var items = menuStore.query(_fields.pId, pm.get(_fields.id), false,
				false);
		
		var noteArray = new Array();
		for ( var k = 0; k < items.getCount(); k++) {
			var node = {
				id : items.get(k).get(_fields.id),
				text : items.get(k).get(_fields.name),
				url : items.get(k).get(_fields.url),
				leaf : true
			};
			noteArray[k] = node;
		}

		var rootNode = {
			text : 'test',
			expanded : true,
			children : noteArray
		};

		var item = Ext.create('Ext.tree.Panel', {
			border : false,
			rootVisible : false,
			root : rootNode
		});
		
		item.on("itemclick", function(view,record,item) {
			addTab(record.raw.id, record.raw.text , record.raw.url);
		});
		
		itemPanel.add(item);
		return itemPanel;
	};

	var doMenu = function() {
		var pms = menuStore.query(_fields.pId, _ROOT_ID);
		for ( var j = 0; j < pms.getCount(); j++) {
			var navBar = createMenu(pms.get(j));
			west_panel.add(navBar);
		}
		west_panel.doLayout();
	};
});