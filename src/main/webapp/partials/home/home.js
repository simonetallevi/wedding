(function() {
    'use strict';

    angular.module('wedding')
        .factory('HomeService', HomeService)
        .controller('HomeCtrl', HomeCtrl);

    HomeService.$inject = ['$rootScope', 'SERVICE', '$http', '$q', 'Utils'];
    HomeCtrl.$inject = ['$log', '$rootScope', 'HomeService'];

    function HomeService($rootScope, SERVICE, $http, $q, Utils) {
        var self = this;
        return {

        }
    }

    function HomeCtrl($log, $rootScope, HomeService) {
        var self = this;

        self.init = function() {
            var container = angular.element(document.getElementById('page-top'));
            container.on('scroll', function() {
                console.log('Container scrolled to ', container.scrollLeft(), container.scrollTop());
            });
        };
    }
})();