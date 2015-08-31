G_CUR_TOPIC = null;/**当前topic*/
G_CUR_BTN = null;/**当前按钮*/
G_CUR_MENU = null;
/**字典*/
TOPICS=[{f1:"临时菜单",f2:"funcs",f3:true}];
BTNS={
	funcs:[
	        {txt:'设备版本',url:basePath+'/upload',key:'upload'},
	        {txt:'应用版本',url:basePath+'/app',key:'app'}
	        ],
};

/*MENU={
		yhgl:[
		      {txt:'分销商管理平台',url:'',key:'fxs',sub:[
		                                           {txt:'分销商商户管理',url:'./yhgl/fxsshgl.html',key:'fxsshgl'},
		                                           {txt:'分销商商户权限管理',url:'./yhgl/fxsshqxgl.html',key:'fxsshqxgl'},
		                                           {txt:'分销商销售统计',url:'./yhgl/fxsshtj.html',key:'fxsshtj'},
		                                           ]},
		      {txt:'用户权限管理平台',url:'',key:'yhqx',sub:[
			                                           {txt:'用户管理',url:'./yhgl/yhgl.html',key:'yhgl'},
			                                           {txt:'权限管理',url:'./yhgl/qsgl.html',key:'qsgl'},
			                                           ]},
		      {txt:'商品管理平台',url:'./yhgl/spgl.html',key:'spgl'},
		      {txt:'仓储管理平台',url:'',key:'ccgl',sub:[
		                                           {txt:'仓储管理系统',url:'./yhgl/yhgl.html',key:'yhgl'},
		                                           ]},
		      ],
};*/
/**工厂*/
FACTORY={
	createTopic:function(txt,key,isActive){
		if(isActive)
			return $('<li key="'+key+'" class="active"><a>'+txt+'</a></li>');
		return $('<li key="'+key+'"><a>'+txt+'</a></li>');
	},
	createBtn:function(txt,key,url){
		var $btn=$('<button type="button" class="btn btn-default" data-toggle="button" key="'+key+'">'+txt+'</button>');
		if(url!=''){
			$btn.bind("click",function(event){
					if(G_CUR_BTN!=this){
						$("#_S").load(url);
						G_CUR_BTN=this;
					}
				}
			);
		}
		else{
			$btn.bind("click",function(event){
					if(G_CUR_BTN!=this){
						G_CUR_BTN=this;
						/**初始化视图*/
						$('#_S').empty();
						if($('#_D4').length<=0){
							$('#_S').append($('<div id="_D4"></div>'));
						}
						if($('#_D5').length<=0){
							$('#_S').append($('<div id="_D5"></div>'));
						}
						FACTORY.createD4($(this).attr('key'));
					}
				}
			);
		}
		return $btn;
	},
	
	createD4_UL:function(){
		return $('<ul id="_menu" class="nav nav-tabs nav-stacked">');
	},
	
	createD4_UL_LI:function(){
		return $("<li></li>");
	},
	
	createD4_UL_LI_A:function(txt,key,url){
		if(url==''){
			return $('<a href="#'+key+'" class="nav-header collapsed" data-toggle="collapse" >'+txt+'<span class="pull-right fa fa-caret-left" style="font-size:18px"></span></a>');
		}
		else{
			var $a = $('<a href="#'+key+'" class="nav-header collapsed" data-toggle="collapse" >'+txt+'</a>');
			$a.bind("click",function(event){
					if(G_CUR_MENU!=this){
						$("#_D5").load(url);
						G_CUR_MENU=this;
					}
				}
			);
			return $a;
		}
			
	},
	createD4_UL_LI_UL:function(key){
		return $('<ul id="'+key+'" class="nav collapse ul2" ></ul>');
	},
	createD4_UL_LI_UL_LI:function(txt,url){
		return $('<li><a href="'+url+'">'+txt+'</a></li>');
	},
	
	/**创建_D3内容*/
	createD3:function(key){
		var btns = BTNS[key];
		var $d3 = $("#_D3");
		$d3.html('');
		for(i in btns){
			txt = btns[i].txt;
			url = btns[i].url;
			key = btns[i].key;
			var $btn = FACTORY.createBtn(txt,key,url);
			$d3.append($btn);
		}
	},
	/**创建_D4内容*/
	createD4:function(key){
		var lvl1=MENU[key];
		var $d4 = $("#_D4");
		var $d4ul = this.createD4_UL();
		for(i in lvl1){
			txt = lvl1[i].txt;
			url = lvl1[i].url;
			key = lvl1[i].key;
			children = lvl1[i].sub;
			var $d4ulli = this.createD4_UL_LI();
			var $d4ullia = this.createD4_UL_LI_A(txt,key,url);
			$d4ulli.append($d4ullia);
			$d4ul.append($d4ulli);
			if(url!='')continue;
			var $ul = this.createD4_UL_LI_UL(key);
			for(n in children){
				txt = children[n].txt;
				url = children[n].url;
				key = children[n].key;
				$ul.append(this.createD4_UL_LI_UL_LI(txt,url));
			}
			$d4ullia.bind("click",function(event){
				$(this).find('span').toggleClass('fa fa-caret-down');
				$(this).find('span').toggleClass('fa fa-caret-left');
			});
			$d4ulli.append($ul);
		}
		$d4.append($d4ul);
	}
};


/**页眉按钮初始化*/
$(document).ready(function() {
	/**初始化TOPIC*/
	var $topicbar = $("#_TOPIC>ul");
	$topicbar.html('');
	for(idx in TOPICS){
		var txt=TOPICS[idx].f1;
		var key=TOPICS[idx].f2;
		var isActive=TOPICS[idx].f3;
		$topicbar.append(FACTORY.createTopic(txt,key,isActive));
	}
	G_CUR_TOPIC = $("#_TOPIC li.active");
	$("#_TOPIC li a").bind("click", function(event) {
		if(G_CUR_TOPIC.attr("key")==$(event.target).parent().attr("key")){
			return;
		}
		//清除上次选中的li的active
		if (G_CUR_TOPIC) {
			G_CUR_TOPIC.removeClass("active");
		}
		//为event.target的父元素也就是li添加active
		$(event.target).parent().addClass("active");
		G_CUR_TOPIC = $(event.target).parent();
		FACTORY.createD3(G_CUR_TOPIC.attr("key"));
		$('#_S').empty();
	});
	FACTORY.createD3(G_CUR_TOPIC.attr("key"));
});


/**调整页面高度*/
G_D1_H = 0;/**页眉高*/
G_D2_H = 0;/**导航条高*/
$(document).ready(function() {
	p = $(window).height();
	q = $(window).width();
	G_D1_H = $("#_D1").height();
	G_D2_H = $("#_D2").height();
	z = p - G_D1_H - G_D2_H;
	$("#_AIM").height(z);
});
$(window).resize(function() {
	w = $(window).width();
	h = $(window).height();
	z = h - G_D2_H - G_D1_H;
	$("#_AIM").height(z);
});

$(document).ready(function() {
});




