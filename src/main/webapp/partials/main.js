(function() {
        'use strict';

        angular.module('wedding')
            .factory('MainService', MainService)
            .controller('MainCtrl', MainCtrl);

        MainService.$inject = ['$rootScope', 'SERVICE', '$http', '$q', 'Utils'];
        MainCtrl.$inject = ['$log', '$rootScope', 'MainService'];

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

        function MainCtrl($log, $rootScope, MainService) {
            var self = this;

            self.init = function() {

            };
        }
})();