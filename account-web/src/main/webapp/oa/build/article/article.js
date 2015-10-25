define(
		'bee-demo/article/article',
		[ "node", "./article-view", 'button', 'io', 'json', 'menu',
				'separator', 'util', 'bee-demo/buttonPlus', 'event-dom',
				'mypkg/jsonx', "kg/xtemplate/4.2.0/runtime",
				'bee-demo/dataFrame', 'bee-demo/dataFilter' ],
		function(require, exports, module) {
			var $ = require('node').all, Node = require('node'), Util = require('util'), Menu = require('menu'), IO = require('io'), BP = require('bee-demo/buttonPlus'), DataFrame = require('bee-demo/dataFrame'), DataFilter = require('bee-demo/dataFilter');
			module.exports = {
				init : function() {
					var BG = new Node('<div>').addClass('middleBanner');
					var sorterOfTime = new Node('<div>').unselectable().append(
							'到达时间').addClass('oa-sorter').addClass('ks-button')
							.addClass('ks-button-mini').append(
									new Node('<img>').prop({
										src : 'image/arrowDownUnuse.png'
									}).addClass('oa-sorter-arrow'));
					var sorterOfUrgency = new Node('<div>').unselectable()
							.append('缓急').addClass('oa-sorter').addClass(
									'ks-button').addClass('ks-button-mini')
							.append(new Node('<img>').prop({
								src : 'image/arrowDownUnuse.png'
							}).addClass('oa-sorter-arrow'));
					var sorterOfCustom = new Node('<div>').unselectable()
							.append('自定义').addClass('oa-sorter').addClass(
									'ks-button').addClass('ks-button-mini')
							.append(new Node('<img>').prop({
								src : 'image/arrowDownUnuse.png'
							}).addClass('oa-sorter-arrow'));
					$('article').append(BG);
					var sorterPanel = new Node('<div>').addClass('sorterPanel')
							.addClass('ks-button-group');
					BG.append(new Node('<div>').addClass('navigatorSpan').prop(
							{
								id : 'navigatorContainer'
							}));
					BG.append(
							new Node('<div>').addClass('bannerSeparatorSpan')
									.append(new Node('<img>').prop({
										src : 'image/bannerSeparator.png'
									}).addClass('bannerSeparator'))).append(
							new Node('<div>').addClass('sorterPanelLeft')
									.append('按')).append(sorterPanel).append(
							new Node('<div>').addClass('sorterPanelRight')
									.append('排序'));
					sorterPanel.append(sorterOfTime).append(sorterOfUrgency)
							.append(sorterOfCustom);
					BP.tapelize(sorterPanel.children(), function(n) {
						n.addClass('ks-button-pressed');
						var src = n.one('.oa-sorter-arrow').prop('src');
						if (Util.endsWith(src, 'Unuse.png')) {
							n.one('.oa-sorter-arrow').prop('src',
									src.substring(0, src.length - 9) + '.png');
						} else {
							if (Util.endsWith(src, 'Down.png')) {
								n.one('.oa-sorter-arrow').prop(
										'src',
										src.substring(0, src.length - 8)
												+ 'Up.png');
							} else if (Util.endsWith(src, 'Up.png')) {
								n.one('.oa-sorter-arrow').prop(
										'src',
										src.substring(0, src.length - 6)
												+ 'Down.png');
							}
						}
					}, function(n) {
						n.removeClass('ks-button-pressed');
						var src = n.one('.oa-sorter-arrow').prop('src');
						if (!Util.endsWith(src, 'Unuse.png')) {
							n.one('.oa-sorter-arrow').prop(
									'src',
									src.substring(0, src.length - 4)
											+ 'Unuse.png');
						}
					});
					BP.nOrders(sorterOfTime, [ function(n) {
					}, function(n) {
					} ]);

					BP.nOrders(sorterOfUrgency, [ function(n) {
					}, function(n) {
					} ]);

					BP.nOrders(sorterOfCustom, [ function(n) {
					}, function(n) {
					} ]);

					IO.get('menuData.json', {}, function(data) {
						var sb = buildMenu(data, {
							offset : [ 5, 36 ],
							points : [ 'tl', 'tl' ]
						});
						var b = new Menu.SubMenu({
							prefixCls : "nav-",
							content : "<span class='title'>首页</span>",
							menu : sb
						});
						var menu = new Menu({
							prefixCls : "nav-",
							width : 90,
							elCls : "horizonal",
							render : '#navigatorContainer',
							children : [ b ]
						});

						menu.render();

						menu.on("click", function(e) {
							console.log(e.target.get("content"));
							var content = e.target.get("content");
							if (Util.startsWith(content, '<a>')
									&& Util.endsWith(content, '</a>')) {
								content = content.substring(3, content.length);
								content = content.substring(0,
										content.length - 4);
								b.set('content', "<span class='title'>"
										+ content + "</span>");
							}
						});

					}, 'json');
					var buildMenu = function(data, align) {
						var sb = new Menu.PopupMenu({
							prefixCls : "nav-",
							autoHideOnMouseLeave : true,
							width : 150
						});
						if (align != null) {
							sb.set('align', align);
						}
						for ( var o in data) {
							var item;
							if (data[o].sub != null) {
								var sb2 = buildMenu(data[o].sub);
								item = new Menu.SubMenu({
									prefixCls : "nav-",
									content : "<a>" + data[o].content + "</a>",
									menu : sb2
								});
							} else {
								item = new Menu.RadioItem({
									prefixCls : "nav-",
									content : "<a>" + data[o].content + "</a>"
								});
							}
							sb.addChild(item);
						}
						return sb;
					};
					var titleFilter = new Node('<div>').append(new Node(
							'<input>').prop({
						id : 'title'
					}));
					var originFilter = new Node('<div>').append(new Node(
							'<input>').prop({
						id : 'origin'
					}));
					$('article').append('标题：').append(titleFilter)
							.append('别名：').append(originFilter);
					var dataFilter = DataFilter.init();
					dataFilter.add({
						id : 'titleLike',
						node : Node.one('#title'),
						handler : function(n) {
							return n.val() == '' ? null : n.val();
						}
					}).add({
						id : 'origin',
						node : Node.one('#origin'),
						handler : function(n) {
							return n.val() == '' ? null : n.val();
						}
					});
					var dataFrame = DataFrame
							.init(
									'index/testDemo?_content=json',
									dataFilter,
									{
										capacity : 2,
										detail : [
												{
													title : '标题',
													handler : function(n) {
														return n.title;
													}
												},
												{
													title : 'ID',
													handler : function(n) {
														return n.id;
													}
												},
												{
													title : '别名',
													handler : function(n) {
														return n.origin == null ? ''
																: n.origin;
													}
												},
												{
													title : '第一作者',
													handler : function(n) {
														return n.bookWriter[0] == null ? ''
																: n.bookWriter[0].writer.name;
													}
												} ]
									}, 'ks-dataFrame');
					$('article').append(dataFrame.dataTable());
					$('article').append(dataFrame.pageSpan());
					$('article').append('共').append(dataFrame.pageMaxSpan())
							.append('页');
					$('article').append(
							dataFrame.queryButton().text('查询').addClass(
									'ks-button').addClass('ks-button-mini')
									.append(new Node('<img>').prop({
										src : 'image/arrowDownUnuse.png'
									}).addClass('oa-sorter-arrow')));
				}
			};
		});