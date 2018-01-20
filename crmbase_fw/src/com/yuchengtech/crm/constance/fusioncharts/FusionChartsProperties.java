package com.yuchengtech.crm.constance.fusioncharts;

public class FusionChartsProperties {
	
	/**CHART图标整体属性**/
	/**1.Functional Attributes(功能属性)*/
	public static final String animation = "animation";										/**可选值：0/1;含义：设置图形的显示是否是动画显示*/
	public static final String palette = "palette";											/**可选值：1-5;含义：5种默认的调色板风格任你选*/
	public static final String paletteColors = "palettecolors";								/**可选值：String;含义：手动设置调色板的颜色paletteColors/**可选值：FF0000,0372AB,FF5904..."*/
	public static final String showAboutMenuItem = "showaboutmenuitem";						/**可选值：0/1;含义：在图形上点击右键时是否显示about链接*/
	public static final String aboutMenuItemLabel = "aboutmenuitemlabel";					/**可选值：String;含义：about链接的具体名字*/
	public static final String aboutMenuItemLink = "aboutmenuitemlink";						/**可选值：String;含义：about链接的具体链接地址*/
	public static final String showLabels = "showlabels";									/**可选值：0/1;含义：是否显示x轴的坐标值*/
	public static final String labelDisplay = "labeldisplay";								/**可选值：WRAP/STAGGER/ROTATE/NONE;含义：x轴坐标值的具体展现形式*/
	public static final String rotateLabels = "rotatelabels";								/**可选值：0/1;含义：是否旋转x轴的坐标值*/
	public static final String slantLabels = "slantlabels";									/**可选值：0/1;含义：将x轴坐标值旋转为倾斜的还是完全垂直的*/
	public static final String labelStep = "labelstep";										/**可选值：1orabove;含义：x轴坐标值的步长，即可以设置隔几个柱子显示一个值*/
	public static final String staggerLines = "staggerlines";								/**可选值：2orabove;含义：如果labelDisplay设置为STAGGER,则此属性是控制一个展示周期*/
	public static final String showValues = "showvalues";									/**可选值：0/1;含义：是否在图形上显示每根柱子具体的值*/
	public static final String rotateValues = "rotatevalues";								/**可选值：0/1;含义：是否旋转图形上显示的柱子的值*/
	public static final String placeValuesInside = "placevaluesinside";						/**可选值：0/1;含义：图形上柱子的值是否显示在柱子里面*/
	public static final String showYAxisValues = "showyaxisvalues";							/**可选值：0/1;含义：是否显示Y轴的值*/
	public static final String showLimits = "showlimits";									/**可选值：0/1;含义：是否显示极值*/
	public static final String showDivLineValues = "showdivlinevalues";						/**可选值：0/1;含义：是否在divline处显示值*/
	public static final String yAxisValuesStep = "yaxisvaluesstep";							/**可选值：1orabove;含义：Y轴值的步长*/
	public static final String showShadow = "showshadow";									/**可选值：0/1;含义：是否显示阴影*/
	public static final String adjustDiv = "adjustdiv";										/**可选值：0/1;含义：是否自动调整divlines*/
	public static final String rotateYAxisName = "rotateyaxisname";							/**可选值：0/1;含义：是否旋转Y轴的名字*/
	public static final String yAxisNameWidth = "yaxisnamewidth";							/**可选值：Number;含义：Y轴名字的宽度*/
	public static final String clickURL = "clickurl";										/**可选值：String;含义：点击的链接地址*/
	public static final String defaultAnimation = "defaultanimation";						/**可选值：0/1;含义：是否使用默认动画*/
	public static final String yAxisMinValue = "yaxisminvalue";								/**可选值：Number;含义：Y轴的最小值*/
	public static final String yAxisMaxValue = "yaxismaxvalue";								/**可选值：Number;含义：Y轴的最大值*/
	public static final String setAdaptiveYMin = "setadaptiveymin";							/**可选值：0/1;含义：自动设置Y轴的最小值*/
	                                        
	/**可选值：2.TitlesandAxisNames(标题和坐标抽名字)*/ 
	public static final String caption = "caption";											/**可选值：String;含义：主标题名字*/
	public static final String subCaption = "subcaption";									/**可选值：String;含义：副标题名字*/
	public static final String xAxisName = "xaxisname";										/**可选值：String;含义：X轴名字*/
	public static final String yAxisName = "yaxisname";										/**可选值：String;含义：Y轴名字*/
	                                             
	/**可选值：3.ChartsCosmetics(图表美容属性)*/        
	public static final String bgColor = "bgcolor";											/**可选值：Color;含义：图表的背景色*/
	public static final String bgAlpha = "bgalpha";											/**可选值：0-100;含义：背景色的透明度*/
	public static final String bgRatio = "bgratio";											/**可选值：1-100;含义：如果背景色有两个，该属性设置差异的比例*/
	public static final String bgAngle = "bgangle";											/**可选值：0-360;含义：转变背景颜色的角度，设置一个倾斜度*/
	public static final String bgSWF = "bgswf";												/**可选值：String;含义：用做背景的swf路径*/
	public static final String bgSWFAlpha = "bgswfalpha";									/**可选值：0-100;含义：背景swf的透明度*/
	public static final String canvasBgColor = "canvasbgcolor";								/**可选值：Color;含义：画板背景颜色*/
	public static final String canvasBgAlpha = "canvasbgalpha";								/**可选值：0-100;含义：画板背景透明度*/
	public static final String canvasBgRatio = "canvasbgratio";								/**可选值：Number;含义：不同画板背景色的比率*/
	public static final String canvasBgAngle = "canvasbgangle";								/**可选值：Number;含义：画布背景色显示角度*/
	public static final String canvasBorderColor = "canvasbordercolor";						/**可选值：Color;含义：画板边框的颜色*/
	public static final String canvasBorderThickness = "canvasborderthickness";				/**可选值：Number;含义：画板边框的宽度*/
	public static final String canvasBorderAlpha = "canvasborderalpha";						/**可选值：0-100;含义：画板边框的透明度*/
	public static final String showBorder = "showborder";									/**可选值：0/1;含义：是否显示图表边框*/
	public static final String borderColor = "bordercolor";									/**可选值：Color;含义：边框颜色*/
	public static final String borderThickness = "borderthickness";							/**可选值：Number;含义：图表边框的粗细*/
	public static final String borderAlpha = "borderalpha";									/**可选值：0-100;含义：边框透明度*/
	public static final String showVLineLabelBorder = "showvlinelabelborder";				/**可选值：0/1;含义：是否显示垂直线label的宽度*/
	public static final String logoURL = "logourl";											/**可选值：String;含义：在图表上加上logo,logo图片的地址*/
	public static final String logoPosition = "logoposition";								/**可选值：TL/TR/BL/BR/CC;含义：logo的位置*/
	public static final String logoAlpha = "logoalpha";										/**可选值：0-100;含义：logo的透明度*/
	public static final String logoScale = "logoscale";										/**可选值：1-300;含义：控制logo放大缩小的倍数*/
	public static final String logoLink = "logolink";										/**可选值：String;含义：logo的链接地址*/
	                                             
	/**可选值：4.DivisionalLines/Grids(分区线/网格属性)*/
	public static final String numDivLines = "numdivlines";									/**可选值：>0;含义：水平网格线的数量*/
	public static final String divLineColor = "divlinecolor";								/**可选值：Color;含义：网格线颜色*/
	public static final String divLineThickness = "divlinethickness";						/**可选值：1-5;含义：网格线粗细*/
	public static final String divLineAlpha = "divlinealpha";								/**可选值：0-100;含义：网格线透明度*/
	public static final String divLineIsDashed = "divlineisdashed";							/**可选值：0/1;含义：网格线是否显示为虚线*/
	public static final String divLineDashLen = "divlinedashlen";							/**可选值：Number;含义：每个虚线的长度*/
	public static final String divLineDashGap = "divlinedashgap";							/**可选值：Number;含义：每个虚线间的间隔长度*/
	public static final String zeroPlaneColor = "zeroplanecolor";							/**可选值：Color;含义：0值处网格线颜色*/
	public static final String zeroPlaneThickness = "zeroplanethickness";					/**可选值：Number;含义：0值处网格线粗细*/
	public static final String zeroPlaneAlpha = "zeroplanealpha";							/**可选值：0-100;含义：0值处网格线透明度*/
	public static final String showAlternateHGridColor = "showalternatehgridcolor";			/**可选值：0/1;含义：是否交替显示网格颜色*/
	public static final String alternateHGridColor = "alternatehgridcolor";					/**可选值：Color;含义：水平网格颜色*/
	public static final String alternateHGridAlpha = "alternatehgridalpha";					/**可选值：Number;含义：水平网格透明度*/
	                                            
	/**可选值：5.Tool-tip(工具提示属性)*/               
	public static final String showToolTip = "showtooltip";									/**可选值：0/1;含义：是否显示气泡提示*/
	public static final String toolTipBgColor = "tooltipbgcolor";							/**可选值：Color;含义：气泡提示的背景颜色*/
	public static final String toolTipBorderColor = "tooltipbordercolor";					/**可选值：Color;含义：汽包提示的边框颜色*/
	public static final String toolTipSepChar = "tooltipsepchar";							/**可选值：String;含义：气泡提示的分隔符*/
	public static final String showToolTipShadow = "showtooltipshadow";						/**可选值：0/1;含义：是否使气泡提示带有阴影效果*/
	                                            
	/**可选值：6.PaddingsandMargins(填充和边距属性)*/   
	public static final String captionPadding = "captionpadding";							/**可选值：";*/
	public static final String xAxisNamePadding = "xaxisnamepadding";						/**可选值：Number;含义：画板与x轴标题之间的距离*/
	public static final String yAxisNamePadding = "yaxisnamepadding";						/**可选值：Number;含义：画板与y轴标题之间的距离*/
	public static final String yAxisValuesPadding = "yaxisvaluespadding";					/**可选值：Number;含义：画板与y轴值之间的距离*/
	public static final String labelPadding = "labelpadding";								/**可选值：Number;含义：画板离label之间的距离*/
	public static final String valuePadding = "valuepadding";								/**可选值：Number;含义：柱子离值之间的距离*/
	public static final String plotSpacePercent = "plotspacepercent";						/**可选值：0-80;含义：两个bar之间的距离*/
	public static final String chartLeftMargin = "chartleftmargin";							/**可选值：Number;含义：距左边框的距离*/
	public static final String chartRightMargin = "chartrightmargin";						/**可选值：Number;含义：距右边框的距离*/
	public static final String chartTopMargin = "charttopmargin";							/**可选值：Number;含义：距上边框的距离*/
	public static final String chartBottomMargin = "chartbottommargin";						/**可选值：Number;含义：距下边框的距离*/
	public static final String canvasLeftMargin = "canvasleftmargin";						/**可选值：Number;含义：画板离左边的距离*/
	public static final String canvasRightMargin = "canvasrightmargin";						/**可选值：Number;含义：画板离右边的距离*/
	public static final String canvasTopMargin = "canvastopmargin";							/**可选值：Number;含义：画板离上边的距离*/
	public static final String canvasBottomMargin = "canvasbottommargin";					/**可选值：Number;含义：画板离下边的距离*/
	                                           
	/**可选值：数据属性**/                              
	/**可选值：7.element(set元素属性)*/                 
	public static final String label_ELELMENT = "label";									/**可选值：String;含义：具体的标签*/
	public static final String value = "value";												/**可选值：Number;含义：具体的值*/
	public static final String displayValue_ELEMENT = "displayvalue";						/**可选值：String;含义：显示的值*/
	public static final String color_ELELMENT = "color";									/**可选值：Color;含义：该柱子的颜色*/
	public static final String link = "link";												/**可选值：String;含义：链接地址*/
	public static final String toolText_ELELMENT = "tooltext";								/**可选值：String;含义：气泡提示时显示的值*/
	public static final String showLabel = "showlabel";										/**可选值：0/1;含义：是否显示标签*/
	public static final String showValue = "showvalue";										/**可选值：0/1;含义：是否显示此柱子的值*/
	public static final String dashed_ELELMENT = "dashed";									/**可选值：0/1;含义：柱子的边框是否显示为虚线*/
	public static final String alpha_ELELMENT = "alpha";									/**可选值：Number;含义：柱子的透明度*/
	                                            
	/**可选值：8.PlotCosmetics(节点美容属性)*/          
	public static final String useRoundEdges = "useroundedges";								/**可选值：0/1;含义：是否显示光滑边缘*/
	public static final String showPlotBorder = "showplotborder";							/**可选值：0/1;含义：是否显示柱子的边框*/
	public static final String plotBorderColor = "plotbordercolor";							/**可选值：Color;含义：柱子边框的颜色*/
	public static final String plotBorderThickness = "plotborderthickness";					/**可选值：0-5;含义：柱子边框的厚度*/
	public static final String plotBorderAlpha = "plotborderalpha";							/**可选值：0-100;含义：柱子边框的透明度*/
	public static final String plotBorderDashed = "plotborderdashed";						/**可选值：0/1;含义：柱子边框是否显示为虚线*/
	public static final String plotBorderDashLen = "plotborderdashlen";						/**可选值：Number;含义：虚线的长度*/
	public static final String plotBorderDashGap = "plotborderdashgap";						/**可选值：Number;含义：虚线的间隔*/
	public static final String plotFillAngle = "plotfillangle";								/**可选值：0-360;含义：数据填充色角度*/
	public static final String plotFillRatio = "plotfillratio";								/**可选值：0-100;含义：数据填充色比率*/
	public static final String plotFillAlpha = "plotfillalpha";								/**可选值：0-100;含义：数据填充色透明度*/
	public static final String plotGradientColor = "plotgradientcolor";						/**可选值：Color;含义：数据的有坡度颜色方案*/
	                                            
	/**可选值：特殊图形属性**/                          
	/**可选值：9.NumberFormatting(数字格式化属性)*/     
	public static final String formatNumber = "formatnumber";								/**可选值：0-1;含义：是否格式化数值*/
	public static final String formatNumberScale = "formatnumberscale";						/**可选值：0-1;含义：是否对大数值以k,M方式表示*/
	public static final String defaultNumberScale = "defaultnumberscale";					/**可选值：String;含义：默认的数字格式化*/
	public static final String numberScaleUnit = "numberscaleunit";							/**可选值：String;含义：设置进位规则对应的单位eg:k,m,b*/
	public static final String numberScaleValue = "numberscalevalue";						/**可选值：String;含义：设置进位的规则eg:1000,1000,1000*/
	public static final String numberPrefix = "numberprefix";								/**可选值：String;含义：数值前缀*/
	public static final String numberSuffix = "numbersuffix";								/**可选值：String;含义：数值后缀*/
	public static final String decimalSeparator = "decimalseparator";						/**可选值：String;含义：设置小数点的分隔符的表示形式,|.*/
	public static final String thousandSeparator = "thousandseparator";						/**可选值：String;含义：设置3位数值之间的分隔符的表示形式,|.*/
	public static final String inDecimalSeparator = "indecimalseparator";					/**可选值：String;含义：设置小数分隔符*/
	public static final String inThousandSeparator = "inthousandseparator";					/**可选值：String;含义：设置千位分隔符*/
	public static final String decimals = "decimals";										/**可选值：0-10;含义：小数点后保留几位*/
	public static final String forceDecimals = "forcedecimals";								/**可选值：0/1;含义：小数点后位数不够的，是否强制补0*/
	public static final String yAxisValueDecimals = "yaxisvaluedecimals";					/**可选值：0-10;含义：y轴值保留几位小数*/
	                                           
	/**可选值：10.FontProperties(字体属性)*/            
	public static final String baseFont = "basefont";										/**可选值：String;含义：字体*/
	public static final String baseFontSize = "basefontsize";								/**可选值：0-72;含义：字体大小*/
	public static final String baseFontColor = "basefontcolor";								/**可选值：Color;含义：字体颜色*/
	public static final String outCnvBaseFont = "outcnvbasefont";							/**可选值：String;含义：画板外的字体*/
	public static final String outCnvBaseFontSize = "outcnvbasefontsize";					/**可选值：0-72;含义：画板外的字体大小*/
	public static final String outCnvBaseFontColor = "outcnvbasefontcolor";					/**可选值：Color;含义：画板外的字体颜色*/
	                                            
	/**可选值：11.VerticalLines(垂直线属性)*/           
	public static final String color_VLINE = "color";										/**可选值：Color;含义：颜色*/
	public static final String thickness_VLINE = "thickness";								/**可选值：Number;含义：厚度*/
	public static final String alpha_VLINE = "alpha";										/**可选值：0-100;含义：透明度*/
	public static final String dashed_VLINE = "dashed";										/**可选值：0/1;含义：是否使用虚线*/
	public static final String dashLen_VLINE = "dashlen";									/**可选值：Number;含义：虚线的长度*/
	public static final String dashGap_VLINE = "dashgap";									/**可选值：Number;含义：虚线间隔的长度*/
	public static final String label = "label";												/**可选值：String;含义：此垂直线的名字*/
	public static final String showLabelBorder = "showlabelborder";							/**可选值：0/1;含义：是否显示label的边框*/
	public static final String linePosition = "lineposition";								/**可选值：0/1;含义：line的位置*/
	public static final String labelPosition = "labelposition";								/**可选值：0/1;含义：label的位置*/
	public static final String labelHAlign = "labelhalign";									/**可选值：left/center/right;含义：水平线label的位置*/
	public static final String labelVAlign = "labelvalign";									/**可选值：top/middle/bottom;含义：垂直线label的位置*/
	                                            
	/**可选值：12.TrendLines(趋势线属性)*/              
	public static final String startValue = "startvalue";									/**可选值：Number;含义：开始值*/
	public static final String endValue = "endvalue";										/**可选值：Number;含义：结束值*/
	public static final String displayValue = "displayvalue";								/**可选值：String;含义：显示的值*/
	public static final String color = "color";												/**可选值：Color;含义：颜色*/
	public static final String isTrendZone = "istrendzone";									/**可选值：0/1;含义：是否显示趋势线*/
	public static final String showOnTop = "showontop";										/**可选值：0/1;含义：趋势线是否显示在上面*/
	public static final String thickness = "thickness";										/**可选值：Number;含义：趋势线的宽度*/
	public static final String alpha = "alpha";												/**可选值：0-100;含义：趋势线的透明度*/
	public static final String dashed = "dashed";											/**可选值：0/1;含义：趋势线是否为虚线*/
	public static final String dashLen = "dashlen";											/**可选值：Number;含义：趋势线虚线的长度*/
	public static final String dashGap = "dashgap";											/**可选值：Number;含义：虚线之间的间隔长度*/
	public static final String valueOnRight = "valueonright";								/**可选值：0/1;含义：趋势线的标记是否在右边*/
	public static final String toolText = "tooltext";										/**可选值：String;含义：趋势线标记的名字*/

}
