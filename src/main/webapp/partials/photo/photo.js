(function() {
    'use strict';

    angular.module('wedding')
        .controller('PhotoCtrl', PhotoCtrl);

    PhotoCtrl.$inject = ['$log', '$rootScope', '$scope', 'SERVICE', 'Utils', '$http', '$timeout', '$window'];

    function PhotoCtrl($log, $rootScope, $scope, SERVICE, Utils, $http, $timeout, $window) {
        var self = this;

        self.collection = [];
        self.getPerRow = function() {
            return $window.innerWidth > 1000 ? 3 : 2;
        };

        self.loadImg = function(){
            var req = Utils.reqConfig("GET", 'https://api.flickr.com/services/rest/');
            req.params['method'] = 'flickr.photosets.getPhotos';
            req.params['photoset_id'] = '72157662294331840';
            req.params['user_id'] = '49259625@N04';
            req.params['api_key'] = 'aa2e9cd506ffb3374f4e5292d85e355e';
            req.params['format'] = 'json';
            req.params['nojsoncallback'] = '1';
            req.params['extras'] = 'url_l';
            req.params['per_page'] = 100;
            req.params['page'] = 1;

            $http(req)
                .then(function(resp){
                    console.log(resp);
                    if(!resp.data.photoset
                        || !resp.data.photoset.photo){
                        console.log("No photo!");
                        return
                    }
                    var photos = resp.data.photoset.photo;
                    photos.forEach(function(p){
                       self.collection.push({
                           ratio: (p.width_l / p.height_l),
                           color: '#' + ('000000' + Math.floor(Math.random() * 16777215).toString(16)).slice(-6),
                           src: p.url_l
                       });
                    });
                }, function(error){
                    console.log(error);
                });
        }

        self.init = function() {
            $rootScope.navBarShrink(true);
            self.loadImg();
        };
    }
})();