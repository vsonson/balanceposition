(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathHistoryDetailController', PathHistoryDetailController);

    PathHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PathHistory', 'PathWay', 'PathStep', 'PathAction'];

    function PathHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, PathHistory, PathWay, PathStep, PathAction) {
        var vm = this;

        vm.pathHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:pathHistoryUpdate', function(event, result) {
            vm.pathHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
