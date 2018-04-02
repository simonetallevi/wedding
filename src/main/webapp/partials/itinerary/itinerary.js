(function() {
    'use strict';

    angular.module('wedding')
        .controller('ItineraryCtrl', ItineraryCtrl);

    ItineraryCtrl.$inject = ['$log', '$rootScope', '$scope', 'SERVICE', 'Utils', '$http', '$timeout', '$window', '$uibModal', '$document'];

    function ItineraryCtrl($log, $rootScope, $scope, SERVICE, Utils, $http, $timeout, $window, $uibModal, $document) {
        var self = this;

        self.init = function() {
            $window.scrollTo(0, 0);
            $rootScope.navBarShrink(true);
        };
    }
})();