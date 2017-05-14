/**
 * controller module
 */
angular.module('controllers', ['ngCookies'])
.config(function ($httpProvider) {
	$httpProvider.defaults.transformRequest = function(data){
        if (data === undefined) {
            return data;
        }
        return $.param(data);
    }
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
    $httpProvider.defaults.headers.post['Content-Type'] =  'application/x-www-form-urlencoded';
})
.controller('NavOperatorController', ['$scope', '$location', '$cookies', '$http', '$window', function($scope, $location, $cookies, $http, $window) {
	
	//激活状态更改
	$scope.changToTask = function () {
		$("li").removeClass("active");
		$("#taskID").addClass("active");
		$location.path("index");
	}
	
	$scope.changToSettlement = function () {
		$("li").removeClass("active");
		$("#settlementID").addClass("active");
		$location.path("settlement");
	}
	
	$scope.changToOperation = function () {
		$("li").removeClass("active");
		$("#operationID").addClass("active");
		$location.path("operation");
	}
	
	$scope.changToStatistics = function () {
		$("li").removeClass("active");
		$("#statisticsID").addClass("active");
		$location.path("statistics");
	}

	$scope.changToInformation = function () {
		$("li").removeClass("active");
		$("#informationID").addClass("active");
		$location.path("information");
	}
	
	//开始状态为不在线
	$scope.online = false;
	
	//点击注册弹出注册模态框
	$scope.showRegisterModal = function () {
		$("#registerModal").modal("toggle");
	}
	
	//点击登录弹出登录模态框
	$scope.showLoginModal = function () {
		$("#loginModal").modal("toggle");
	}
	
	//两次密码是否一致
	$scope.comfirmPasswordInvalidate = false;
	//密码和确认密码是否一致
	$('.confirmPasswordInput').on('change', function () {
		
		if ($scope.comfirmPassword == undefined) {
			$scope.$apply(function () {
				$scope.comfirmPasswordInvalidate = true;
			});
		}
		
		if ($scope.password === $scope.comfirmPassword) {
			$scope.$apply(function () {
				$scope.comfirmPasswordInvalidate = false;
			});
		} else {
			$scope.$apply(function () {
				$scope.comfirmPasswordInvalidate = true;
			});
		}
	});
	
	$scope.accountInvalidate = false;//账号合法设置
	$scope.accountUsed = false;//账号是否使用过
	//验证账号是否合法
	$('.accountInput').on('blur', function() {
		if ($scope.account === undefined || $scope.account.trim() === '') {
			return ;
		}
		
		if ($scope.account.trim().length < 6 || $scope.account.trim().length > 20) {
			return ;
		} else {
			$scope.$apply(function () {
				$scope.accountInvalidate  = false;
			});
		}
		
		$http({
			method:'post',  
			url:'/logistics/user/accountused.do',
			data:{
				account: $scope.account
			}
		}).success(function(data){  
			if (data.used) {
				$scope.accountUsed = true;
				$scope.accountInvalidate = true;
			} else {
				$scope.accountUsed = false;
				$scope.accountInvalidate = false;
			}
		})
		
	});
	
	//点击注册发起请求
	$scope.register = function () {
		
		if ($scope.accountInvalidate || $scope.accountUsed) {
			alert("账号或者密码有问题");
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/user/register.do',
		    data:{
		    	account: $scope.account,
		    	password: $scope.password
		    }
		}).success(function(data){
			if (data.status === 200) {
				$scope.online = true;
				$scope.username = data.username;
				$scope.job = data.job;
				
				$cookies.put("username", data.username);
				$cookies.put("job", data.job);
				$cookies.put("userID", data.userID);
				$cookies.put("userType", data.userType);
			} else {
				aler(data.reason);
			}
		})
	}
	
	//用户登录
	$scope.login = function () {
		
		$http({
		    method:'post', 
		    url:'/logistics/user/login.do',
		    data:{
		    	account: $scope.account,
		    	password: $scope.password
		    }
		}).success(function(data){
			if (data.status === 200) {
				$scope.online = true;
				$scope.username = data.username;
				$scope.job = data.job;
				
				$cookies.put("username", data.username);
				$cookies.put("job", data.job);
				$cookies.put("userID", data.userID);
				$cookies.put("userType", data.userType);
			} else {
				aler(data.reason);
			}
		})
	}
	
	//退出登陆
	$scope.logout = function () {
		$scope.online = false;
	}
}])
.controller('InformationController', ['$scope', '$location', '$cookies', '$http', '$window', 'GetCarrierList', 'GetCarList', 'GetUserList', function($scope, $location, $cookies, $http, $window, GetCarrierList, GetCarList, GetUserList) {
	
	//查询出所有人员信息
	GetUserList.get().then(function(data) {
		$scope.userList = data.userList;
	});
	
	
	//查询出所有车辆信息
	GetCarList.get().then(function(data) {
		$scope.carList = data.carList;
	});

	//查询出所有承运商信息
	GetCarrierList.get().then(function(data) {
		$scope.carrierList = data.carrierList;
	});
	
	//点击弹出添加车辆信息模态框
	$scope.carModalShow = function () {
		$("#carModal").modal("toggle");
	}
	
	$scope.selectCarrier = false;
	//点击承运商单选框
	$scope.showCarrier = function () {
		$scope.selectCarrier = true;
	}
	//点击承运商单选框
	$scope.hideCarrier = function () {
		$scope.selectCarrier = false;
	}
	
	//添加车辆
	$scope.addCar = function () {
		
		var test = $scope.selectedCarrier;
		
		$http({
		    method:'post', 
		    url:'/logistics/car/addcar.do',
		    data:{
		    	carNumber: $scope.carNumber,
		    	carType: $scope.selectedCarType,
		    	capacity: $scope.weight,
		    	belong: $scope.selfCar === undefined? $scope.carrierCar:$scope.selfCar,
    			carrierID: $scope.selectedCarrier === undefined? "-1" : $scope.selectedCarrier.carrierID,
		    	carrierName: $scope.selectedCarrier ===undefined? "" : $scope.selectedCarrier.carrierName
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarList.get().then(function(data) {
					$scope.carList = data.carList;
				});
			} else {
				alert("添加车辆信息失败");
			}
		})
	}
	
	//删除车辆信息
	$scope.deleteCar = function (carID) {
		$http({
		    method:'post', 
		    url:'/logistics/car/deletecar.do',
		    data:{
		    	carID: carID
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarList.get().then(function(data) {
					$scope.carList = data.carList;
				});
			} else {
				alert("删除车辆信息失败");
			}
		})
	}
	
	//点击添加承运商信息模态框
	$scope.carrierModalShow = function () {
		$("#carrierModal").modal("toggle");
	}
	
	//添加承运商信息
	$scope.addCarrier = function () {
		
		$http({
		    method:'post', 
		    url:'/logistics/carrier/addcarrier.do',
		    data:{
		    	carrierName: $scope.carrierName,
		    	carrierTelephone: $scope.carrierTelephone,
		    	carrierAddress: $scope.carrierProvince+$scope.carrierCity+$scope.carrierDistrict+$scope.carrierDetailAddress
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarrierList.get().then(function(data) {
					$scope.carrierList = data.data;
				});
			} else {
				alert("添加承运商失败！");
			}
		})
	}
	
	//根据承运商id删除承运商
	$scope.deleteCarrier = function (carrierID) {
		if (carrierID != undefined || carrierID != null) {
			$http({
			    method:'post', 
			    url:'/logistics/carrier/deletecarrier.do',
			    data:{
			    	carrierID: carrierID
			    }
			}).success(function(data){
				if (data.status === 200) {
					GetCarrierList.get().then(function(data) {
						$scope.carrierList = data.data;
					});
				} else {
					alert("删除承运商信息失败");
				}
			})
		}
	}
	
}])
.controller('OperationController', ['$scope', '$location', '$cookies', '$http', '$window', function($scope, $location, $cookies, $http, $window) {
	
}]);