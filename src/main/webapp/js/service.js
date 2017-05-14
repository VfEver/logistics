/**
 * angular JS service module
 */
angular.module('service', [])
.factory('GetCarrierList', function($http) {
	
	var res = {};
	var get = function() {

		return $http
				.get('/logistics/carrier/getcarrierlist.do')
				.then(function(data) {
					res = data.data;
					return res;
				});
	};
	return {
		get: get
	};
})
.factory('GetCarList', function($http) {
	
	var res = {};
	var get = function() {

		return $http
				.get('/logistics/car/getcarlist.do')
				.then(function(data) {
					res = data.data;
					return res;
				});
	};
	return {
		get: get
	};
})
.factory('GetUserList', function($http) {
	
	var res = {};
	var get = function() {

		return $http
				.get('/logistics/user/getuserlist.do')
				.then(function(data) {
					res = data.data;
					return res;
				});
	};
	return {
		get: get
	};
})
;