(function() {
    'use strict';

    angular.module('wedding')
        .factory('HomeService', HomeService)
        .controller('HomeCtrl', HomeCtrl);

    HomeService.$inject = ['$rootScope', 'SERVICE', '$http', '$q', 'Utils'];
    HomeCtrl.$inject = ['$log', '$rootScope', 'HomeService', '$timeout', '$window'];

    function HomeService($rootScope, SERVICE, $http, $q, Utils) {
        var self = this;
        return {

        }
    }

    function HomeCtrl($log, $rootScope, HomeService, $timeout, $window) {
        var self = this;

        self.init = function() {
            $window.scrollTo(0, 0);
            $rootScope.navBarShrink(false);
        };
    }
})();