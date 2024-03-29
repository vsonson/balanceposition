(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('WellnessHistoryDetailController', WellnessHistoryDetailController);

    WellnessHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WellnessHistory', 'WellnessItem', 'UserInfo'];

    function WellnessHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, WellnessHistory, WellnessItem, UserInfo) {
        var vm = this;

        vm.wellnessHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:wellnessHistoryUpdate', function(event, result) {
            vm.wellnessHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
