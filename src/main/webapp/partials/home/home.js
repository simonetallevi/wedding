(function() {
    'use strict';

    angular.module('wedding')
        .factory('HomeService', HomeService)
        .controller('HomeCtrl', HomeCtrl);

    HomeService.$inject = ['$rootScope', 'SERVICE', '$http', '$q', 'Utils'];
    HomeCtrl.$inject = ['$log', '$rootScope', 'HomeService', '$timeout'];

    function HomeService($rootScope, SERVICE, $http, $q, Utils) {
        var self = this;
        return {

        }
    }

    function HomeCtrl($log, $rootScope, HomeService, $timeout) {
        var self = this;

        self.init = function() {

            $timeout(function(){
                var res = hello.getAuthResponse('google');
                            console.log(res);
            }, 1000)
        };
    }
})();