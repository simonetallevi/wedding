(function() {
    'use strict';

    angular.module('wedding')
        .controller('PhotoCtrl', PhotoCtrl);

    PhotoCtrl.$inject = ['$log', '$rootScope', '$scope', 'SERVICE', 'Utils', '$http', '$timeout', '$window', '$uibModal', '$document'];

    function PhotoCtrl($log, $rootScope, $scope, SERVICE, Utils, $http, $timeout, $window, $uibModal, $document) {
        var self = this;

        self.hasMore = true;
        self.loading = false;
        self.currentPage = 1;
        self.collection = [];
        self.getPerRow = function() {
            return $window.innerWidth > 1000 ? 3 : 2;
        };

        self.loadNext = function(){
            if(!self.loading && self.hasMore){
                console.log("LOAD - NEXT " + self.currentPage);
                self.loadImg();
            }
        }

        self.selectImg = function(index, tile){
            console.log(index, tile);
            var parentElem = angular.element($document[0].querySelector('#modal-parent'));
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'partials/photo/modal.slideshow.html',
                controller: function(items){
                    var $ctrl = this;
                    $ctrl.inputs = items;
                    $ctrl.currentIndex = index - 1;
                    $ctrl.hasLess = false;
                    $ctrl.hasMore = false;

                    $ctrl.hasLessMore = function(){
                        if($ctrl.currentIndex > 0){
                            $ctrl.hasLess = true;
                        }else{
                            $ctrl.hasLess = false;
                        }
                        if($ctrl.currentIndex + 1 < $ctrl.inputs.length){
                            $ctrl.hasMore = true;
                        }else{
                            $ctrl.hasMore = false;
                        }
                    }

                    $ctrl.next = function(){
                        if($ctrl.currentIndex + 1 < $ctrl.inputs.length){
                            $ctrl.currentIndex += 1;
                            $ctrl.tile = $ctrl.inputs[$ctrl.currentIndex];
                        }
                        $ctrl.hasLessMore();
                    }

                    $ctrl.back = function(){
                        if($ctrl.currentIndex - 1 < 0){
                            return
                        }
                        $ctrl.currentIndex -= 1;
                        $ctrl.tile = $ctrl.inputs[$ctrl.currentIndex];
                        $ctrl.hasLessMore();
                    }

                    $ctrl.togglePlay = function(){
                        $ctrl.play = !$ctrl.play;
                        if($ctrl.play){
                            $ctrl.playNext();
                        }else{
                            $timeout.cancel($ctrl.timeoutPlay);
                        }
                    }

                    $ctrl.close = function(){
                        $timeout.cancel($ctrl.timeoutPlay);
                        $ctrl.hide = false;
                        $ctrl.play = false;
                        modalInstance.dismiss();
                    }

                    $ctrl.keyEvents = function($event){
                        if ($event.keyCode == 39){
                            $ctrl.next();
                        } else if ($event.keyCode == 37){
                            $ctrl.back();
                        }
                    }

                    $ctrl.init = function(){
                        $ctrl.next()
                    }
              },
              controllerAs: 'Modal',
              appendTo: parentElem,
              resolve: {
                items: function () {
                  return self.collection
                }
              }
            });

            modalInstance.result.then(function(){
              //Get triggers when modal is closed
             }, function(){
              //gets triggers when modal is dismissed.
             });
        }

        self.loadImg = function(){
            self.loading = true;
            var req = Utils.reqConfig("GET", 'https://api.flickr.com/services/rest/');
            req.params['method'] = 'flickr.photos.search';
            req.params['user_id'] = '160363174@N07';
            req.params['api_key'] = '3592844f07dede57633c527de3e2ac3d';
            req.params['format'] = 'json';
            req.params['nojsoncallback'] = '1';
            req.params['extras'] = 'url_l';
            req.params['per_page'] = 50;
            req.params['page'] = self.currentPage;

            $http(req)
                .then(function(resp){
                    console.log(resp);
                    if(!resp.data.photos
                        || !resp.data.photos.photo
                        || resp.data.photos.photo.length == 0){
                        console.log("No photo!");
                        self.hasMore = false;
                    }else{
                        var photos = resp.data.photos.photo;
                        photos.forEach(function(p){
                           self.collection.push({
                               ratio: (p.width_l / p.height_l),
                               color: '#' + ('000000' + Math.floor(Math.random() * 16777215).toString(16)).slice(-6),
                               src: p.url_l
                           });
                        });
                    }

                    self.currentPage++
                    $timeout(function(){
                        self.loading = false;
                    }, 1000);
                }, function(error){
                    console.log(error);
                    $timeout(function(){
                        self.loading = false;
                    }, 1000);
                });
        }

        self.init = function() {
            $window.scrollTo(0, 0);
            $rootScope.navBarShrink(true);
            self.hasMore = true;
            self.loading = false;
            self.currentPage = 1;
            self.collection = [];
            self.loadImg();
        };
    }
})();