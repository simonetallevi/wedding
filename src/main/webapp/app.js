(function () {
    'use strict';
    angular.module('Wedding', [])

        .run(['$rootScope', '$log', '$timeout',
            function ($rootScope, $log, $timeout) {
                $rootScope.initialized = true;
                $rootScope.showSpinner = false;
            }])
})();