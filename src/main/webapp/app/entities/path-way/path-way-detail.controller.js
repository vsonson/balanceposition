(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathWayDetailController', PathWayDetailController);

    PathWayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PathWay', 'PathStep', 'UserNotification'];

    function PathWayDetailController($scope, $rootScope, $stateParams, previousState, entity, PathWay, PathStep, UserNotification) {
        var vm = this;

        vm.pathWay = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:pathWayUpdate', function(event, result) {
            vm.pathWay = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
