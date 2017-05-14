/**
 * app module
 */
angular.module("logistics", ['ui.router', 'controllers', 'service'])
.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/index');
	$stateProvider
	.state('index', {
		url: '/index',
		views: {
			'navinfo': {
				templateUrl: 'tpls/index-nav-tpl.html'
			},
			'main': {
				templateUrl: 'tpls/task-main-tpl.html'
			},
			'footer': {
				templateUrl: ''
			}
		},
		cache:'false'
	})
	.state('operation', {
		url: '/operation',
		views: {
			'navinfo': {
				templateUrl: 'tpls/index-nav-tpl.html'
			},
			'main': {
				templateUrl: 'tpls/operation-main-tpl.html'
			},
			'footer': {
				templateUrl: ''
			}
		},
		cache:'false'
	})
	.state('information', {
		url: '/information',
		views: {
			'navinfo': {
				templateUrl: 'tpls/index-nav-tpl.html'
			},
			'main': {
				templateUrl: 'tpls/information-main-tpl.html'
			},
			'footer': {
				templateUrl: ''
			}
		},
		cache:'false'
	})
	.state('settlement', {
		url: '/settlement',
		views: {
			'navinfo': {
				templateUrl: 'tpls/index-nav-tpl.html'
			},
			'main': {
				templateUrl: 'tpls/settlement-main-tpl.html'
			},
			'footer': {
				templateUrl: ''
			}
		},
		cache:'false'
	})
	.state('statistics', {
		url: '/statistics',
		views: {
			'navinfo': {
				templateUrl: 'tpls/index-nav-tpl.html'
			},
			'main': {
				templateUrl: 'tpls/statistics-main-tpl.html'
			},
			'footer': {
				templateUrl: ''
			}
		},
		cache:'false'
	});
}]);