(function () {
    'use strict';

    angular.module('wedding', ['ngAnimate', 'ngTouch', 'ui.bootstrap'])

        .run(['$rootScope', '$log', '$timeout',
            function ($rootScope, $log, $timeout) {
                $rootScope.initialized = true;
                $rootScope.showSpinner = false;
            }])
})();