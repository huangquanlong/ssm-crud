/**
 * Ext 3.3
 * 只选择年月
 */

Ext.ns('Ext.yucheng.form');

Ext.yucheng.form.MonthField = Ext.extend(Ext.form.DateField, { 
	onTriggerClick : function() { 
		Ext.yucheng.form.MonthField.superclass.onTriggerClick.call(this, arguments); 
		if (this.format.indexOf('d') == -1) { 
			this.menu.picker.monthPicker.slideIn = function() { 
				this.show(); 
			}; 
			this.menu.picker.hideMonthPicker = function() { 
				this.monthPicker.hide(); 
				var v = this.activeDate; 
				if (v) { 
					this.setValue(v); 
					this.fireEvent('select', this, this.value); 
				} 
			}; 
			this.menu.picker.showMonthPicker(); 
		} 
	} 
});