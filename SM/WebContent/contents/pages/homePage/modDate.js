var panelModDate = [{
			cmid : 'custMgrAch',
			name : '客户经理业绩表（客户经理）',
			dataNames : [{
						header : '项目名称',
						width : 150,
						align : 'left',
						dataIndex : 'TARGET_NAME',
						sortable : true
					}, {
						header : '当前业绩',
						width : 150,
						align : 'right',
						dataIndex : 'CURR_BAL',
						sortable : true,
						renderer : money('0,000.00')
					}, {
						header : '上日业绩',
						width : 150,
						align : 'right',
						dataIndex : 'LADY_BAL',
						sortable : true,
						renderer : money('0,000.00')
					}, {
						header : '较上日增量',
						width : 170,
						align : 'right',
						dataIndex : 'COMLADY_INCRE',
						sortable : true
					}, {
						header : '业绩目标值',
						width : 170,
						align : 'right',
						dataIndex : 'YEAR_TARVA',
						sortable : true,
						renderer : money('0,000.00')
					}, {
						header : '指标完成率%',
						width : 170,
						align : 'right',
						dataIndex : 'TAR_COMRATE',
						sortable : true,
						renderer : money('0,000.00')
					}]
		}, {
			cmid : 'work',
			name : '待办工作',
			dataNames : [{
						align : 'left',
						dataIndex : 'WFJOBNAME',
						header : '工作名称'

					}, {
						align : 'left',
						dataIndex : 'WFNAME',
						header : '流程名称'
					}, {
						align : 'left',
						dataIndex : 'AUTHOR',
						header : '发起人'
					}, {
						align : 'left',
						dataIndex : 'PRENODENAME',
						header : '上一办理人'
					}, {
						align : 'left',
						dataIndex : 'NODEPLANENDTIME',
						header : '交办时间',
						renderer : function(value, p, record) {
							if (typeof value == 'string') {
								return value.substring(0, 10);
							} else {
								return value.format('Y-m-d');
							}
						}
					}]
		}, {
			cmid : 'attCust',
			name : '关注客户',
			dataNames : [{
						align : 'left',
						dataIndex : 'CUST_ZH_NAME',
						header : '客户名称'

					}, {
						align : 'left',
						dataIndex : 'CERT_NUM',
						header : '证件号码'
					}, {
						align : 'right',
						dataIndex : 'TELEPHONE_NUM',
						header : '存款余额',
						renderer : money('0,000.00')
					}]
		}, {
			cmid : 'custManager',
			name : '我管理的客户',
			dataNames : [{
						dataIndex : 'user_name',
						header : '客户名称',
						align : 'left',
						rendered : function() {
							return "<span style='text-align:right'>mid</span"
									+ ">";
						}
					}, {
						dataIndex : 'rela_acct',
						header : '存款时点余额',
						align : 'right',
						renderer : money('0,000.00')
					}, {
						dataIndex : 'rela_pct',
						header : '贷款时点余额',
						align : 'right',
						renderer : money('0,000.00')
					}, {
						dataIndex : 'bal',
						width : 130,
						header : '数据日期',
						align : 'left',
						sortable : true
					}

			]
		}, {
			name : '客户经理业绩表（支行行长）',
			cmid : 'custManAch',
			dataNames : [{
						dataIndex : 'CUST_MANAGER_NAME',
						header : '客户经理姓名',
						align : 'left'
					}, {
						dataIndex : 'DEP_AVG_BAL',
						header : '存款日均',
						align : 'right',
						renderer : money('0,000.00')
					}, {
						dataIndex : 'LOAN_AVG_BAL',
						header : '贷款日均',
						align : 'right',
						renderer : money('0,000.00')
					}, {
						dataIndex : 'DISCOUNT_AVG_BAL',
						header : '贴现日均',
						align : 'right',
						renderer : money('0,000.00')
					}, {
						dataIndex : 'COM_CUST_SUM',
						header : '对公客户数量',
						align : 'right'
					}, {
						dataIndex : 'MIDBU_INCOME',
						header : '中间业务收入',
						align : 'right',
						renderer : money('0,000.00')
					}

			]
		}, {
			name : 'TopN客户',
			cmid : 'topNCust',
			dataNames : [{
						header : '统计日期',
						width : 130,
						align : 'left',
						dataIndex : 'crm_dt',
						sortable : true
					}, {
						header : '客户名称',
						width : 210,
						align : 'left',
						dataIndex : 'cust_name',
						sortable : true
					}, {
						header : '组织机构代码',
						width : 210,
						align : 'left',
						dataIndex : 'cust_zzdm',
						sortable : true
					}, {
						header : '存款时点余额',
						width : 210,
						align : 'right',
						dataIndex : 'dep_bal_sum',
						sortable : true,
						renderer : money('0,000.00')
					}, {
						header : '贷款时点余额',
						width : 210,
						align : 'right',
						dataIndex : 'lon_bal_sum',
						sortable : true,
						renderer : money('0,000.00')
					}]
		}, {
			name : '公告',
			cmid : 'notice',
			dataNames : [{
						align : 'left',
						dataIndex : 'NOTICE_TITLE',
						header : '公告标题'
					}, {
						align : 'left',
						dataIndex : 'NOTICE_LEVEL_ORA',
						header : '重要程度'
					}, {
						align : 'left',
						dataIndex : 'PUBLISHER_NAME',
						header : '发布人'
					}, {
						align : 'left',
						dataIndex : 'PUB_ORG_NAME',
						header : '发布机构'
					}]
		}, {
			name : '提醒',
			cmid : 'remind',
			dataNames : [{
						header : '提醒类型',
						dataIndex : 'MSG_TYP_ORA',
						sortable : true,
						width : 130,
						align : 'left'
					}, {
						header : '客户名称',
						dataIndex : 'CUST_NAME',
						sortable : true,
						width : 130,
						align : 'left'
					}, {
						header : '组织机构代码',
						dataIndex : 'CUST_ZZDM',
						sortable : true,
						width : 130,
						align : 'left'
					}, {
						header : '高管姓名',
						dataIndex : 'MANAGER_NAME',
						sortable : true,
						width : 130,
						align : 'left'
					}, {
						header : '事件名称',
						dataIndex : 'EVENT_NAME',
						sortable : true,
						width : 130,
						align : 'left'
					}]
		}, {
			name : '资讯',
			cmid : 'infomation',
			dataNames : [{
						align : 'left',
						dataIndex : 'MESSAGE_TITLE',
						header : '文档标题'
					}, {
						align : 'left',
						dataIndex : 'MESSAGE_SUMMARY',
						header : '文档摘要'
					}, {
						align : 'left',
						dataIndex : 'PUBLISH_DATE',
						header : '发布时间',
						renderer : function(value, p, record) {
							if (typeof value == 'string') {
								return value.substring(0, 10);
							} else {
								return value.format('Y-m-d');
							}
						}
					}, {
						align : 'left',
						dataIndex : 'PUBLISH_USER',
						header : '发布人'
					}]
		}, {
			name : '营销计划',
			cmid : 'marketPlan',
			dataNames : [{
						header : '营销计划状态',
						width : 130,
						align : 'left',
						dataIndex : 'MKT_PLAN_STAT_ORA',
						sortable : true
					}, {
						header : '营销计划名称',
						width : 210,
						align : 'left',
						dataIndex : 'PLAN_NAME',
						sortable : true
					}, {
						header : '计划开始日期',
						width : 170,
						align : 'left',
						dataIndex : 'PLAN_START_DATE',
						sortable : true
					}, {
						header : '预计结束日期',
						width : 170,
						align : 'left',
						dataIndex : 'PLAN_END_DATE',
						sortable : true
					}]
		}, {
			name : '营销活动',
			cmid : 'marketAct',
			dataNames : [{
						header : '营销活动名称',
						width : 150,
						align : 'left',
						dataIndex : 'MKT_ACTI_NAME',
						sortable : true
					}, {
						header : '客户名称',
						width : 150,
						align : 'left',
						dataIndex : 'ACTI_CUST_NAME',
						sortable : true
					}, {
						header : '执行团队',
						width : 150,
						align : 'left',
						dataIndex : 'OPER_NAME',
						sortable : true
					}, {
						header : '开始日期',
						width : 150,
						align : 'left',
						dataIndex : 'ACTI_START_DATE',
						sortable : true
					}, {
						header : '结束日期',
						width : 150,
						align : 'left',
						dataIndex : 'ACTI_END_DATE',
						sortable : true
					}]
		}, {
			name : '商机',
			cmid : 'opportunity',
			dataNames : [{
						header : '商机状态',
						width : 170,
						align : 'left',
						dataIndex : 'MKT_OPPOR_STAT_ORA',
						sortable : true
					}, {
						header : '商机名称',
						width : 175,
						align : 'left',
						dataIndex : 'MKT_OPPOR_NAME',
						sortable : true
					}, {
						header : '客户名称',
						width : 200,
						align : 'left',
						dataIndex : 'AIM_CUST_NAME',
						sortable : true
					}, {
						header : '执行团队',
						width : 150,
						align : 'left',
						dataIndex : 'OPER_USER_NAME',
						sortable : true
					}, {
						header : '计划开始日期',
						width : 150,
						align : 'left',
						dataIndex : 'OPPOR_START_DATE',
						sortable : true
					}]
		}, {
			name : '客户群管理',
			cmid : 'custGroup',
			dataNames : [{
						header : '客户群编号',
						width : 150,
						align : 'left',
						dataIndex : 'CUST_BASE_NUMBER',
						sortable : true
					}, {
						header : '客户群名称',
						width : 150,
						align : 'left',
						dataIndex : 'CUST_BASE_NAME',
						sortable : true
					}, {
						header : '客户群创建时间',
						width : 150,
						align : 'left',
						dataIndex : 'CUST_BASE_CREATE_DATE',
						sortable : true
					}, {
						header : '客户群描述',
						width : 170,
						align : 'left',
						dataIndex : 'CUST_BASE_DESC',
						sortable : true
					}]
		}];
