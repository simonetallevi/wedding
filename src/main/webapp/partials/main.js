(function() {
    'use strict';

    angular.module('wedding')
        .factory('MainService', MainService)
        .controller('MainCtrl', MainCtrl);

    MainService.$inject = ['$rootScope', 'SERVICE', '$http', '$q', 'Utils'];
    MainCtrl.$inject = ['$log', '$rootScope', '$document', '$scope', 'MainService'];

    function MainService($rootScope, SERVICE, $http, $q, Utils) {
        var self = this;

        var _goTo = function(state, params) {
            self.currentState = state;
            $state.go(state, params);
        };

        return {
            goTo: function(state, params) {
                _goTo(state, params);
            }
        }
    }

    function MainCtrl($log, $rootScope, $document, $scope, MainService) {
        var self = this;

        self.scrollActive = false;
        self.menuToggled = 'none';
        self.navBar = angular.element(document.getElementById('mainNav'));
        self.toggleMenuScrollTop = 0;

        self.toggleMenu = function(){
            if(self.menuToggled == 'none'){
                self.menuToggled = 'block';
                self.toggleMenuScrollTop = $document.scrollTop();
            } else {
                self.menuToggled = 'none';
            }
        }

        self.init = function() {

            $document.on('scroll', function() {
                if($document.scrollTop() >= 100 && !self.scrollActive){
                    self.scrollActive = true;
                    self.navBar.addClass("navbar-shrink");
                    $log.info("adding class");
                }else if($document.scrollTop() < 100 && self.scrollActive){
                    self.scrollActive = false;
                    self.navBar.removeClass("navbar-shrink");
                    $log.info("removing class");
                }
                if(self.menuToggled == 'block'
                    && Math.abs(self.toggleMenuScrollTop - $document.scrollTop()) > 100){
                    self.toggleMenu();
                    $scope.$apply();
                }
            });
        };
    }
})();