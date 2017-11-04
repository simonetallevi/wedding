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

    .directive('setHeight', ['$window', function($window) {
        return {
            link: function(scope, element, attrs) {
                element.css('height', $window.innerHeight + 'px');
            }
        }
    }])

    .directive('googleMap', ['$window', 'MAP', function($window, MAP) {
        var self = this;

        return {
            scope: {
                map: '@',
                marker: '@'
            },
            link: function(scope, element, attrs) {
                var mapOptions = {
                    zoom: 11,
                    center: new google.maps.LatLng(MAP.lat, MAP.long), // New York
                    disableDefaultUI: true,
                    scrollwheel: false,
                    draggable: false,
                    styles: MAP.styles
                };
                scope.map = new google.maps.Map(element[0], mapOptions);

                scope.marker = new google.maps.Marker({
                    map: scope.map,
                    draggable: false,
                    animation: google.maps.Animation.DROP,
                    position: { lat: MAP.lat, lng: MAP.long }
                });

                scope.marker.addListener('click', function() {
                    window.open("https://www.google.it/maps/place/Maklas/@39.2018075,9.5634451,15z/data=!4m5!3m4!1s0x0:0xa374f950792d396a!8m2!3d39.2018075!4d9.5634451", '_blank');
                });
            },
            controller: function() {}
        };
    }])

    .run(['$rootScope', '$log', '$timeout',
        function($rootScope, $log, $timeout) {
            $rootScope.initialized = true;
            $rootScope.showSpinner = false;
        }
    ])
})();