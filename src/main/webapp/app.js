(function() {
    'use strict';

    angular.module('wedding', ['ngAnimate', 'ngTouch', 'ui.bootstrap', 'ui.bootstrap.modal',
        'ui.router', 'duScroll', 'hj.gridify', 'infinite-scroll'])

    .config(['$stateProvider', '$urlRouterProvider',
        function($stateProvider, $urlRouterProvider) {

            // Routing
            $stateProvider
                .state('home', {
                    url: "/",
                    templateUrl: "partials/home/home.html",
                    controller: 'HomeCtrl as Home'
                })
                .state('photo', {
                    url: "/photo",
                    templateUrl: "partials/photo/photo.html",
                    controller: 'PhotoCtrl as Photo'
                })
                .state('itinerary', {
                    url: "/itinerary",
                    templateUrl: "partials/itinerary/itinerary.html",
                    controller: 'ItineraryCtrl as Itinerary'
                });

            $urlRouterProvider.otherwise("/");
        }
    ])

    .directive('backImg', function(){
        return function(scope, element, attrs){
            var url = attrs.backImg;
            element.css({
                'background-image': 'url(' + url +')',
                'background-size' : 'cover',
                'background-position': 'center'
            });
        };
    })

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

    .directive('googleMapHoneyMoon', ['$window', 'HONEYMOON', 'MAP', function($window, HONEYMOON, MAP) {
            var self = this;

            return {
                scope: {
                    map: '@',
                },
                link: function(scope, element, attrs) {
                    var mapOptions = {
//                        zoom: 5,
//                        center: new google.maps.LatLng(HONEYMOON.lat, HONEYMOON.long),
                        disableDefaultUI: false,
                        scrollwheel: true,
                        draggable: true,
                        styles: MAP.styles
                    };

                    var line = [];
                    scope.map = new google.maps.Map(element[0], mapOptions);
                    var markers = [];
                    var infos = [];
                    HONEYMOON.itinerary.forEach(function(loc){
                        var marker = new google.maps.Marker({
                            map: scope.map,
                            title: loc.title,
                            draggable: false,
                            animation: google.maps.Animation.DROP,
                            position: { lat: loc.lat, lng: loc.long }
                        });
                        var info = new google.maps.InfoWindow({
                          content: '<a class="link" href="'+loc.link+'">'+loc.title+'</a>'
                        });
                        marker.addListener('click', function(e) {
                            infos.forEach(function(i){
                                i.close();
                            });
                            info.open(scope.map, marker);
                        });
                        line.push({ lat: loc.lat, lng: loc.long });
                        infos.push(info);
                        markers.push(marker);
                    });
                    var flightPath = new google.maps.Polyline({
                        path: line,
                        geodesic: true,
                        strokeColor: '#FF0000',
                        strokeOpacity: 1.0,
                        strokeWeight: 2
                    });
                    var bounds = new google.maps.LatLngBounds();
                    for (var i = 0; i < markers.length; i++) {
                        bounds.extend(markers[i].getPosition());
                    }
                    flightPath.setMap(scope.map);
                    scope.map.fitBounds(bounds);
                },
                controller: function() {}
            };
        }])

    .run(['$rootScope', '$log', '$timeout',
        function($rootScope, $log, $timeout) {
            $rootScope.initialized = true;
            $rootScope.showSpinner = false;

            $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
                console.log(event, toState, toParams, fromState, fromParams);
            });
        }
    ])
})();