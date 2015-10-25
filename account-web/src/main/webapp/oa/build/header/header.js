define(
		'bee-demo/header/header',
		[ "node", "io" ],
		function(require, exports, module) {
			var $ = require('node').all;
			var Node = require('node');
			var IO = require('io');
			module.exports = {
				init : function() {
					var headerBG = new Node('<div>').addClass('banner');
					var logoKnot = new Node('<div>').addClass('image logoKnot');
					var logoText = new Node('<div>').addClass('image logoText');
					$('header').append(
							headerBG.append(logoKnot).append(logoText));
					var welcomePanel = new Node('<div>')
							.addClass('image welcomePanel');
					var welcomeImage = new Node('<div>')
							.addClass('image welcomeImage');
					var welcomePerson = new Node('<div>')
							.addClass('welcomePerson');
					headerBG.append(welcomePanel.append(welcomeImage).append(
							welcomePerson));
					initWelcome(welcomePerson);
					var rightPanel = new Node('<div>').addClass('rightPanel');
					var headerSetting = new Node('<div>')
							.addClass('rightPanelItems image headerSetting');
					var headerSearch = new Node('<div>')
							.addClass('rightPanelItems image headerSearch');
					var headerHelping = new Node('<div>')
							.addClass('rightPanelItems image headerHelping');
					headerBG.append(rightPanel.append(headerSetting).append(
							headerSearch).append(headerHelping));
				}
			}
			function initWelcome(welcomePerson) {
				IO.get('loginPerson.json', {
					'index' : 1
				}, function(data) {
					welcomePerson.html(data.loginPerson.name);
				}, 'json');
			}
		});