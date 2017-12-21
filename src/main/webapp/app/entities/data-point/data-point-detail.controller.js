(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('DataPointDetailController', DataPointDetailController);

    DataPointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DataPoint'];

    function DataPointDetailController($scope, $rootScope, $stateParams, previousState, entity, DataPoint) {
        var vm = this;

        vm.dataPoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:dataPointUpdate', function(event, result) {
            vm.dataPoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
