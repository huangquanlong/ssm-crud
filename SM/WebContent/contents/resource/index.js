	Controller = function()
{
	var grid, record;

	function createGrid()
	{
    var data = [
			{"product":"111","price":64.72,"_id":1,"_parent":null,"_level":1,"_lft":1,"_rgt":8,"_is_leaf":false},
			{"product":"222","price":34.14,"_id":2,"_parent":1,"_level":2,"_lft":2,"_rgt":7,"_is_leaf":false},
			{"product":"333","price":75.43,"_id":3,"_parent":2,"_level":3,"_lft":3,"_rgt":4,"_is_leaf":true},
			{"product":"444","price":75.43,"_id":4,"_parent":2,"_level":3,"_lft":5,"_rgt":6,"_is_leaf":true},
			{"product":"555","price":75.43,"_id":5,"_parent":null,"_level":1,"_lft":9,"_rgt":12,"_is_leaf":false},
			{"product":"666","price":75.43,"_id":6,"_parent":5,"_level":2,"_lft":10,"_rgt":11,"_is_leaf":true},
			{"product":"777","price":75.43,"_id":7,"_parent":null,"_level":1,"_lft":13,"_rgt":14,"_is_leaf":true}
		];
   



    // create the data store
    var record = Ext.data.Record.create([
   		{name: 'product'},
     	{name: 'price', type: 'float'},
     	{name: '_id', type: 'int'},
     	{name: '_level', type: 'int'},
     	{name: '_lft', type: 'int'},
     	{name: '_rgt', type: 'int'},
     	{name: '_is_leaf', type: 'bool'}
   	]);
    var store = new Ext.ux.maximgb.tg.NestedSetStore({
    	autoLoad : true,
			reader: new Ext.data.JsonReader({id: '_id'}, record),
			proxy: new Ext.data.MemoryProxy(data)
    });
    // create the Grid
    var grid = new Ext.ux.maximgb.tg.GridPanel({
      store: store,
      master_column_id : 'product',
      columns: [
      	//expander,
				{id:'product',header: "产品", width: 100, sortable: true, dataIndex: 'product'},
        {header: "Price", width: 100, sortable: true, renderer: 'money', dataIndex: 'price'}
      ],
      stripeRows: true,
      autoExpandColumn: 'product',
      title: '客户业务汇总',
      //plugins: expander,
      viewConfig : {
      	enableRowBody : true
      }
    });
    var vp = new Ext.Viewport({
    	layout : 'fit',
    	items : grid
    });
	}
	
	

	return {
		init : function()
		{
			createGrid();
		}
	}
}();

Ext.onReady(Controller.init);