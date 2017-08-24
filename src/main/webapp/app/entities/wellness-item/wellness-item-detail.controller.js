(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('WellnessItemDetailController', WellnessItemDetailController);

    WellnessItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WellnessItem', 'Trigger'];

    function WellnessItemDetailController($scope, $rootScope, $stateParams, previousState, entity, WellnessItem, Trigger) {
        var vm = this;

        vm.wellnessItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:wellnessItemUpdate', function(event, result) {
            vm.wellnessItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
