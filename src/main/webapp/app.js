(function() {
    'use strict';

    angular.module('wedding', ['ngAnimate', 'ngTouch', 'ui.bootstrap', 'ui.router', 'duScroll'])

    .config(['$stateProvider', '$urlRouterProvider',
        function($stateProvider, $urlRouterProvider) {

            // Routing
            $stateProvider
                .state('home', {
                    url: "/home",
                    templateUrl: "partials/home/home.html",
                    controller: 'HomeCtrl as Home'
                });

            $urlRouterProvider.otherwise("/home");
        }
    ])

    .directive('setHeight', function($window) {
        return {
            link: function(scope, element, attrs) {
                element.css('height', $window.innerHeight + 'px');
            }
        }
    })

    .run(['$rootScope', '$log', '$timeout',
        function($rootScope, $log, $timeout) {
            $rootScope.initialized = true;
            $rootScope.showSpinner = false;
        }
    ])
})();