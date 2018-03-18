(function() {
    'use strict';

    angular.module('wedding')
        .factory('PhotoService', PhotoService)
        .controller('PhotoCtrl', PhotoCtrl);

    PhotoService.$inject = ['$rootScope', 'SERVICE', '$http', '$q', 'Utils'];
    PhotoCtrl.$inject = ['$log', '$rootScope', 'PhotoService', '$timeout', '$window'];

    function PhotoService($rootScope, SERVICE, $http, $q, Utils) {
        var self = this;
        return {

        }
    }

    function PhotoCtrl($log, $rootScope, PhotoService, $timeout, $window) {
        var self = this;

           self.photos = [
            "https://i.vimeocdn.com/portrait/58832_300x300",
            "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg?h=350&dpr=2&auto=compress&cs=tinysrgb",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwswZVnaDs2Sn9AyTVbEhdfGc3cCr3tbh_tiytTGd_cJf1d8_a"
           ]

        self.collection = [];
        self.getPerRow = function() {
            return $window.innerWidth > 1000 ? 3 : 2;
        };

        self.init = function() {
            for (var i = 0; i < 50; i++) {
                self.collection.push({
                    ratio: Math.max(0.5, Math.random() * 2),
                    color: '#' + ('000000' + Math.floor(Math.random() * 16777215).toString(16)).slice(-6),
                    src: self.photos[i%3]
                });
            }
        };
    }
})();